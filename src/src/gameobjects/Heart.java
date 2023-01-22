package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {
    private final GameObject paddle; // game paddle for checking if heart collided with it
    private final Counter livesCounter; // increments lives if heart got caught
    private final GameObjectCollection gameObjects; // current game objects
    private final int maxLives; // maximum value livesCounter can accept
    private final Vector2 boundaries; // boundaries of the current object

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param paddle        Paddle of the game to be compared to collider objects
     * @param livesCounter  Counter of the current number of lives
     * @param gameObjects   GameObjectCollection of all game objects
     * @param maxLives      The integer representing the maximum amount of lives accepted in the game
     * @param boundaries    Vector2 representing the boundaries of the object
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObject paddle,
                 Counter livesCounter, GameObjectCollection gameObjects, int maxLives, Vector2 boundaries) {
        super(topLeftCorner, dimensions, renderable);
        this.paddle = paddle;
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        this.maxLives = maxLives;
        this.boundaries = boundaries;
        setHeart();
    }

    /**
     * Should be called once per frame.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y()>boundaries.y()){
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Sets the starting position of the heart
     */
    private void setHeart() {
        this.setCenter(boundaries.mult(0.5f));
        this.setVelocity(new Vector2(0,100));
    }

    /**
     * Should this object be allowed to collide the the specified other object.
     * If both this object returns true for the other, and the other returns true
     * for this one, the collisions may occur when they overlap, meaning that their
     * respective onCollisionEnter/onCollisionStay/onCollisionExit will be called.
     * Note that this assumes that both objects have been added to the same
     * GameObjectCollection, and that its handleCollisions() method is invoked.
     *
     * @param other The other GameObject.
     * @return true if the objects should collide. This does not guarantee a collision
     * would actually collide if they overlap, since the other object has to confirm
     * this one as well.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == paddle;
    }

    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (maxLives > livesCounter.value() && gameObjects.removeGameObject(this)){
            livesCounter.increment();
        }
    }
}
