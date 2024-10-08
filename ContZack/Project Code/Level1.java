import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.input.*;

public class Level1 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private Runnable onLevelComplete;
    private Arrow arrow;
    private AnimationTimer timer;
    private Main main; // Reference to the main class
    private final int startX;
    private final int startY;

    public Level1(Main main, Stage primaryStage, int startX, int startY) {
        this.main = main; // Store reference to the main class
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) { // Pause on ESC key press
            main.pauseGame(); // Call the pause method in main
        }
    }

    public void start() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");

        // Initialize the checkerboard for Level 1 (4x6)
        checkerboard = new Checkerboard(4, 6, 100, 1); // 4 columns, 6 rows, cell size 100, border size 1

        // Load character image
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);

        // Set the character image size
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);

        // Initialize game board
        initializeGameBoard(pane);

        // Create and add the arrow
        int arrowX = 0; // Column (0-indexed)
        int arrowY = 2; // Row (0-indexed)
        int arrowSize = 100;
        arrow = new Arrow(
            "Assets/arrow.png",
            0, 0, // Initial position
            arrowSize, arrowSize
        );
        pane.getChildren().add(arrow.getImageView());

        // Move the arrow to the desired position
        moveArrowToBlock(arrowX, arrowY);

        // Rotate the arrow by 90 degrees
        arrow.getImageView().setRotate(90);

        // Load the jukebox image
        Image jukeboxImage = new Image("Assets/jukebox.png");

        // Create an ImageView for the jukebox
        ImageView jukeboxView = new ImageView(jukeboxImage);

        // Set the size of the jukebox image (adjust as needed)
        jukeboxView.setFitWidth(80);  // Assuming frameWidth = 50
        jukeboxView.setFitHeight(80);  // Assuming frameHeight = 50

        // Calculate the position for the coordinates (4,4)
        double xPos = 3.1 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double yPos = 3.1 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();

        // Set the position of the jukebox
        jukeboxView.setLayoutX(xPos);
        jukeboxView.setLayoutY(yPos);

        // Add the jukebox to the pane
        pane.getChildren().add(jukeboxView);

        // Add character to pane and set its starting position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        // Set up character movement
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

        Scene scene = new Scene(pane, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );

        // Add key press and release event handlers
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            handleKeyPress(event); // Handle key press for pausing
        });
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // Use AnimationTimer for smooth movement and animation
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                characterMover.moveCharacter();
                checkLevelCompletion();
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setOnLevelComplete(Runnable onLevelComplete) {
        this.onLevelComplete = onLevelComplete;
    }

    private void completeLevel() {
        if (onLevelComplete != null) {
            onLevelComplete.run(); // Trigger the transition to Level 2
        }
    }

    private void checkLevelCompletion() {
        // Check if the player collides with the arrow
        if (arrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to next level.");
            completeLevel();
            timer.stop();
        }
    }

    private void initializeGameBoard(Pane pane) {
        double checkerboardWidth = checkerboard.getCheckerboardWidth() * checkerboard.getCellSize();
        double checkerboardHeight = checkerboard.getCheckerboardHeight() * checkerboard.getCellSize();
        double borderSize = checkerboard.getBorderSize() * checkerboard.getCellSize();

        checkerboard.getPane().setLayoutX(borderSize);
        checkerboard.getPane().setLayoutY(borderSize);
        pane.getChildren().add(checkerboard.getPane());
    }

    private void moveArrowToBlock(int row, int col) {
        double x = col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double y = row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        arrow.setPosition(x, y);
        System.out.println("Arrow position set to: (" + x + ", " + y + ")");
    }

    private void setCharacterStartPosition(int x, int y) {
        double xPos = x * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double yPos = y * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();

        character.setLayoutX(xPos);
        character.setLayoutY(yPos);
    }
}
