package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * Class for creating Paddle GameObject
 */
public class Paddle extends GameObject {
    private final UserInputListener inputListener; // listens to the user input
    private final Vector2 windowDimensions; // dimensions of game window
    private final int minDistFromEdge; // distance paddle can get from edge
    public static final int MOVE_SPEED = 500; // Speed of paddle movement

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param inputListener     Listens in the keyboard input
     * @param windowDimensions  Dimensions of game window
     * @param minDistFromEdge   Minimum distance the paddle can get to the edge
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * Should be called once per frame.
     * Moves paddle according to user input direction
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
        Vector2 movementDir = Vector2.ZERO;
        // if user selected left
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                this.getTopLeftCorner().x() > minDistFromEdge) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        // if user selected right
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                windowDimensions.x() - minDistFromEdge > getTopLeftCorner().x() + getDimensions().x()) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        // update speed
        setVelocity(movementDir.mult(MOVE_SPEED));
    }
}
