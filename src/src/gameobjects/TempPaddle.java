package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class TempPaddle extends Paddle{
    private int currentLives; // number of lives the object has left
    private final int totalLives; // total number of lives the object received
    private final GameObjectCollection gameObjects; // current game objects

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    Listens in the keyboard input
     * @param windowDimensions Dimensions of game window
     * @param minDistFromEdge  Minimum distance the paddle can get to the edge
     * @param totalLives       Integer of total number of lives the object has per usage
     * @param gameObjects      The GameObjectCollection of current game objects
     */
    public TempPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener
            inputListener, Vector2 windowDimensions, int minDistFromEdge, int totalLives,
                      GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.totalLives = totalLives;
        this.currentLives = totalLives;
        this.gameObjects = gameObjects;
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
        return other instanceof Ball;
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
        // decrements object's lives upon collision
        currentLives--;
        if (currentLives <= 0 && gameObjects.removeGameObject(this)){
            //resets currentLives for next usage of object
            currentLives = totalLives;
        }

    }
}
