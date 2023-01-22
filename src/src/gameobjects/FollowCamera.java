package src.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class FollowCamera extends Camera {

    private final Counter collisionCounter; // counter to determine when to revert camera settings
    private final GameManager gameManager; // game manager
    private int startAmount; // allows us to calculate the delta of collisions
    private final int amountOfCollisions; // the number of collisions needed to revert camera settings

    /**
     * Construct a new Camera which covers a given rectangle in the world.
     *
     * @param objToFollow           a GameObject the Camera should follow around.
     *                              If this parameter is null, the rectangle viewed will begin
     *                              in the world origin (Vector2.ZERO).
     * @param deltaRelativeToObject the desired delta between the camera's center and
     *                              objToFollow's center. A value of Vector2.ZERO
     *                              means objToFollow will always appear in the dead center.
     *                              A value of Vector2.UP.mult(50) means the object will
     *                              appear slightly below the view's center. This parameter
     *                              is meaningless if objToFollow is null.
     * @param dimensions            the dimensions (in world-coordinates) of the rectangle the
     *                              camera should cover
     * @param windowDimensions      the dimensions, in pixels, of the window
     * @param ballCollisionCounter  Counts the number of ball collisions throughout the game
     * @param amountOfCollisions    The number of collisions needed to revert camera settings
     * @param gameManager           The GameManager of the current game
     */
    public FollowCamera(GameObject objToFollow, Vector2 deltaRelativeToObject, Vector2 dimensions,
                        Vector2 windowDimensions, Counter ballCollisionCounter, int amountOfCollisions,
                        GameManager gameManager) {
        super(objToFollow, deltaRelativeToObject, dimensions, windowDimensions);
        this.collisionCounter = ballCollisionCounter;
        this.gameManager = gameManager;
        this.amountOfCollisions = amountOfCollisions;
        startAmount = 0;
    }

    /**
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
        // if camera is being used
        if (gameManager.getCamera() == this)
        {
            // if this is the first time it is being updated since current use
            if (startAmount == 0)
                startAmount = collisionCounter.value();
            // if this is not the first time it is being updated since current use we calculate the delta
            else if (startAmount + amountOfCollisions <= collisionCounter.value()) {
                // resets camera for next usage
                gameManager.setCamera(null);
                startAmount = 0;
            }
        }
    }
}
