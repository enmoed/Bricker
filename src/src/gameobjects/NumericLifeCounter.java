package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Class for creating a numeric representation GameObject
 */
public class NumericLifeCounter extends GameObject {
    private final Counter livesCounter; // amount of lives left
    private final TextRenderable textRenderable; // numeric text renderer

    /**
     * Constructor for numeric life counter
     * @param livesCounter          Amount of lives left
     * @param topLeftCorner         Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param dimensions            Width and height in window coordinates.
     * @param gameObjectCollection  The objects of the game
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        textRenderable = new TextRenderable(livesCounter.toString());
        renderer().setRenderable(textRenderable);
        gameObjectCollection.addGameObject(this, Layer.BACKGROUND);
    }


    /**
     * Should be called once per frame.
     * Changes the color and number of the numeric counter to the current amount
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
        textRenderable.setString(Integer.toString(livesCounter.value()));
        // switches between the three options
        switch (livesCounter.value()) {
            case (1):
                textRenderable.setColor(Color.RED);
                return;
            case (2):
                textRenderable.setColor(Color.YELLOW);
                return;
            default:
                textRenderable.setColor(Color.GREEN);
        }
    }
}
