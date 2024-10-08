import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Screwdriver {
    private ImageView imageView;
    private boolean collected;

    public Screwdriver(int x, int y) {
        // Load the screwdriver image from the assets folder
        Image image = new Image("Assets/screwdriver.png");  // Make sure the file path is correct
        imageView = new ImageView(image);
        imageView.setFitWidth(50);  // Set width of the screwdriver
        imageView.setFitHeight(50); // Set height of the screwdriver

        // Set the initial position of the screwdriver
        setPosition(x, y);

        collected = false;
    }

    // Method to set the position of the screwdriver
    public void setPosition(double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
    }

    // Check if the screwdriver is collected
    public boolean isCollected() {
        return collected;
    }

    // Collect the screwdriver
    public void collect() {
        collected = true;
        imageView.setVisible(false);  // Hide the screwdriver once collected
    }

    // Get the ImageView for displaying the screwdriver
    public ImageView getImageView() {
        return imageView;
    }
}
