import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	final private Circle circle;

	final private AudioClip bounceSound = new AudioClip(getClass().getClassLoader().getResource("boing.wav").toString());

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

	/**
	 * Bounces the ball in a specific direction in X and Y.
	 * @param bounceX a BounceDirection that modifies X velocity
	 * @param bounceY a BounceDirection that modifies Y velocity
	 */
	public void triggerBounce(BounceDirection bounceX, BounceDirection bounceY) {
		vx = applyBounceDirection(vx, bounceX);
		vy = applyBounceDirection(vy, bounceY);
		bounceSound.play();
	}

	/**
	 * Given a velocity and bounce direction, apply the bounce to the velocity and return it
	 * @param delta initial velocity
	 * @param modifier bounce direction
	 * @return result of applying bounce direction
	 */
	public double applyBounceDirection(double delta, BounceDirection modifier) {
		double abs_delta = Math.abs(delta);
		switch(modifier) {
			case NEGATIVE:
				return abs_delta * -1;
			case NOCHANGE:
				return delta;
			case POSITIVE:
				return abs_delta;
			case INVERSE:
				return delta * -1;
		}
		return 0; //should never run with valid input
	}

	/**
	 * Increase speed on X and Y given a multiplier.
	 * @param multiplierX multiplier for speed on X direction
	 * @param multiplierY multiplier for speed on Y direction
	 */
	public void increaseSpeed(double multiplierX, double multiplierY) {
		vx *= multiplierX;
		vy *= multiplierY;
	}
}
