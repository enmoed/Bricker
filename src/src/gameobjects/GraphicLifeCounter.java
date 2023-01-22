package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final int EXTRA_LIVES = 1; // default amount of extra lives for original constructor
    private final int extraLives; // number of extra lives the game accepts
    private final Counter livesCounter; // counts number of lives left
    private final GameObjectCollection gameObjectsCollection; // game objects
    private final Renderable widgetRenderable; // renderable image of a life widget
    private int numOfLives; // current number of lives left
    private final GameObject [] lives; // the different life objects

    /**
     * Construct a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner   Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions      Width and height in window coordinates.
     * @param livesCounter          Counter for the amount of lives left
     * @param widgetRenderable      The renderable representing the object. Can be null, in which case
     *                              the GameObject will not be rendered.
     * @param gameObjectsCollection Objects of the current game
     * @param numOfLives            Number of lives currently left
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.extraLives = EXTRA_LIVES;
        this.lives = new GameObject[numOfLives+extraLives];
        this.widgetRenderable = widgetRenderable;
        gameObjectsCollection.addGameObject(this);
        addLives(widgetTopLeftCorner, widgetDimensions);
    }
    /**
     * Construct a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner   Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions      Width and height in window coordinates.
     * @param livesCounter          Counter for the amount of lives left
     * @param widgetRenderable      The renderable representing the object. Can be null, in which case
     *                              the GameObject will not be rendered.
     * @param gameObjectsCollection Objects of the current game
     * @param numOfLives            Number of lives currently left
     * @param extraLives            Number of additional lives the game accepts
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives, int extraLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.extraLives = extraLives;
        this.lives = new GameObject[numOfLives+extraLives];
        this.widgetRenderable = widgetRenderable;
        gameObjectsCollection.addGameObject(this);
        addLives(widgetTopLeftCorner, widgetDimensions);
    }

    /**
     * Adds hearts to the game
     * @param widgetTopLeftCorner   the starting point of the first heart
     * @param widgetDimensions      the dimensions of the heart
     */
    private void addLives(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions) {
        for (int i = 0; i < numOfLives + extraLives; i++){
            Vector2 currTopLeftCorner = widgetTopLeftCorner.add
                    (Vector2.RIGHT.mult(widgetDimensions.mult(i).x()));
            if (i < numOfLives) {
                lives[i] = new GameObject(currTopLeftCorner, widgetDimensions, widgetRenderable);
            }
            else{ // extra hearts are rendered to null at the beginning of the game
                lives[i] = new GameObject(currTopLeftCorner, widgetDimensions, null);
            }
            // add hearts to layer 1
            gameObjectsCollection.addGameObject(lives[i], Layer.BACKGROUND);
        }
    }

    /**
     * Should be called once per frame.
     * Removes hearts if number of lives changes
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
        if (numOfLives == 0)
        {
            return;
        }
        // removes image if the lives have decreased
        if (livesCounter.value() < numOfLives) {
            // removes hearts from layer 1
            this.lives[numOfLives-1].renderer().setRenderable(null);
            numOfLives--;
        }
        // adds lives images while we have collected hearts
        else while(livesCounter.value() > numOfLives) {
            this.lives[numOfLives].renderer().setRenderable(widgetRenderable);
            numOfLives++;
        }
    }
}
