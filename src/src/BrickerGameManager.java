package src;

import danogl.gui.rendering.Camera;
import src.brick_strategies.BrickStrategy;
import src.brick_strategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Bricker game play class
 */
public class BrickerGameManager extends GameManager {
    public static final float BORDER_WIDTH = 10; // width of outer border of game
    private static final Renderable BORDER_RENDER =
            new RectangleRenderable(new Color(80, 140, 250)); // design of border
    private static final int NUM_LIVES = 3; // number of lives
    private static final int NUM_EXTRA_LIVES = 1; // number of extra lives
    private static final int BALL_DIAMETER = 35; // ball size
    private static final float BALL_SPEED = 200; // ball speed
    private static final int BRICK_ROWS = 8; // amount of rows of bricks
    private static final int BRICK_COLS = 7; // amount of colomns of bricks
    private static final Vector2 PADDLE_VECTOR = new Vector2(100, 20); // paddle dimensions
    private static final Vector2 HEART_VECTOR = new Vector2(20, 20); // heart dimensions
    private static final Vector2 NUMERIC_VECTOR = new Vector2(20, 20); // number of lives dimensions
    private static final String BALL_PNG = "assets/ball.png"; // ball widget design
    private static final String BRICK_PNG = "assets/brick.png"; // brick widget design
    private static final String HEART_PNG = "assets/heart.png"; // heart widget design
    private static final String PADDLE_PNG = "assets/paddle.png"; // paddle widget design
    private static final String TEMP_PADDLE_PNG = "assets/botGood.png"; // temporary paddle widget design
    private static final String BLOP_WAV = "assets/blop_cut_silenced.wav"; // collision sound
    private static final String PLAY_AGAIN = "You %s! Play again?"; // game over message
    private static final String BACKGROUND_JPEG = "assets/DARK_BG2_small.jpeg"; // background design
    private static final String PUCK_PNG = "assets/mockBall.png"; // puck widget design
    private static final int COLLISION_PUCKS = 1; // number of pucks used per brick strategy
    private static final int COLLISION_HEARTS = 1; // number of hearts used per brick strategy
    private static final int MAX_STRATEGIES = 3; // max strategies a brick can have
    private static final int TEMP_PADDLE_LIVES = 3; // number of collisions a temporary paddle can absorb
    private static final int FOLLOW_CAMERA_COLLISIONS = 4; // number of collisions camera follows ball for

    private WindowController windowController;
    private Counter livesCounter; // counts amount of lives left
    private Counter brickCounter; // counts amount of bricks left
    private Ball ball; // ball object
    private final Vector2 windowDimensions; // dimensions of game window
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Paddle paddle; // game paddle

