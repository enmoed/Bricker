package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * A strategy for collisions
 */
public class CollisionStrategy implements BrickStrategy{
    private final GameObjectCollection gameObjects; // game objects

    /**
     * Constructor for collision strategy
     * @param gameObjects Objects of the game
     */
    public CollisionStrategy(GameObjectCollection gameObjects){
        this.gameObjects = gameObjects;
    }

    /**
     * Removes collided object from game upon collision
     * @param collidedObj GameObject to remove
     * @param colliderObj GameObject that collided into the object
     * @param bricksCounter Amount of bricks left in game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        if(gameObjects.removeGameObject(collidedObj)){
            bricksCounter.decrement();
        }
    }
}
