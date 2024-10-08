import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DoorSwitch {
    private ImageView switchView;
    private Door door;  // Reference to a Door object
    private boolean isActivated;
    private boolean toggleCooldown; // Flag to prevent continuous toggling
    private final Duration toggleDelay = Duration.seconds(2); // 2-second delay between toggles

    public DoorSwitch(Door door, String imagePath) {
        this.door = door;  // Assign Door object
        this.isActivated = false;
        this.toggleCooldown = false; // Start with no cooldown

        Image switchImage = new Image(imagePath); // Load the switch image
        switchView = new ImageView(switchImage);
        switchView.setFitWidth(100); // Adjust as necessary
        switchView.setFitHeight(100); // Adjust as necessary
    }

    public ImageView getSwitchView() {
        return switchView;
    }

    public void activate() {
        if (!toggleCooldown) {
            isActivated = !isActivated; // Toggle activation state
            startCooldown();  // Start cooldown
            door.toggle();  // Door toggle logic
        }
    }

    // Method to start cooldown between toggles
    private void startCooldown() {
        toggleCooldown = true; // Prevent further toggles

        PauseTransition pause = new PauseTransition(toggleDelay);
        pause.setOnFinished(event -> toggleCooldown = false); // Reset cooldown after delay
        pause.play();
    }

    // Check if character collides with the switch
    public boolean checkCollision(ImageView character) {
        return character.getBoundsInParent().intersects(switchView.getBoundsInParent());
    }
}
