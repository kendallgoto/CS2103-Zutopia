import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;

import java.util.*;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;

	/**
	 * Max number of lives at start of game.
	 */
	public static final int MAX_LIVES = 5;

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	final private ArrayList<Objective> targets = new ArrayList<>(16);

	final private ArrayList<Class<? extends Objective>> possibleTargets = new ArrayList<>();
	private int lives = MAX_LIVES;
	final private AudioClip loseSound = new AudioClip(getClass().getClassLoader().getResource("shatter.wav").toString());
	final private AudioClip winSound = new AudioClip(getClass().getClassLoader().getResource("chaching.wav").toString());

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");
		possibleTargets.add(DuckObjective.class);
		possibleTargets.add(HorseObjective.class);
		possibleTargets.add(GoatObjective.class);

		restartGame(GameState.NEW);
	}

	/**
	 * Returns the name of the window
	 * @return window name
	 */
	public String getName () {
		return "Zutopia";
	}

	/**
	 * Returns this pane
	 * @return this pane
	 */
	public Pane getPane () {
		return this;
	}

	/**
	 * Begins / Restarts the game board, customizing the on-screen label based on state
	 * @param state Current Game State label
	 */
	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...
		targets.clear();
		Random rng = new Random();
		//for loops divided for clarity: this is our 4x4 grid of animals
		for(int y = 0; y < 4; y++) {
			int yPlacement = (y * 60) + 30;
			for(int x = 0; x < 4; x++) {
				int xPlacement = (x * (WIDTH / 4)) + 30; //automatically split x placement across width of screen
				generateObjective(xPlacement, yPlacement, rng);
			}
		}

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getTopRectangle());  // Add the paddle to the game board
		getChildren().add(paddle.getBottomRectangle());

		lives = MAX_LIVES; //reset lives to max

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		setOnMouseClicked(e -> {
			if(state != GameState.ACTIVE) {
				GameImpl.this.setOnMouseClicked(null); // stop listening for click
				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});
		setOnMouseMoved(e -> paddle.moveTo(e.getX(), e.getY()));
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		ball.updatePosition(deltaNanoTime);
		Bounds ballParent = ball.getCircle().getBoundsInParent();
		Bounds ballScene = this.localToScene(ballParent);

		//detect bounce on top of paddle vs bottom of paddle
		Bounds sceneBounds_top = paddle.getTopRectangle().sceneToLocal(ballScene);
		Bounds sceneBounds_bottom = paddle.getBottomRectangle().sceneToLocal(ballScene);
		if(paddle.getTopRectangle().intersects(sceneBounds_top)) {
			ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.NEGATIVE);
		} else if(paddle.getBottomRectangle().intersects(sceneBounds_bottom)) {
			ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.POSITIVE);
		}

		//test collision with each objective
		Iterator<Objective> iterator = targets.iterator();
		while(iterator.hasNext()) {
			Objective t = iterator.next();
			ImageView image = t.getImage();
			Bounds sceneBounds = image.sceneToLocal(ballScene);
			if(t.testObjectiveCollide(ball, sceneBounds)) {
				t.teleport();
				getChildren().remove(image);
				iterator.remove();
				ball.increaseSpeed(1.1, 1.1);
			}
		}
		if(targets.size() <= 0) {
			winSound.play();
			return GameState.WON;
		}

		//test screen boundaries...
		Bounds sceneBoundaries = this.getLayoutBounds();
		if(ballScene.getMinX() <= sceneBoundaries.getMinX()) //L Wall
			ball.triggerBounce(BounceDirection.POSITIVE, BounceDirection.NOCHANGE);
		else if(ballScene.getMaxX() >= sceneBoundaries.getMaxX()) // R Wall
			ball.triggerBounce(BounceDirection.NEGATIVE, BounceDirection.NOCHANGE);
		else if(ballScene.getMinY() <= sceneBoundaries.getMinY()) // Top Wall
			ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.POSITIVE);
		else if(ballScene.getMaxY() >= sceneBoundaries.getMaxY()) { // Bottom Wall
			ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.NEGATIVE);
			//check lives
			lives--;
			if(lives <= 0) {
				loseSound.play();
				return GameState.LOST;
			}
		}
		return GameState.ACTIVE;
	}

	/**
	 * Creates a random objective subclass with top corner at (x, y), given a random number generator
	 * @param x Top left corner X position
	 * @param y Top right corner Y position
	 * @param rng A random number generator to produce a new random objective with.
	 */
	private void generateObjective(int x, int y, Random rng) {
		//Get a random objective class from our class list, possibleTargets
		Class<? extends Objective> randomObjective = possibleTargets.get(rng.nextInt(possibleTargets.size()));
		try {
			//Given a random class, get its first constructor + construct it with (x, y) initial
			Objective random = (Objective) randomObjective.getConstructors()[0].newInstance(x, y);
			targets.add(random);
			getChildren().add(random.getImage());
		}
		catch(Exception e) { // will never throw exception
		}
	}

}
