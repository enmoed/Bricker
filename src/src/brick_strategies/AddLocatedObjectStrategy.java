package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class AddLocatedObjectStrategy implements BrickStrategy {
    private final BrickStrategy strategy;
    private final GameObjectCollection gameObjects;
    private final GameObject object;

    /**
     * Constructor for strategy that adds objects to a predetermined location upon collision
     * @param strategy      BrickStrategy for decoration usage
     * @param gameObjects   The GameObjectCollection of current game objects
     * @param object       Initialized objects to add to the game upon collision
     */
    public AddLocatedObjectStrategy(BrickStrategy strategy, GameObjectCollection gameObjects,
                                    GameObject object) {
        this.strategy = strategy;
        this.gameObjects = gameObjects;
        this.object = object;
    }

    /**
     * Adds initialized object to game upon collision
     * @param collidedObj GameObject to remove
     * @param colliderObj GameObject that collided into the object
     * @param bricksCounter Amount of bricks left in game
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        strategy.onCollision(collidedObj, colliderObj, bricksCounter);
        gameObjects.addGameObject(object);
    }
}
