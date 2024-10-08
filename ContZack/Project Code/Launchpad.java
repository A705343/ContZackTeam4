import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Launchpad {
    private ImageView imageView;    // Represents the visual launchpad
    private Image defaultImage;     // Default launchpad image
    private Image activatedImage;   // Launchpad image after activation
    private double targetX;         // X coordinate of the landing zone
    private double targetY;         // Y coordinate of the landing zone
    private boolean activated;      // Tracks if the launchpad has been used

    public Launchpad(double startX, double startY, double targetX, double targetY) {
        // Load the launchpad images
        defaultImage = new Image("Assets/launchpadDown.png");
        activatedImage = new Image("Assets/launchpadUp.png");

        imageView = new ImageView(defaultImage);
        imageView.setFitWidth(80);  // Set the width of the launchpad
        imageView.setFitHeight(80); // Set the height of the launchpad

        // Set the position of the launchpad
        imageView.setX(startX);
        imageView.setY(startY);

        // Set the target landing zone
        this.targetX = targetX;
        this.targetY = targetY;

        activated = false;
    }

    // Get the ImageView for displaying the launchpad
    public ImageView getImageView() {
        return imageView;
    }

    // Get the landing zone coordinates
    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    // Activate the launchpad (player lands at the target)
    public void activate() {
        if (!activated) {
            activated = true;
            imageView.setImage(activatedImage);  // Change the image to the activated one
            System.out.println("Launchpad activated! Player launched to (" + targetX + ", " + targetY + ")");
        }
    }

    // Check if the launchpad is already activated
    public boolean isActivated() {
        return activated;
    }
}