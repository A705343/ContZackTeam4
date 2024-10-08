import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.scene.shape.Rectangle;


public class CharacterMover {
    private final ImageView character;
    private final int frameWidth;
    private final int frameHeight;
    private final Set<KeyCode> pressedKeys;
    private final Checkerboard checkerboard;
    private List<Wall> walls = new ArrayList<>();
    private List<Spikes.Spike> spikes = new ArrayList<>(); // Use Spike class for spikes

    private double speed = 5.0; // Movement speed (pixels per frame)

    public CharacterMover(ImageView character, int frameWidth, int frameHeight, Set<KeyCode> pressedKeys, Checkerboard checkerboard) {
        this.character = character;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.pressedKeys = pressedKeys;
        this.checkerboard = checkerboard;
    }

    // Set the walls for collision detection
    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    // Set the spikes for collision detection
    public void setSpikes(List<Spikes.Spike> spikes) {
        this.spikes = spikes;
    }

    public void moveCharacter() {
        double xPos = character.getLayoutX();
        double yPos = character.getLayoutY();

        double deltaX = 0;
        double deltaY = 0;

        if (pressedKeys.contains(KeyCode.A)) { deltaX = -speed; }
        if (pressedKeys.contains(KeyCode.D)) { deltaX = speed; }
        if (pressedKeys.contains(KeyCode.W)) { deltaY = -speed; }
        if (pressedKeys.contains(KeyCode.S)) { deltaY = speed; }

        double newX = xPos + deltaX;
        double newY = yPos + deltaY;

        double cellSize = checkerboard.getCellSize();
        double borderSize = checkerboard.getBorderSize() * cellSize;

        double minX = borderSize;
        double minY = borderSize;
        double maxX = minX + (checkerboard.getCheckerboardWidth() * cellSize);
        double maxY = minY + (checkerboard.getCheckerboardHeight() * cellSize);

        if (newX < minX) newX = minX;
        if (newX > maxX - frameWidth) newX = maxX - frameWidth;
        if (newY < minY) newY = minY;
        if (newY > maxY - frameHeight) newY = maxY - frameHeight;

        int newRow = (int) ((newY - borderSize) / cellSize);
        int newCol = (int) ((newX - borderSize) / cellSize);

        if (!checkerboard.isCellBlocked(newRow, newCol) && !isCollidingWithWalls(newX, newY) && !isCollidingWithSpikes(newX, newY)) {
            character.setLayoutX(newX);
            character.setLayoutY(newY);
        } else {
            System.out.println("Movement blocked: (" + newRow + ", " + newCol + ")");
        }
    }

    // Collision detection with walls
    private boolean isCollidingWithWalls(double newX, double newY) {
        double playerWidth = character.getFitWidth();
        double playerHeight = character.getFitHeight();

        for (Wall wall : walls) {
            double wallX = wall.getImageView().getLayoutX();
            double wallY = wall.getImageView().getLayoutY();
            double wallWidth = wall.getImageView().getFitWidth();
            double wallHeight = wall.getImageView().getFitHeight();

            if (newX < wallX + wallWidth && newX + playerWidth > wallX &&
                newY < wallY + wallHeight && newY + playerHeight > wallY) {
                return true;
            }
        }
        return false;
    }

    // Collision detection with spikes
    private boolean isCollidingWithSpikes(double newX, double newY) {
        double playerWidth = character.getFitWidth();
        double playerHeight = character.getFitHeight();

        for (Spikes.Spike spike : spikes) {
            ImageView spikeView = spike.getView();
            Rectangle spikeBoundary = spike.getBoundary();
            
            if (spikeBoundary != null) {
                double spikeX = spikeBoundary.getX();
                double spikeY = spikeBoundary.getY();
                double spikeWidth = spikeBoundary.getWidth();
                double spikeHeight = spikeBoundary.getHeight();
                
                if (newX < spikeX + spikeWidth && newX + playerWidth > spikeX &&
                    newY < spikeY + spikeHeight && newY + playerHeight > spikeY) {
                    return true; // Collision with a spike
                }
            }
        }
        return false; // No collision with spikes
    }
}