    /**
     * Constructor of Bricker game
     * @param windowTitle String name of game
     * @param windowDimensions Vector2 dimensions of game window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * The method will be called once when a Bricker game is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @see ImageReader
     * @see SoundReader
     * @see UserInputListener
     * @see WindowController
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        livesCounter = new Counter(NUM_LIVES);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;

        // initialize game objects
        createBall();
        createPaddle();
        createBricks();
        createBoarders();
        createBackground();
        createLives();
        createNumericLife();
    }

    /**
     * Creates and deploys background image
     */
    private void createBackground() {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage(BACKGROUND_JPEG,false));
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates and deploys the borders of the game
     */
    private void createBoarders() {
        GameObject leftBoarder = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), BORDER_RENDER);
        GameObject rightBoarder = new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH,0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), BORDER_RENDER);
        GameObject topBoarder = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), BORDER_RENDER);

        gameObjects().addGameObject(leftBoarder);
        gameObjects().addGameObject(topBoarder);
        gameObjects().addGameObject(rightBoarder);
    }

    /**
     * Creates and deploys the number representation of lives left
     */
    private void createNumericLife() {
        float windowHeight = windowDimensions.y();

        new NumericLifeCounter(livesCounter,
                new Vector2(HEART_VECTOR.x()*NUM_LIVES+NUM_EXTRA_LIVES+1,windowHeight-35),
                NUMERIC_VECTOR, gameObjects());
    }

    /**
     * Creates and deploys heart object
     */
    private void createLives() {
        float windowHeight = windowDimensions.y();
        Renderable heartImage = imageReader.readImage(HEART_PNG, true);

        new GraphicLifeCounter(new Vector2(BORDER_WIDTH, windowHeight-35), HEART_VECTOR,
                livesCounter, heartImage, gameObjects(), livesCounter.value(), NUM_EXTRA_LIVES);
    }

    /**
     * Creates and deploys brick object
     */
    private void createBricks() {
        brickCounter = new Counter(BRICK_ROWS*BRICK_COLS); // rows of bricks multiplied by columns
        //total width minus two borders divided by columns
        int xBrickDimension = (int) (windowDimensions.x()-(BORDER_WIDTH*2))/BRICK_COLS;
        //total length minus a border divided by 2.5 times the amount of rows
        int yBrickDimension = (int) (windowDimensions.y()-BORDER_WIDTH)*2/(BRICK_ROWS*5);

        Vector2 brickDimensions = new Vector2(xBrickDimension, yBrickDimension);
        Renderable brickImage = imageReader.readImage(BRICK_PNG, true);
        GameObject tempPaddle = createTempPaddle();
        Camera followCamera = createFollowCamera();
        // initiates each brick
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                // factory that decides on the strategy for a brick
                BrickStrategy strategy = BrickStrategyFactory.brickStrategy(gameObjects(), createPucks(),
                        tempPaddle, this, followCamera, createHearts(), ball);

                Vector2 topLeftCorner = new Vector2(BORDER_WIDTH + col*brickDimensions.x(),
                        BORDER_WIDTH + row*brickDimensions.y());
                Brick brick = new Brick(topLeftCorner, brickDimensions, brickImage, strategy, brickCounter);
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * Creates hearts to be added to a brick strategy
     * @return an array of hearts to be used for a bricks strategy
     */
    private Heart [] createHearts() {
        Heart [] hearts = new Heart[COLLISION_HEARTS*MAX_STRATEGIES];
        Renderable heart_image = imageReader.readImage(HEART_PNG, true);
        for (int i=0; i<COLLISION_HEARTS*MAX_STRATEGIES; i++){
            hearts[i] = new Heart(Vector2.ZERO, HEART_VECTOR, heart_image, paddle, livesCounter,
                    gameObjects(),NUM_LIVES+NUM_EXTRA_LIVES, windowDimensions);
        }
        return hearts;
    }

    /**
     * @return FollowCamera initiated to follow the ball upon usage
     */
    private Camera createFollowCamera() {
        return new FollowCamera(ball, Vector2.ZERO, windowDimensions.mult(1.2f), windowDimensions,
                ball.getCollisionCounter(), FOLLOW_CAMERA_COLLISIONS, this);
    }

    /**
     * Creates and deploys paddle object
     */
    private void createPaddle() {
        Renderable paddleImage =
                imageReader.readImage(PADDLE_PNG, false);
        // creates object

        paddle = new Paddle(Vector2.ZERO, PADDLE_VECTOR,
                paddleImage, inputListener, windowDimensions, (int) BORDER_WIDTH);
        // assigns paddle location
        paddle.setCenter(new Vector2(windowDimensions.x()/2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(paddle);
    }

    /**
     * @return TempPaddle initiated for TEMP_PADDLE_LIVES amount of collisions
     */
    private GameObject createTempPaddle() {
        Renderable paddleImage =
                imageReader.readImage(TEMP_PADDLE_PNG, false);
        // creates object
        GameObject paddle = new TempPaddle(Vector2.ZERO, PADDLE_VECTOR, paddleImage, inputListener,
                windowDimensions, (int) BORDER_WIDTH, TEMP_PADDLE_LIVES, gameObjects());
        // assigns paddle location
        paddle.setCenter(windowDimensions.mult(0.5f));
        return paddle;
    }

    /**
     * Creates and deploys ball object
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_PNG, true);
        Sound collisionSound = soundReader.readSound(BLOP_WAV);
        // creates object
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIAMETER, BALL_DIAMETER),
                ballImage, collisionSound);
        // assigns object location
        setBall(ball);
        gameObjects().addGameObject(ball);
    }

    /**
     * @return an array of arrays of Pucks to be used for a brick's strategy
     */
    private Ball [][] createPucks() {
        Ball [][] pucks = new Ball[MAX_STRATEGIES][COLLISION_PUCKS];
        for (int i = 0; i<MAX_STRATEGIES; i++){
            pucks[i] = new Ball[COLLISION_PUCKS];
            for (int j = 0; j<COLLISION_PUCKS; j++) {
                pucks[i][j] = createPuck();
            }
        }
        return pucks;
    }

    /**
     * @return Puck object
     */
    private Ball createPuck() {
        float brickWidth = (windowDimensions.x()-(BORDER_WIDTH*2))/BRICK_COLS;
        Renderable ballImage = imageReader.readImage(PUCK_PNG, true);
        Sound collisionSound = soundReader.readSound(BLOP_WAV);
        // creates object
        Ball puck = new Puck(Vector2.ZERO, new Vector2(brickWidth/3, brickWidth/3),
                ballImage, collisionSound, gameObjects(), windowDimensions);
        // assigns object location
        setBall(puck);

        return puck;
    }

    /**
     * Places ball in specified location with starting speed
     */
    private void setBall(Ball ball) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;

        // randomly selects speed direction
        Random rand = new Random();
        if(rand.nextBoolean())
            ballVelX *= -1;
        if(rand.nextBoolean())
            ballVelY *= -1;

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /**
     * Updates the current state of the game
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // checks if user lost a life
        if (ball.getTopLeftCorner().y() > windowDimensions.y()) {
            livesCounter.decrement();
            setBall(ball);
        }
        // check if game is over
        if (gameOver()) {
            if (newGame()) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();

            }
        }
    }


    /**
     * @return true if user lost, else false
     */
    private boolean gameOver() {
        // checks if user has lives, checks if user selected w, checks if bricks are left
        return livesCounter.value() <= 0 ||
                inputListener.isKeyPressed(KeyEvent.VK_W) ||
                brickCounter.value() <= 0;
    }

    /**
     * @return true if the user wants another game, else false
     */
    private boolean newGame() {
        String status = "won";
        if (livesCounter.value() <= 0) {
            status = "lost";
        }
        return windowController.openYesNoDialog(String.format(PLAY_AGAIN, status));
    }

    /**
     * Runs Bricker game
     * @param args
     */
    public static void main(java.lang.String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
    }
}
