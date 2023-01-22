package src.gameobjects;

import src.brick_strategies.BrickStrategy;
import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class for creating brick GameObjects
 */
public class Brick extends GameObject {
    private final BrickStrategy collisionStrategy; // strategy of collision
    private final Counter bricksCounter; // amount of bricks in the game
    /**
     * Construct a new Brick instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionStrategy The strategy to play by when there is a collision
     * @param bricksCounter     A counter for the amount of bricks left in the game
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, CollisionStrategy
            collisionStrategy, Counter bricksCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.bricksCounter = bricksCounter;
    }
    /**
     * Construct a new Brick instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionStrategy The strategy to play by when there is a collision
     * @param bricksCounter     A counter for the amount of bricks left in the game
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, BrickStrategy
            collisionStrategy, Counter bricksCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Called on the first frame of a collision.
     * Calls the collision strategy
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other, bricksCounter);
    }
}
