package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * A strategy for collisions
 */
public class AddObjectsStrategy implements BrickStrategyDecorator{
    private final BrickStrategy strategy; // for decoration usage
    private final GameObjectCollection gameObjects; // game objects
    private final GameObject[] objects; // initialized objects to add to the game upon collision


    /**
     * Constructor for strategy that adds objects to a specified location upon collision
     * @param strategy      BrickStrategy for decoration usage
     * @param gameObjects   The GameObjectCollection of current game objects
     * @param objects       Initialized objects to add to the game upon collision
     */
    public AddObjectsStrategy(BrickStrategy strategy, GameObjectCollection gameObjects,
                              GameObject [] objects){
        this.strategy = strategy;
        this.gameObjects = gameObjects;
        this.objects = objects;
    }

    /**
     * Adds flying objects to game at center of collidedObj upon collision
     * @param collidedObj GameObject to remove
     * @param colliderObj GameObject that collided into the object
     * @param bricksCounter Amount of bricks left in game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        strategy.onCollision(collidedObj, colliderObj, bricksCounter);
        for (GameObject object : objects) {
            object.setCenter(collidedObj.getCenter());
            gameObjects.addGameObject(object);
        }
    }
}
