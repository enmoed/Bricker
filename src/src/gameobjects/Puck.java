package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball{
    private final GameObjectCollection gameObjects; // current game objects
    private final Vector2 boundaries; // boundaries of the pucks

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in ball coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param sound         The sound the ball makes upon collision
     * @param gameObjects   The GameObjectCollection of current game objects
     * @param boundaries    Vector2 boundaries of the pucks
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound,
                GameObjectCollection gameObjects, Vector2 boundaries) {
        super(topLeftCorner, dimensions, renderable, sound);
        this.gameObjects = gameObjects;
        this.boundaries = boundaries;
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
        if (this.getTopLeftCorner().y() > boundaries.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
