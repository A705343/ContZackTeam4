import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Set;
import java.util.HashSet;

public class Level4 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private Arrow arrow;
    private Arrow secondArrow;  // New arrow in the lower-left corner
    private AnimationTimer timer;
    private final int startX;
    private final int startY;
    private Rectangle[] blackObstaclesCol2; // Array for black rectangles in column 2
    private Rectangle[] blackObstaclesCol3; // Array for black rectangles in column 3
    private double previousX;  // To hold the character's previous X position
    private double previousY;  // To hold the character's previous Y position
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;

    public Level4(Stage primaryStage, int startX, int startY) {
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
    }

    public void start() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");

        // Initialize the checkerboard for Level 4 (adjust size as needed)
        checkerboard = new Checkerboard(6, 9, 80, 1); // 6 columns, 9 rows, cell size 80, border size 1
        
        // Apply the red checkerboard style for Level 4
        checkerboard.setLevel4Checkerboard();

        // Initialize the canvas for drawing game objects
        Canvas canvas = new Canvas(
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Add the canvas to the pane
        pane.getChildren().add(canvas);
        
        // Initialize game board
initializeGameBoard(pane);

        // Load character image
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);

        // Set the character image size
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);


        // Set up the character start position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        // Add black rectangles for vertical obstacles in both column 2 and column 3
        addVerticalBlackObstacles(pane);

        // Set up character movement with collision detection
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);
        
        // Add the first arrow at column 1, row 1
int arrowX = 1; // Column 1
int arrowY = 1; // Row 1
int arrowSize = checkerboard.getCellSize(); // Set the arrow size to match the checkerboard cell size
arrow = new Arrow(
    "Assets/arrow.png",
    arrowX, arrowY, // Initial position
    arrowSize, arrowSize
);

// Set the first arrow's position based on the checkerboard's layout
arrow.setPosition(
    arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Account for border size
    arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize()  // Account for border size
);
pane.getChildren().add(arrow.getImageView());

// Add the second arrow in the bottom-left corner (column 1, last row)
int secondArrowX = 1; // Column 1
int secondArrowY = 8; // Increment the row index to move it down
int secondArrowSize = checkerboard.getCellSize();
secondArrow = new Arrow(
    "Assets/arrow.png",
    secondArrowX, secondArrowY, // Initial position
    secondArrowSize, secondArrowSize
);


 // Load elevator image and add it to the pane
        Image elevatorImage = new Image("Assets/Elevator.png");
        ImageView elevator = new ImageView(elevatorImage);
        elevator.setFitWidth(80); // Set the desired width for the elevator image
        elevator.setFitHeight(100); // Set the desired height for the elevator image
        elevator.setLayoutX(270); // Set the X position (adjust as needed)
        elevator.setLayoutY(500); // Set the Y position (adjust as needed)
        pane.getChildren().add(elevator); // Add the elevator to the pane


// Set the second arrow's position
secondArrow.setPosition(
    secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Account for border size
    secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize()  // Account for border size
);
pane.getChildren().add(secondArrow.getImageView());

       System.out.println("First Arrow Position: (" + 
    (arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize()) + ", " + 
    (arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize()) + ")");

