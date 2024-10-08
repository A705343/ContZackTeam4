import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.Set;

public class Player {
    private ImageView character;
    private double x;
    private double y;
    private final int frameWidth;
    private final int frameHeight;
    private final Set<KeyCode> pressedKeys;
    private final Checkerboard checkerboard;

    // Constructor to initialize player attributes
    public Player(String imageUrl, double startX, double startY, int frameWidth, int frameHeight, Set<KeyCode> pressedKeys, Checkerboard checkerboard) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.pressedKeys = pressedKeys;
        this.checkerboard = checkerboard;
        
        // Load player image and create ImageView
        Image playerImage = new Image("https://cdn.discordapp.com/attachments/1281076837652496478/1283901199627583572/jack1.png?ex=66e4adab&is=66e35c2b&hm=f518fbe49d61b2d21c18eda9e2b1f14d8bb39d794cf809a2a605d9ac1ed0b058&");
        this.character = new ImageView(playerImage);
        this.character.setFitWidth(frameWidth);
        this.character.setFitHeight(frameHeight);
        
        // Set starting position
        this.x = startX;
        this.y = startY;
        this.character.setX(x);
        this.character.setY(y);
    }
    
    

    // Getter for ImageView (used to add the player to the scene)
    public ImageView getImageView() {
        return character;
    }

    // Method to move the player based on the keys pressed
    public void move() {
        double speed = 5; // Speed of the character movement

        // Move up
        if (pressedKeys.contains(KeyCode.W)) {
            y -= speed;
        }
        // Move down
        if (pressedKeys.contains(KeyCode.S)) {
            y += speed;
        }
        // Move left
        if (pressedKeys.contains(KeyCode.A)) {
            x -= speed;
        }
        // Move right
        if (pressedKeys.contains(KeyCode.D)) {
            x += speed;
        }

        // Ensure the player stays within the borders of the checkerboard
        double minX = checkerboard.getBorderSize() * checkerboard.getCellSize();
        double maxX = minX + (checkerboard.getCheckerboardWidth() * checkerboard.getCellSize()) - frameWidth;
        double minY = checkerboard.getBorderSize() * checkerboard.getCellSize();
        double maxY = minY + (checkerboard.getCheckerboardHeight() * checkerboard.getCellSize()) - frameHeight;

        x = Math.max(minX, Math.min(x, maxX));
        y = Math.max(minY, Math.min(y, maxY));

        // Update the player's position
        character.setX(x);
        character.setY(y);
    }

    // Get player's X position
    public double getX() {
        return x;
    }

    // Get player's Y position
    public double getY() {
        return y;
    }
}
