package src.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * Interface for brick collision strategies
 */
public interface BrickStrategy {
    /**
     * Implements strategy upon collision
     * @param collidedObj GameObject to remove
     * @param colliderObj GameObject that collided into the object
     * @param bricksCounter Amount of bricks left in game
     */
    void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter);
}
