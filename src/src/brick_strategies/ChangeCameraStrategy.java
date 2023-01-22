package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;

/**
 * Brick collision strategy to change camera view
 */
public class ChangeCameraStrategy implements BrickStrategyDecorator {
    private final BrickStrategy strategy; // brick strategy to use when decorating
    private final GameManager gameManager; // game manager
    private final Camera camera; // camera object to set to game
    private final GameObject object; // object to check collision cause

    /**
     * Constructor for brick collision strategy that changes camera view
     * @param strategy      BrickStrategy to use when decorating
     * @param gameManager   GameManager representing current game
     * @param camera        Camera to use if collision strategy invoked
     * @param object        Object for comparing to collision cause
     */
    public ChangeCameraStrategy(BrickStrategy strategy, GameManager gameManager, Camera camera,
                                GameObject object) {
        this.strategy = strategy;
        this.gameManager = gameManager;
        this.camera = camera;
        this.object = object;
    }

    /**
     * Changes camera view upon collision with ball object
     * @param collidedObj GameObject to remove
     * @param colliderObj GameObject that collided into the object
     * @param bricksCounter Amount of bricks left in game
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        strategy.onCollision(collidedObj, colliderObj, bricksCounter);
        if (gameManager.getCamera() == null && colliderObj == object){
            gameManager.setCamera(camera);
        }
    }
}
