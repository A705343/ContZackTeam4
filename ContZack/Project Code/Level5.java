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
import java.util.List;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Level5 {
    private ImageView character;
    private final int frameWidth = 50;
    private final int frameHeight = 50;
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
    private Rectangle rightSideObstacle; // For the right-side obstacle
    private Rectangle[] topRowObstacles; // Array for top row obstacles
    private Pane gamePane;
    private Pane pane;
    private Rectangle topObstacle;
    private Rectangle bottomObstacle;
    private Launchpad launchpad;
    private Launchpad secondLaunchpad;
    private Launchpad thirdLaunchpad;
    private Launchpad fourthLaunchpad;
    private Spikes spikes;
    private List<Spikes> yellowSpikes = new ArrayList<>();
    private List<Spikes> spikesList = new ArrayList<>();
    private int launchpadIndex = 0;
    // Class variable to track if the green button has been activated
    private boolean isGreenButtonActivated = false;
    private Button yellowEvertButton;
    private Button greenButton;

    private double previousX;  // To hold the character's previous X position
    private double previousY;  // To hold the character's previous Y position
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;
    private List<Wall> walls = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();

    public Level5(Stage primaryStage, int startX, int startY) {
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
        gamePane = new Pane();
        initializeCheckerboard();
        spikes = new Spikes(gamePane);
    }
    
    public Level5(Pane gamePane) {
      this.gamePane = gamePane;
      this.startX = 0;
      this.startY = 0;
      initializeCheckerboard();
    
    }
    
    private void initializeCheckerboard() {
      this.checkerboard = new Checkerboard(6,9,80,1);
      gamePane.getChildren().add(checkerboard.getPane());
    }    

    public void start() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");

        // Initialize the checkerboard for Level 4 (adjust size as needed)
        checkerboard = new Checkerboard(8, 9, 80, 1); // 6 columns, 9 rows, cell size 80, border size 1
        
        checkerboard.setLevel5Checkerboard();
        

        // Initialize the canvas for drawing game objects
        Canvas canvas = new Canvas(
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
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

        // Add the arrow
        int arrowX = 3; // Set column to 0 (top-left corner)
        int arrowY = 8; // Set row to 0 (top-left corner)
        int arrowSize = 80; // Size of the arrow image
        arrow = new Arrow(
            "Assets/arrow.png",
            arrowX, arrowY,
            arrowSize, arrowSize
        );
         
        // Set the arrow's position based on the checkerboard's layout
        arrow.getImageView().setLayoutX(arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        arrow.getImageView().setLayoutY(arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        
         
        // Add the arrow to the pane
        pane.getChildren().add(arrow.getImageView());
        arrow.getImageView().setRotate(270);
        
        // Add the second arrow in the bottom-left corner
        int secondArrowX = 0; // Column 0
        int secondArrowY = checkerboard.getCheckerboardHeight() - 1; // Last row
        secondArrow = new Arrow(
            "Assets/arrow.png",
            secondArrowX, secondArrowY,
            arrowSize, arrowSize
        );
        secondArrow.getImageView().setLayoutX(secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        secondArrow.getImageView().setLayoutY(secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize());
        pane.getChildren().add(secondArrow.getImageView());
        secondArrow.getImageView().setRotate(270);

        // Set up the character start position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);
        
       

        // Add black rectangles for vertical obstacles in column 2
        addVerticalBlackObstacles(pane);
        
        // Create a single Spikes instance for the entire level
        //Spikes spikesManager = new Spikes(gamePane);
        
        // Add yellow spikes
        spikes.addSpikeToGame(pane, "yellow_vert", 330, 246, false, 25, 80);
        spikes.addSpikeToGame(pane, "yellow_vert", 506, 246, false, 25, 80);
        
        spikes.addSpikeToGame(pane, "yellow_vert", 506, 399, true, 25, 105);
        spikes.addSpikeToGame(pane, "yellow_vert", 506, 499, true, 25, 105);
        spikes.addSpikeToGame(pane, "yellow_vert", 506, 599, true, 25, 105);
        spikes.addSpikeToGame(pane, "yellow_vert", 506, 699, true, 25, 105);
        
        
        // Button dimensions
        double buttonWidth = 50; // Width of the button
        double buttonHeight = 50; // Height of the button

        // Initialize and add buttons to the pane
        addButtonsToPane(pane, buttonWidth, buttonHeight);
        character.toFront();
        
        double launchpadWidth = 80;
        double launchpadHeight = 40;
        
        // Initialize and add the launchpad to the pane
        double launchpadStartX = 1.01 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadStartY = 2.03 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        //where the player will land
        double launchpadTargetX = 2 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadTargetY = 1 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        
        launchpad = new Launchpad(launchpadStartX, launchpadStartY, launchpadTargetX, launchpadTargetY);
        launchpad.getImageView().setRotate(270);
        
        pane.getChildren().add(launchpad.getImageView());
        
        // Initialize and add the second launchpad to the left of the first one
         double secondLaunchpadStartX = 2.02 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();  // Move 2 cells to the left
         double secondLaunchpadStartY = 2.03 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         // Where the player will land for the second launchpad
         double secondLaunchpadTargetX = (1) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         double secondLaunchpadTargetY = 2 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         
         secondLaunchpad = new Launchpad(secondLaunchpadStartX, secondLaunchpadStartY, secondLaunchpadTargetX, secondLaunchpadTargetY);
         // Rotate the launchpad 180 degrees
         secondLaunchpad.getImageView().setRotate(180);
         pane.getChildren().add(secondLaunchpad.getImageView());
         secondLaunchpad.getImageView().setRotate(270);
         
         // Initialize and add the second launchpad to the left of the first one
         double thirdLaunchpadStartX = 3.4 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();  // Move 2 cells to the left
         double thirdLaunchpadStartY = 2.03 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         // Where the player will land for the second launchpad
         double thirdLaunchpadTargetX = (7.8) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         double thirdLaunchpadTargetY = 2.6 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         
         thirdLaunchpad = new Launchpad(thirdLaunchpadStartX, thirdLaunchpadStartY, thirdLaunchpadTargetX, thirdLaunchpadTargetY);
         // Rotate the launchpad 180 degrees
         thirdLaunchpad.getImageView().setRotate(180);
         pane.getChildren().add(thirdLaunchpad.getImageView());
         thirdLaunchpad.getImageView().setRotate(270);
         
         // Initialize and add the second launchpad to the left of the first one
         double fourthLaunchpadStartX = 5.8 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();  // Move 2 cells to the left
         double fourthLaunchpadStartY = 2.03 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         // Where the player will land for the second launchpad
         double fourthLaunchpadTargetX = (7.4) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         double fourthLaunchpadTargetY = 5.1 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         
         fourthLaunchpad = new Launchpad(fourthLaunchpadStartX, fourthLaunchpadStartY, fourthLaunchpadTargetX, fourthLaunchpadTargetY);
         // Rotate the launchpad 180 degrees
         fourthLaunchpad.getImageView().setRotate(180);
         pane.getChildren().add(fourthLaunchpad.getImageView());
         fourthLaunchpad.getImageView().setRotate(270);
         
         character.toFront();
        
       
        // Set up the scene
        Scene scene = new Scene(pane, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );

        // Add key press and release event handlers
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        
        // Add key press and release event handlers
scene.setOnKeyPressed(event -> {
    KeyCode keyCode = event.getCode();
    pressedKeys.add(keyCode);

    if (keyCode == KeyCode.E) {
        checkButtonInteraction(); // Call method to check button interaction
    }
});
scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

// Set up character movement with collision detection
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);



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
            checkWallCollisions();
            checkLaunchpadInteraction();
            checkSpikeCollisions();
            checkButtonInteraction();
            // Removed checkButtonInteraction here, as it's already handled in key press
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
        // Check if the player collides with the arrow
        if (arrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to next level.");
            completeLevel();
            timer.stop();
        } else if (secondArrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to previous level.");
            completeLevel2();
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

private void addVerticalBlackObstacles(Pane pane) {
    // Create black rectangles for vertical gaps in column 3
    int blockedColumn3 = 3; // Column 3 (no change)
    int numRows = checkerboard.getCheckerboardHeight(); // Get total number of rows

    // Initialize blackObstacles array
    blackObstaclesCol2 = new Rectangle[1]; // Only one obstacle for column 3

    // Create the vertical obstacle
    blackObstaclesCol2[0] = new Rectangle(
        blockedColumn3 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // X position for column 3
        4 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Start from row 5
        checkerboard.getCellSize() * 1.03, // Width remains the same
        (numRows - 2) * checkerboard.getCellSize() // Adjusted height from row 5 to bottom
    );
    blackObstaclesCol2[0].setFill(Color.BLACK);
    pane.getChildren().add(blackObstaclesCol2[0]); // Add to pane

    // Create right side obstacle
    rightSideObstacle = new Rectangle(
        4 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Start at column 4
        4 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Start at row 5
        (checkerboard.getCheckerboardWidth() - 2) * checkerboard.getCellSize(), // Width remains the same
        checkerboard.getCellSize() * 1.03 // Height for thickness
    );
    rightSideObstacle.setFill(Color.BLACK);
    pane.getChildren().add(rightSideObstacle); // Add to pane

    // Adding top obstacle
    topObstacle = new Rectangle(
        3 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // X position at column 3
        1 * checkerboard.getCellSize() + checkerboard.getBorderSize(), // Y position at the top of row 1
        (checkerboard.getCheckerboardWidth() - 1) * checkerboard.getCellSize(), // Width remains the same
        2 * checkerboard.getCellSize() * 1 // Height adjusted to cover rows 1 and 2
    );
    topObstacle.setFill(Color.BLACK);
    pane.getChildren().add(topObstacle); // Add topObstacle to the pane

    // Adding bottom obstacle
    bottomObstacle = new Rectangle(
        7 * checkerboard.getCellSize() + checkerboard.getBorderSize() + 3.5, // X position at column 3
        (numRows - 1) * checkerboard.getCellSize() + checkerboard.getBorderSize() + 3.5, // Y position at the bottom of row 4
        (checkerboard.getCheckerboardWidth() - 1) * checkerboard.getCellSize(), // Width remains the same
        2 * checkerboard.getCellSize() * 1.05 // Height adjusted to cover 4 rows
    );
    bottomObstacle.setFill(Color.BLACK);
    pane.getChildren().add(bottomObstacle); // Add bottomObstacle to the pane
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

    // Check collision with the new right-side obstacle
    if (rightSideObstacle != null && isCollision(characterX, characterY, characterWidth, characterHeight, rightSideObstacle)) {
        return true; // Collision detected
    }
    
    // Check collision with the top obstacle
    if (topObstacle != null && isCollision(characterX, characterY, characterWidth, characterHeight, topObstacle)) {
        return true; // Collision detected
    }

    return false; // No collisions
}

// Helper method to check if the character is colliding with an obstacle
private boolean isCollision(double x, double y, double width, double height, Rectangle obstacle) {
    return x + width > obstacle.getX() && x < obstacle.getX() + obstacle.getWidth()
        && y + height > obstacle.getY() && y < obstacle.getY() + obstacle.getHeight();
}

private void checkWallCollisions() { // Jason's famous wall collision code (took only 3 days!)
      for (Wall wall : walls) {
         if (character.getBoundsInParent().intersects(wall.getWall().getBoundsInParent())) {
            double characterX = character.getLayoutX();
            double characterY = character.getLayoutY();
      
            // Prevent character from crossing the wall
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
      }
   }
   
   private void addButtonsToPane(Pane pane, double buttonWidth, double buttonHeight) {
    // Position for the silver button using double values
    double buttonX1 = getButtonX(1); // Example of more precise X position
    double buttonY1 = getButtonY(0); // Example of more precise Y position

    // Create and add the silver button
    Button silverButton = new Button(buttonX1, buttonY1, Color.SILVER);
    pane.getChildren().add(silverButton.getShape());
    buttons.add(silverButton);

    // Position for the yellow button (rectangular)
    double buttonX2 = getButtonX(4.4); // Example for the yellow button
    double buttonY2 = getButtonY(2);
    
    // Create a new yellow button that interacts with the spikes
    Button yellowButton = new Button(buttonX2, buttonY2, Color.YELLOW, pane, spikes, "yellow_vert"); // Pass pane, spikes, and type
    pane.getChildren().add(yellowButton.getShape());
    buttons.add(yellowButton);

    // Position for the green button
    double buttonX3 = getButtonX(6.8);
    double buttonY3 = getButtonY(2);
    Button greenButton = new Button(buttonX3, buttonY3, Color.GREEN);
    pane.getChildren().add(greenButton.getShape());
    buttons.add(greenButton);
    
    // Position for the yellow 'evert' button (circular)
    /*Button yellowEvertButton = new Button(getButtonX(0.4), getButtonY(3.4), Color.YELLOW, buttonRadius, pane, spikes, "yellow_vert");
    pane.getChildren().add(yellowEvertButton.getCircularShape());
    buttons.add(yellowEvertButton);*/
}






private double getButtonX(double col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }

    private double getButtonY(double row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }
    
    

private void checkButtonInteraction() {
    for (Button button : buttons) {
        // Check if the character is intersecting the button
        if (character.getBoundsInParent().intersects(button.getShape().getBoundsInParent())) {
            if (pressedKeys.contains(KeyCode.E)) { // Check if the 'E' key is pressed
                if (button.getShape().getFill() == Color.SILVER) {
                    activateLaunchpads(); // Activate launchpads if the silver button is pressed
                } else if (button.getShape().getFill() == Color.YELLOW) {
                    // Assuming the yellow evert button is set up to activate spikes directly
                    button.activate(); // Activate spikes directly
                }
            }
        }
    }
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

    // Check for the third launchpad
    if (!thirdLaunchpad.isActivated() && character.getBoundsInParent().intersects(thirdLaunchpad.getImageView().getBoundsInParent())) {
        thirdLaunchpad.activate();
        launchPlayerToTarget(thirdLaunchpad); // Pass the activated launchpad
    }

    // Check for the fourth launchpad
    if (!fourthLaunchpad.isActivated() && character.getBoundsInParent().intersects(fourthLaunchpad.getImageView().getBoundsInParent())) {
        fourthLaunchpad.activate();
        launchPlayerToTarget(fourthLaunchpad); // Pass the activated launchpad
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

private void activateLaunchpads() {
    // Create a PauseTransition for the first launchpad (no delay)
    PauseTransition firstPause = new PauseTransition(Duration.seconds(0)); // No delay for the first launchpad
    firstPause.setOnFinished(event -> {
        launchpad.activate(); // Activate the first launchpad
        
        // Play the second launchpad activation after a 1-second delay
        PauseTransition secondPause = new PauseTransition(Duration.seconds(1)); // 1-second delay for the second launchpad
        secondPause.setOnFinished(event2 -> {
            secondLaunchpad.activate(); // Activate the second launchpad
            
            // Play the fourth launchpad activation after a 1-second delay
            PauseTransition fourthPause = new PauseTransition(Duration.seconds(1)); // 1-second delay for the fourth launchpad
            fourthPause.setOnFinished(event3 -> {
                fourthLaunchpad.activate(); // Activate the fourth launchpad
                
                // Play the third launchpad activation after a 1-second delay
                PauseTransition thirdPause = new PauseTransition(Duration.seconds(1)); // 1-second delay for the third launchpad
                thirdPause.setOnFinished(event4 -> {
                    thirdLaunchpad.activate(); // Activate the third launchpad
                });
                thirdPause.play(); // Start the third launchpad activation
            });
            fourthPause.play(); // Start the fourth launchpad activation
        });
        secondPause.play(); // Start the second launchpad activation
    });
    firstPause.play(); // Start the first launchpad activation
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


}