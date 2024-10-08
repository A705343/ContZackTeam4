import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Arrow {
    private final ImageView arrowImageView;

    public Arrow(String imagePath, double x, double y, double width, double height) {
        Image arrowImage = new Image(imagePath);
        arrowImageView = new ImageView(arrowImage);
        arrowImageView.setX(x);
        arrowImageView.setY(y);
        arrowImageView.setFitWidth(width);
        arrowImageView.setFitHeight(height);
    }

    public ImageView getImageView() {
        return arrowImageView;
    }

    public void setPosition(double x, double y) {
        arrowImageView.setX(x);
        arrowImageView.setY(y);
    }

    public boolean checkCollision(ImageView character) {
        
        return arrowImageView.getBoundsInParent().intersects(character.getBoundsInParent());

        // Check if the character intersects with the arrow
        //boolean isColliding = arrowImageView.getBoundsInParent().intersects(character.getBoundsInParent());
        //System.out.println("Checking collision with arrow: " + isColliding);
        //return isColliding;
    }
}