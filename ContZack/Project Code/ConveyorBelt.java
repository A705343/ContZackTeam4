import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConveyorBelt {
    private ImageView beltView; // ImageView for the conveyor belt
    private double speed;
    private String direction;

    public ConveyorBelt(double speed, String direction) {
        this.speed = speed;
        this.direction = direction;
        // Load the conveyor belt image
        Image beltImage = new Image("Assets/Conveyor.png"); // Adjust the path as necessary
        beltView = new ImageView(beltImage);
        beltView.setFitWidth(100); // Set width as needed
        beltView.setFitHeight(100); // Set height as needed
    }

    public ImageView getBeltView() {
        return beltView;
    }

    public void moveCharacter(ImageView character) {
        // Check for collision with the conveyor belt
        if (character.getBoundsInParent().intersects(beltView.getBoundsInParent())) {
            // Implement logic to move the character based on the conveyor belt's direction and speed
            if (direction.equals("UP")) {
                character.setLayoutY(character.getLayoutY() - speed);
            } else if (direction.equals("DOWN")) {
                character.setLayoutY(character.getLayoutY() + speed);
            }
            else if (direction.equals("RIGHT")) {
                character.setLayoutX(character.getLayoutX() + speed);
            }
            else if (direction.equals("LEFT")) {
                character.setLayoutX(character.getLayoutX() - speed);
            }

            // Add other directions if necessary
        }
    }

    // Define a toggle method to change direction
    public void toggle() {
        // Toggle between "UP" and "DOWN" for example
        if (direction.equals("UP")) {
            direction = "DOWN";
        } else if (direction.equals("DOWN")) {
            direction = "UP";
        }
        else if (direction.equals("RIGHT")) {
            direction = "LEFT";
        }
        else if (direction.equals("LEFT")) {
            direction = "RIGHT";
        }

    }
}
