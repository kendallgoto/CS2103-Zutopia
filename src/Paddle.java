import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Paddle {
	// Constants
	/**
	 * The width of the paddle.
	 */
	public static final int PADDLE_WIDTH = 100;
	/**
	 * The height of the paddle.
	 */
	public static final int PADDLE_HEIGHT = 5;
	/**
	 * The initial position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double INITIAL_Y_LOCATION_FRAC = 0.8;
	/**
	 * The minimum position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double MIN_Y_LOCATION_FRAC = 0.7;
	/**
	 * The maximum position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double MAX_Y_LOCATION_FRAC = 0.9;

	// Instance variables
	private Rectangle topRectangle;
	private Rectangle bottomRectangle;

	/**
	 * Constructs a new Paddle whose vertical center is at INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT.
	 */
	public Paddle () {
		final double x = PADDLE_WIDTH/2;
		final double y = INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT;

		topRectangle = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT/2);
		topRectangle.setLayoutX(x-PADDLE_WIDTH/2);
		topRectangle.setLayoutY(y-PADDLE_HEIGHT/2);
		topRectangle.setStroke(Color.GREEN);
		topRectangle.setFill(Color.GREEN);
		//We split the paddle into a top rectangle and bottom rectangle for bouncing off top/bottom.
		bottomRectangle = new Rectangle(0, PADDLE_HEIGHT/2, PADDLE_WIDTH, PADDLE_HEIGHT/2);
		bottomRectangle.setLayoutX(x-PADDLE_WIDTH/2);
		bottomRectangle.setLayoutY(y-PADDLE_HEIGHT/2);
		bottomRectangle.setStroke(Color.GREEN);
		bottomRectangle.setFill(Color.GREEN);
	}

	/**
	 * @return the Rectangle object that represents the top of the paddle on the game board.
	 */
	public Rectangle getTopRectangle () {
		return topRectangle;
	}

	/**
	 * @return the Rectangle object that represents the top of the paddle on the game board.
	 */
	public Rectangle getBottomRectangle () {
		return bottomRectangle;
	}

	/**
	 * Moves the paddle so that its center is at (newX, newY), subject to
	 * the horizontal constraint that the paddle must always be completely visible
	 * and the vertical constraint that its y coordiante must be between MIN_Y_LOCATION_FRAC
	 * and MAX_Y_LOCATION_FRAC times the game height.
	 * @param newX the newX position to move the center of the paddle.
	 * @param newY the newX position to move the center of the paddle.
	 */
	public void moveTo (double newX, double newY) {
		if (newX < PADDLE_WIDTH/2) {
			newX = PADDLE_WIDTH/2;
		} else if (newX > GameImpl.WIDTH - PADDLE_WIDTH/2) {
			newX = GameImpl.WIDTH - PADDLE_WIDTH/2;
		}

		if (newY < MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		} else if (newY > MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		}

		topRectangle.setTranslateX(newX - (topRectangle.getLayoutX() + PADDLE_WIDTH/2));
		topRectangle.setTranslateY(newY - (topRectangle.getLayoutY() + PADDLE_HEIGHT/2));
		bottomRectangle.setTranslateX(newX - (bottomRectangle.getLayoutX() + PADDLE_WIDTH/2));
		bottomRectangle.setTranslateY(newY - (bottomRectangle.getLayoutY() + PADDLE_HEIGHT/2));
	}
}