System.out.println("Second Arrow Position: (" + 
    (secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize()) + ", " + 
    (secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize()) + ")");

        // Set up the scene
        Scene scene = new Scene(pane, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );

        // Add key press and release event handlers
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // AnimationTimer for smooth movement
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas

                // Store the character's previous position before moving
                previousX = character.getLayoutX();
                previousY = character.getLayoutY();

                // Move the character
                characterMover.moveCharacter();

                // Check for collisions
                if (checkForCollisions()) {
                    // If there's a collision, revert to the previous position
                    character.setLayoutX(previousX);
                    character.setLayoutY(previousY);
                    checkLevelCompletion();
                }

                // Check for level completion (arrow collision)
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
            onLevelComplete.run();
        }
    }
    
     public void setOnLevelComplete2(Runnable onLevelComplete2) {
        this.onLevelComplete2 = onLevelComplete2;
    }
    
    private void completeLevel2() {
        if (onLevelComplete2 != null) {
            onLevelComplete2.run();
        }
    }


    private void checkLevelCompletion() {
    // Check if the player collides with the arrow to go to next level
    if (arrow.checkCollision(character)) {
        System.out.println("Collision detected with arrow, moving to next level.");
        completeLevel();  // Call completeLevel to proceed to next level
        timer.stop();
    }
    // Check if the player collides with the second arrow to go to the previous level
    else if (secondArrow.checkCollision(character)) {
        System.out.println("Collision detected with second arrow, moving to previous level.");
        completeLevel2();  // Call completeLevel2 to go back to Level 3
        timer.stop();
    }
}

    private void initializeGameBoard(Pane parentPane) {
    // Calculate the total size of the checkerboard including any borders
    double boardWidth = checkerboard.getCheckerboardWidth() * checkerboard.getCellSize();
    double boardHeight = checkerboard.getCheckerboardHeight() * checkerboard.getCellSize();

    // Center the checkerboard in the parent pane by calculating the offsets
    double centerX = (parentPane.getWidth() - boardWidth) / 2;
    double centerY = (parentPane.getHeight() - boardHeight) / 2;

    // Ensure that the layout is updated whenever the parent pane is resized
    parentPane.widthProperty().addListener((obs, oldVal, newVal) -> {
        double newCenterX = (newVal.doubleValue() - boardWidth) / 2;
        checkerboard.getPane().setLayoutX(newCenterX);
    });

    parentPane.heightProperty().addListener((obs, oldVal, newVal) -> {
        double newCenterY = (newVal.doubleValue() - boardHeight) / 2;
        checkerboard.getPane().setLayoutY(newCenterY);
    });

    // Set the initial layout to center the checkerboard
    checkerboard.getPane().setLayoutX(centerX);
    checkerboard.getPane().setLayoutY(centerY);

    // Add the checkerboard to the parent pane
    parentPane.getChildren().add(checkerboard.getPane());
}


    private void setCharacterStartPosition(int x, int y) {
        character.setLayoutX(x * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        character.setLayoutY(y * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
    }

    private void addVerticalBlackObstacles(Pane pane) {
    // Create black rectangles for vertical gaps in column 4 and column 5
    int[] blockedColumns = {3, 4}; // Updated to columns 3 and 4
    int numRows = checkerboard.getCheckerboardHeight();
    blackObstaclesCol2 = new Rectangle[1]; // Only one obstacle for column 4
    blackObstaclesCol3 = new Rectangle[1]; // Only one obstacle for column 5

    // Black rectangle for column 4
    blackObstaclesCol2[0] = new Rectangle(
        blockedColumns[0] * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Column 4
        0 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Start at row 5
        checkerboard.getCellSize() * 1.03, // Width remains the same
        11 * checkerboard.getCellSize() // Height adjusted to cover from row 5 to bottom (you can modify this value)
    );
    blackObstaclesCol2[0].setFill(Color.BLACK);
    pane.getChildren().add(blackObstaclesCol2[0]);

    // Black rectangle for column 5
    blackObstaclesCol3[0] = new Rectangle(
        blockedColumns[1] * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Column 5
        0 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Start at row 5
        checkerboard.getCellSize() * 1.03, // Width remains the same
        11 * checkerboard.getCellSize() // Height adjusted to cover from row 5 to bottom (you can modify this value)
    );
    blackObstaclesCol3[0].setFill(Color.BLACK);
    pane.getChildren().add(blackObstaclesCol3[0]);
}



    // Method to check for collisions between character and black rectangles
    private boolean checkForCollisions() {
        double characterX = character.getLayoutX();
        double characterY = character.getLayoutY();
        double characterWidth = character.getFitWidth();
        double characterHeight = character.getFitHeight();

        // Check collisions for column 2
        for (Rectangle obstacle : blackObstaclesCol2) {
            if (obstacle != null && isCollision(characterX, characterY, characterWidth, characterHeight, obstacle)) {
                return true; // Collision detected
            }
        }

        // Check collisions for column 3
        for (Rectangle obstacle : blackObstaclesCol3) {
            if (obstacle != null && isCollision(characterX, characterY, characterWidth, characterHeight, obstacle)) {
                return true; // Collision detected
            }
        }

        return false; // No collisions
    }

    // Helper method to check if the character is colliding with an obstacle
    private boolean isCollision(double x, double y, double width, double height, Rectangle obstacle) {
        return x + width > obstacle.getX() && x < obstacle.getX() + obstacle.getWidth()
            && y + height > obstacle.getY() && y < obstacle.getY() + obstacle.getHeight();
    }
}