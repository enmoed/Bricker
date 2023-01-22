package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;

import java.util.Random;

/**
 * Factory for selection of brick collision strategies
 */
public class BrickStrategyFactory {
    /**
     * Determines the strategies of a brick
     * @param gameObjects   The GameObjectCollection of the current game objects
     * @param pucks         An array of arrays of Pucks to be used upon multiple usages of puck strategy
     * @param tempPaddle    A Paddle to be used if strategy invoked
     * @param gameManager   The current GameManager
     * @param camera        The Camera instance to switch to upon usage
     * @param hearts        An array of Hearts to be used upon multiple usages of additional life strategy
     * @param ball          Game Ball object to be used for understanding cause of collision
     * @return              BrickStrategy to be implemented
     */
    public static BrickStrategy brickStrategy(GameObjectCollection gameObjects, GameObject[][] pucks,
                                              GameObject tempPaddle, GameManager gameManager, Camera camera,
                                              GameObject [] hearts, GameObject ball){
        BrickStrategy strategy = new CollisionStrategy(gameObjects);
        Random random = new Random();
        // choosing strategy between all 6 options
        switch (random.nextInt(6)) {
            case (0):
                return extraStrategy(strategy, gameObjects, pucks, tempPaddle, gameManager, camera,
                        hearts, ball);
            case (1):
                return new AddObjectsStrategy(strategy, gameObjects, pucks[0]);
            case (2):
                return new AddLocatedObjectStrategy(strategy, gameObjects, tempPaddle);
            case (3):
                return new ChangeCameraStrategy(strategy, gameManager, camera, ball);
            case (4):
                return new AddLocatedObjectStrategy(strategy, gameObjects, hearts[0]);
            default:
                return strategy;

        }
    }

    /**
     * Determines the strategies of a brick after extra strategy was selected
     * @param strategy      The strategies already selected and now being decorated
     * @param gameObjects   The GameObjectCollection of the current game objects
     * @param pucks         An array of arrays of Pucks to be used upon multiple usages of puck strategy
     * @param tempPaddle    A Paddle to be used if strategy invoked
     * @param gameManager   The current GameManager
     * @param camera        The Camera instance to switch to upon usage
     * @param hearts        An array of Hearts to be used upon multiple usages of additional life strategy
     * @param ball          Game Ball object to be used for understanding cause of collision
     * @return              BrickStrategy to be implemented
     */
    private static BrickStrategy extraStrategy(BrickStrategy strategy, GameObjectCollection gameObjects,
                                               GameObject[][] pucks, GameObject tempPaddle,
                                               GameManager gameManager, Camera camera, GameObject [] hearts,
                                               GameObject ball) {
        Random random = new Random();
        int usedExtraStrategy=0;
        for (int i = 0; i < 1; i++){
            // choosing strategy twice between 6 options then 5 options
            switch (random.nextInt(6-i-usedExtraStrategy)) {
                case (0):
                    strategy = new AddLocatedObjectStrategy(strategy, gameObjects,
                            hearts[i-usedExtraStrategy]);
                    break;
                case (1):
                    strategy = new AddObjectsStrategy(strategy, gameObjects,
                            pucks[i-usedExtraStrategy]);
                    break;
                case (2):
                    strategy = new AddLocatedObjectStrategy(strategy, gameObjects, tempPaddle);
                    break;
                case (3):
                    strategy = new ChangeCameraStrategy(strategy, gameManager, camera, ball);
                    break;
                case (4):
                    usedExtraStrategy = 1;
                    strategy = lastStrategy(strategy, gameObjects, pucks, tempPaddle,
                            gameManager, camera, hearts, ball);
                    break;

                }
            }
        return strategy;
    }

    /**
     * Determines the strategies of a brick after extra strategy was selected for a second time
     * @param strategy      The strategies already selected and now being decorated
     * @param gameObjects   The GameObjectCollection of the current game objects
     * @param pucks         An array of arrays of Pucks to be used upon multiple usages of puck strategy
     * @param tempPaddle    A Paddle to be used if strategy invoked
     * @param gameManager   The current GameManager
     * @param camera        The Camera instance to switch to upon usage
     * @param hearts        An array of Hearts to be used upon multiple usages of additional life strategy
     * @param ball          Game Ball object to be used for understanding cause of collision
     * @return              BrickStrategy to be implemented
     */
    private static BrickStrategy lastStrategy(BrickStrategy strategy, GameObjectCollection gameObjects,
                                              GameObject[][] pucks, GameObject tempPaddle,
                                              GameManager gameManager, Camera camera, GameObject [] hearts,
                                              GameObject ball) {
        Random random = new Random();
        for (int i = 0; i < 1; i++){
            // choosing two strategies between 5 options then 4 options, after extra strategy selected again
            switch (random.nextInt(5-i)) {
                case (0):
                    strategy = new AddLocatedObjectStrategy(strategy, gameObjects, hearts[i+1]);
                    break;
                case (1):
                    strategy = new AddObjectsStrategy(strategy, gameObjects, pucks[i+1]);
                    break;
                case (2):
                    strategy = new AddLocatedObjectStrategy(strategy, gameObjects, tempPaddle);
                    break;
                case (3):
                    strategy = new ChangeCameraStrategy(strategy, gameManager, camera, ball);
                    break;

            }
        }
        return strategy;
    }
}
