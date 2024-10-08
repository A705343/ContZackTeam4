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
import java.util.ArrayList; // Add this import if you're using List
import java.util.List;  
import javafx.scene.shape.Circle;


public class Level6 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private Arrow arrow;
    private Arrow secondArrow;  // New arrow in the lower-left corner
    private Arrow thirdArrow;
    private AnimationTimer timer;
    private final int startX;
    private final int startY;
    private Rectangle[] blueObstaclesCol3; // Array for blue rectangles in column 3
    private double previousX;  // To hold the character's previous X position
    private double previousY;  // To hold the character's previous Y position
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;
    private Runnable onLevelComplete3;
    private Game game;
    private Pane pane;
    // Launchpads
    private Launchpad launchpad; // First launchpad
    private Launchpad secondLaunchpad; // Second launchpad
    // List to store all buttons
    private List<Button> buttons = new ArrayList<>();
    private Spikes spikes;
    private Pane gamePane;
     private List<Spikes> spikesList = new ArrayList<>();
      private List<Wall> walls = new ArrayList<>();

    public Level6(Stage primaryStage, int startX, int startY) {
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
         gamePane = new Pane();
        
        spikes = new Spikes(gamePane);
    }

    public void start() {
    
    
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");
        clearSpikesFromGame( pane);
        // Initialize the checkerboard for Level 6 (adjust size as needed)
        checkerboard = new Checkerboard(6, 9, 80, 1); // 6 columns, 9 rows, cell size 80, border size 1

        // Initialize the canvas for drawing game objects
        Canvas canvas = new Canvas(
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        spikes.clearSpikesFromGame(pane);
        // Add the canvas to the pane
        pane.getChildren().add(canvas);

        // Load character image
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);

        // Set the character image size
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);

        // Initialize game board
        initializeGameBoard(pane);

        

        // Add the first arrow at column 0, row 3
int arrowX = 0; // Column 0
int arrowY = 2; // Row 3 (-indexed for the square)
int arrowSize = 80; // Size of the arrow image
arrow = new Arrow(
    "Assets/arrow.png",
    arrowX, arrowY, // Initial position (0, 0)
    arrowSize, arrowSize
);


// Set the arrow's position based on the checkerboard's layout
arrow.setPosition(
    arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
    arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
);
//arrow.getImageView().setRotate(90); // Adjust rotation if necessary
pane.getChildren().add(arrow.getImageView());

// Add the second arrow at column 0, row 7
int secondArrowX = 0; // Column 0
int secondArrowY = 6; // Row 7 (0-indexed)
secondArrow = new Arrow(
    "Assets/arrow.png",
    secondArrowX, secondArrowY, // Initial position (0, 0)
    arrowSize, arrowSize
);
secondArrow.setPosition(
    secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
    secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
);
pane.getChildren().add(secondArrow.getImageView());

// Add the third arrow at column 4, row 0
int thirdArrowX = 5; // Column 4
int thirdArrowY = 0; // Row 0 (0-indexed)
thirdArrow = new Arrow(
    "Assets/arrow.png",
    thirdArrowX, thirdArrowY, // Initial position (0, 0)
    arrowSize, arrowSize
);
thirdArrow.setPosition(
    thirdArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
    thirdArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
);
thirdArrow.getImageView().setRotate(180);
pane.getChildren().add(thirdArrow.getImageView());



 // Create a single Spikes instance for the entire level
         Spikes spikesManager = new Spikes(pane);
         
         // Add blue spikes
         Spikes blueSpike1 = new Spikes(pane);
         blueSpike1.addSpikeToGame(pane, "blue", 162, 475, true, 75, 18);
         spikesList.add(blueSpike1);

        // Set up the character start position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        // Add blue rectangles for obstacles in column 3 (removing column 2)
        addBlueObstacles(pane);

        // Set up character movement with collision detection
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

        // First Launchpad at (0, 1)
        double launchpadStartX = 2 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadStartY = 2 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadTargetX = 5 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadTargetY = 3 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();

        launchpad = new Launchpad(launchpadStartX, launchpadStartY, launchpadTargetX, launchpadTargetY);
        pane.getChildren().add(launchpad.getImageView());

        // Second Launchpad at (5, 8)
        double secondLaunchpadStartX = 4 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double secondLaunchpadStartY = 6 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double secondLaunchpadTargetX = 3 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(); // Adjust target as needed
        double secondLaunchpadTargetY = 7 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();

        secondLaunchpad = new Launchpad(secondLaunchpadStartX, secondLaunchpadStartY, secondLaunchpadTargetX, secondLaunchpadTargetY);
        pane.getChildren().add(secondLaunchpad.getImageView());

        // Set up the scene
        Scene scene = new Scene(pane, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );

 int wallThickness = 10; // Thickness of the wall
        int cellSize = checkerboard.getCellSize();

        // Horizontal middle row (excluding gaps)
        walls.add(new Wall(1 * cellSize, 6 * cellSize, 1 * cellSize, wallThickness, Color.BROWN, pane)); // Left part
        walls.add(new Wall(3 * cellSize, 6 * cellSize, 1 * cellSize, wallThickness, Color.BROWN, pane)); // Before gap at (2,3)

               
        scene.setOnKeyPressed(event -> {
    pressedKeys.add(event.getCode());
     // Button dimensions
        double buttonWidth = 50; // Width of the button
        double buttonHeight = 50; // Height of the button

        // Initialize and add buttons to the pane
        addButtonsToPane(pane, buttonWidth, buttonHeight);
       
        


   
});

scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // AnimationTimer for smooth movement
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
                clearSpikesFromGame( pane);

                // Store the character's previous position before moving
                previousX = character.getLayoutX();
                previousY = character.getLayoutY();

                // Move the character
                characterMover.moveCharacter();
                checkButtonInteraction();
                checkSpikeCollisions();
                checkWallCollisions();

                // Check for collisions
                if (checkForCollisions()) {
                    // If there's a collision, revert to the previous position
                    character.setLayoutX(previousX);
                    character.setLayoutY(previousY);
                    checkLevelCompletion();
                }
                
                checkLaunchpadInteraction();

                // Check for level completion (arrow collision)
                checkLevelCompletion();
                
            }
        };
        timer.start();
        

        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    public void clearSpikesFromGame(Pane pane) {
    for (Spikes spike : spikesList) {
        pane.getChildren().remove(spike.getView());
    }
    spikesList.clear();  // Clears the spike list to remove all spikes
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
    
     public void setOnLevelComplete3(Runnable onLevelComplete3) {
        this.onLevelComplete3 = onLevelComplete3;
    }
    
    private void completeLevel3() {
        if (onLevelComplete3 != null) {
            onLevelComplete3.run();
        }
    }

    private void checkLevelCompletion() {
        // Check if the player collides with the arrow
        if (arrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to next level.");
            completeLevel();
            timer.stop();
        }
        else if (secondArrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to previous level.");
            completeLevel2();
            timer.stop();
        }
        else if (thirdArrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to previous level.");
            completeLevel3();
            timer.stop();
        }

    }

    private void initializeGameBoard(Pane pane) {
        checkerboard.getPane().setLayoutX(checkerboard.getBorderSize() * checkerboard.getCellSize());
        checkerboard.getPane().setLayoutY(checkerboard.getBorderSize() * checkerboard.getCellSize());
        pane.getChildren().add(checkerboard.getPane());
    }

    private void setCharacterStartPosition(int x, int y) {
        character.setLayoutX(x * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        character.setLayoutY(y * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
    }

    private void addBlueObstacles(Pane pane) {
        // Create blue rectangles for obstacles in column 3 only
        int blockedColumn = 3; // Blocked column
        int numRows = checkerboard.getCheckerboardHeight();
        blueObstaclesCol3 = new Rectangle[numRows];

        for (int row = 0; row < numRows; row++) {
            // Blue rectangle for column 3
            Rectangle blueSquareCol3 = new Rectangle(
                blockedColumn * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
                row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
                checkerboard.getCellSize(), checkerboard.getCellSize()
            );
            blueSquareCol3.setFill(Color.BLUE); // Set color to blue
            blueObstaclesCol3[row] = blueSquareCol3;
            pane.getChildren().add(blueSquareCol3);
        }
    }

    // Method to check for collisions between character and blue rectangles
    private boolean checkForCollisions() {
        double characterX = character.getLayoutX();
        double characterY = character.getLayoutY();
        double characterWidth = character.getFitWidth();
        double characterHeight = character.getFitHeight();

        // Check collisions for column 3 (blue obstacles)
        for (Rectangle obstacle : blueObstaclesCol3) {
            if (obstacle != null && isCollision(characterX, characterY, characterWidth, characterHeight, obstacle)) {
                return true; // Collision detected
            }
        }

        return false; // No collisions
    }

    // Helper method to check if the character has collided with an obstacle
    private boolean isCollision(double x1, double y1, double width1, double height1, Rectangle rect) {
        double x2 = rect.getX();
        double y2 = rect.getY();
        double width2 = rect.getWidth();
        double height2 = rect.getHeight();

        // Check if the rectangles overlap (collision detection)
        return x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2;
    }
    
    private void checkLaunchpadInteraction() {
        if (!launchpad.isActivated() && character.getBoundsInParent().intersects(launchpad.getImageView().getBoundsInParent())) {
            launchpad.activate();
            launchPlayerToTarget(launchpad); // Pass the activated launchpad
        }

        if (!secondLaunchpad.isActivated() && character.getBoundsInParent().intersects(secondLaunchpad.getImageView().getBoundsInParent())) {
            secondLaunchpad.activate();
            launchPlayerToTarget(secondLaunchpad); // Pass the activated launchpad
        }
    }

    private void launchPlayerToTarget(Launchpad launchpad) {
        double checkerboardX = launchpad.getTargetX() - checkerboard.getPane().getLayoutX();
        double checkerboardY = launchpad.getTargetY() - checkerboard.getPane().getLayoutY();

        character.setLayoutX(checkerboardX);
        character.setLayoutY(checkerboardY);
        
        character.toFront();
        System.out.println("Player landed at (" + checkerboardX + ", " + checkerboardY + ")");
    }
    
  private void addButtonsToPane(Pane pane, double buttonWidth, double buttonHeight) {
        // Define and add buttons to the pane
      

        Button blueButton = new Button(getButtonX(1), getButtonY(6), Color.BLUE, pane, spikes, "blue");
        pane.getChildren().add(blueButton.getShape());
        buttons.add(blueButton);
        
      Button orangeButton = new Button(getButtonX(5), getButtonY(3), Color.ORANGE, pane, spikes, "orange");
        pane.getChildren().add(orangeButton.getShape());
        buttons.add(orangeButton);

        
        
    }

    private double getButtonX(int col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }

    private double getButtonY(int row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }
  private void checkButtonInteraction() {
    if (!pressedKeys.contains(KeyCode.E)) {
        return;  // Only check interaction if 'E' key is pressed
    }

    for (Button button : buttons) {
        if (!button.isActivated() && character.getBoundsInParent().intersects(button.getShape().getBoundsInParent())) {
            button.activate();
        }
    }
}

 
     private void checkSpikeCollisions() {
    for (Spikes.Spike spike : spikes.getAllSpikes()) {
        if (spike.isSpikeUp()) {
            // Only block movement if spike is up and player collides with the spike
            if (character.getBoundsInParent().intersects(spike.getView().getBoundsInParent())) {
                //System.out.println("Collision detected with spike UP!");

                // Prevent the character from crossing the spike based on movement direction
                double characterX = character.getLayoutX();
                double characterY = character.getLayoutY();

                if (pressedKeys.contains(KeyCode.W)) { // Moving up
                    character.setLayoutY(characterY + 5); // Push the character down
                }
                if (pressedKeys.contains(KeyCode.S)) { // Moving down
                    character.setLayoutY(characterY - 5); // Push the character up
                }
                if (pressedKeys.contains(KeyCode.A)) { // Moving left
                    character.setLayoutX(characterX + 5); // Push the character right
                }
                if (pressedKeys.contains(KeyCode.D)) { // Moving right
                    character.setLayoutX(characterX - 5); // Push the character left
                }
            }
        } else {
            // If spike is down, allow passing and print debug
            if (character.getBoundsInParent().intersects(spike.getView().getBoundsInParent())) {
               // System.out.println("Spike is down, player should pass through.");
            }
        }
    }
}


  private void checkWallCollisions() {
        for (Wall wall : walls) {
            if (character.getBoundsInParent().intersects(wall.getWall().getBoundsInParent())) {
                double characterX = character.getLayoutX();
                double characterY = character.getLayoutY();

                // Prevent character from crossing the wall
                if (pressedKeys.contains(KeyCode.W)) {
                    character.setLayoutY(characterY + 5);
                }
                if (pressedKeys.contains(KeyCode.S)) {
                    character.setLayoutY(characterY - 5);
                }
                if (pressedKeys.contains(KeyCode.A)) {
                    character.setLayoutX(characterX + 5);
                }
                if (pressedKeys.contains(KeyCode.D)) {
                    character.setLayoutX(characterX - 5);
                }
            }
        }
    }






    
}