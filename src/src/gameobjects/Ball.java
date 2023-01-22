package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class for creating Ball GameObjects
 */
public class Ball extends GameObject {
    private final Sound sound; // sound to make upon ball collision
    private final Counter collisionCounter; // number of collisions a ball has had

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height in ball coordinates.
     * @param renderable           The renderable representing the object. Can be null, in which case
     *                             the GameObject will not be rendered.
     * @param sound                The sound the ball makes upon collision
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        this.sound = sound;
        this.collisionCounter = new Counter(0);
    }

    /**
     * Called on the first frame of a collision.
     * Changes speed and creates sound upon collision
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // changes speed when collision occurs
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionCounter.increment();
        sound.play();
    }

    /**
     * @return Counter for total ball collisions
     */
    public Counter getCollisionCounter(){
        return collisionCounter;
    }
}
