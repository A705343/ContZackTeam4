import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Door {
    private ImageView doorView;
    private boolean open; // to track whether the door is open or closed
    private Rectangle hitbox; // To detect collisions

    public Door(String imagePath) {
        Image doorImage = new Image(imagePath);
        this.doorView = new ImageView(doorImage);
        this.open = false; // Door is initially closed
        this.hitbox = new Rectangle(); // Create a hitbox for collision detection
        this.hitbox.setWidth(doorView.getFitWidth()); // Set the hitbox width
        this.hitbox.setHeight(doorView.getFitHeight()); // Set the hitbox height
        updateHitbox(); // Update the hitbox position
    }

    public ImageView getDoorView() {
        return doorView;
    }

    public void setPosition(double x, double y) {
        doorView.setLayoutX(x);
        doorView.setLayoutY(y);
        updateHitbox(); // Update hitbox position when the door is moved
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        doorView.setOpacity(0.5); // Example: change opacity to indicate it's open
    }

    public void close() {
        open = false;
        doorView.setOpacity(1.0); // Example: reset opacity to indicate it's closed
    }

    public void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    // Update the hitbox position based on the doorView's position
    private void updateHitbox() {
        hitbox.setX(doorView.getLayoutX());
        hitbox.setY(doorView.getLayoutY());
    }

    // Check for collision with the player
    public boolean checkCollision(ImageView player) {
        if (open) {
            return hitbox.getBoundsInLocal().intersects(player.getBoundsInParent());
        }
        return false; // No collision if the door is closed
    }
}
