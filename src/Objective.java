import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.io.File;

abstract public class Objective {
    // Instance variables
    // (x,y) is the position of the center of the ball.
    final private double x, y;
    private ImageView image;
    private AudioClip teleportSound;
    /**
     * Creates a new objective at a given x and y location
     * @param x top left corner x offset
     * @param y top left corner y offset
     */
    public Objective (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the objective's display image given a relative path
     * @param path relative path to image
     */
    protected void setImage(String path) {
        image = new ImageView(getClass().getClassLoader().getResource(path).toString());
        image.setLayoutX(x);
        image.setLayoutY(y);
    }

    /**
     * Returns the underlying ImageView
     * @return the underlying ImageView
     */
    public ImageView getImage () {
        return image;
    }

    /**
     * Sets the objective's teleport sound given a relative path
     * @param path relative path to teleport sound file
     */
    protected void setTeleportSound(String path) {
        teleportSound = new AudioClip(getClass().getClassLoader().getResource(path).toString());
    }

    /**
     * Plays the teleport sound file.
     */
    public void teleport() {
        if(teleportSound != null) {
            teleportSound.play();
        }
    }

    /**
     * Calculates bounce if a collision is detected with an objective piece
     * @param ball The ball to check
     * @param ballBounds The ballBounds, localized to the Objective's local
     * @return true if collision occurred
     */
    public boolean testObjectiveCollide(Ball ball, Bounds ballBounds) {
        Bounds imageBounds = getImage().getBoundsInLocal();
        if(imageBounds.intersects(ballBounds)) {

            Bounds imageN = new BoundingBox(imageBounds.getMinX(), imageBounds.getMinY(),
                    imageBounds.getWidth(), imageBounds.getHeight() / 4);
            Bounds imageS = new BoundingBox(imageBounds.getMinX(), imageBounds.getMaxY() - imageBounds.getHeight() / 4,
                    imageBounds.getWidth(), imageBounds.getHeight() / 4);
            Bounds imageW = new BoundingBox(imageBounds.getMinX(), imageBounds.getMinY(),
                    imageBounds.getWidth()/4, imageBounds.getHeight());
            Bounds imageE = new BoundingBox(imageBounds.getMaxX() - imageBounds.getWidth() / 4, imageBounds.getMinY(),
                    imageBounds.getWidth() / 4, imageBounds.getHeight());

            if(imageN.intersects(ballBounds))
                ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.NEGATIVE);
            else if(imageE.intersects(ballBounds))
                ball.triggerBounce(BounceDirection.POSITIVE, BounceDirection.NOCHANGE);
            else if(imageS.intersects(ballBounds))
                ball.triggerBounce(BounceDirection.NOCHANGE, BounceDirection.POSITIVE);
            else if(imageW.intersects(ballBounds))
                ball.triggerBounce(BounceDirection.NEGATIVE, BounceDirection.NOCHANGE);
            return true;
        }
        return false;
    }

}
