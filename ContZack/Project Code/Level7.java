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
import javafx.scene.input.*;


public class Level7 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private Arrow arrow;
    private Arrow secondArrow;
    private Arrow thirdArrow;
    private Arrow fourthArrow;
    private AnimationTimer timer;
    private final int startX;
    private final int startY;
    private Rectangle[] wallObstacles;
    private double previousX;
    private double previousY;
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;
     private Runnable onLevelComplete3;
    private Runnable onLevelComplete4;
    private Spikes spikes;
    private Pane gamePane;
    
    private List<Button> buttons = new ArrayList<>();
    private List<Spikes> spikesList = new ArrayList<>();

    public Level7(Stage primaryStage, int startX, int startY) {
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
        // Initialize the checkerboard for Level 7
        checkerboard = new Checkerboard(6, 9, 80, 1); // Same grid size as Level 6
        spikes.clearSpikesFromGame(pane);

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
        character.setFitWidth(21);
        character.setFitHeight(35);

        // Initialize game board
        initializeGameBoard(pane);

        // Add the first arrow at column 0, row 3
int arrowX = 1; // Column 0
int arrowY = 0; // Row 3 (0-indexed for the square)
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
arrow.getImageView().setRotate(90);
pane.getChildren().add(arrow.getImageView());

// Add the second arrow at column 0, row 7
int secondArrowX = 4; // Column 0
int secondArrowY = 0; // Row 7 (0-indexed)
secondArrow = new Arrow(
    "Assets/arrow.png",
    secondArrowX, secondArrowY, // Initial position (0, 0)
    arrowSize, arrowSize
);
secondArrow.setPosition(
    secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
    secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
);
secondArrow.getImageView().setRotate(90);
pane.getChildren().add(secondArrow.getImageView());

// Add the third arrow at column 4, row 0
int thirdArrowX = 4; // Column 4
int thirdArrowY = 8; // Row 0 (0-indexed)
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

 // Add the first arrow at column 0, row 3
int fourthArrowX = 5; // Column 0
int fourthArrowY = 4; // Row 3 (0-indexed for the square)
int fourthArrowSize = 80; // Size of the arrow image
fourthArrow = new Arrow(
    "Assets/arrow.png",
    fourthArrowX, fourthArrowY, // Initial position (0, 0)
    fourthArrowSize, fourthArrowSize
);
// Set the arrow's position based on the checkerboard's layout
fourthArrow.setPosition(
    fourthArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
    fourthArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
);
//arrow.getImageView().setRotate(90); // Adjust rotation if necessary
fourthArrow.getImageView().setRotate(90);
pane.getChildren().add(fourthArrow.getImageView());


 // Create a single Spikes instance for the entire level
         Spikes spikesManager = new Spikes(pane);
         
         // Add blue spikes
         Spikes blueSpike1 = new Spikes(pane);
         blueSpike1.addSpikeToGame(pane, "blue", 162, 442, true, 75, 18);
         spikesList.add(blueSpike1);
         
       
         
       
         
         // Add purple spike
         Spikes purpleSpike = new Spikes(pane);
         purpleSpike.addSpikeToGame(pane, "purple", 400, 325, true, 18, 75);
         spikesList.add(purpleSpike);  
         
         // Add yellow spikes
         Spikes yellowSpike1 = new Spikes(pane);
         yellowSpike1.addSpikeToGame(pane, "yellow", 325, 300, true, 75, 18);
         spikesList.add(yellowSpike1);  
         
     

 // Button dimensions
        double buttonWidth = 50; // Width of the button
        double buttonHeight = 50; // Height of the button

        // Initialize and add buttons to the pane
        addButtonsToPane(pane, buttonWidth, buttonHeight);
        // Set up the scene
        Scene scene = new Scene(pane, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        ); 
        
 // Add key press and release event handlers
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());

            if (event.getCode() == KeyCode.E) {  // Check for 'E' key press
               checkButtonInteraction();        // Call method to check button interaction
            }
        });
         scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));





        // Set up the character start position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        // Add wall obstacles in the center of the room and the leftmost column's center row
        addWallObstacles(pane);

        // Set up character movement with collision detection
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

       

        // Add key press and release event handlers
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // AnimationTimer for smooth movement
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
                clearSpikesFromGame( pane);

                // Redraw all game objects
                for (Button button : buttons) {
                    button.draw(gc); // Draw all buttons
                }
                  checkButtonInteraction();
                  checkSpikeCollisions();

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
                }

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
    
      public void setOnLevelComplete4(Runnable onLevelComplete4) {
        this.onLevelComplete4 = onLevelComplete4;
    }

    private void completeLevel4() {
        if (onLevelComplete4 != null) {
            onLevelComplete4.run();
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
        else if (thirdArrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to previous level.");
            completeLevel3();
            timer.stop();
        }
        else if (fourthArrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to previous level.");
            completeLevel4();
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
    
    
    


     private void addButtonsToPane(Pane pane, double buttonWidth, double buttonHeight) {
        // Define and add buttons to the pane
        Button newYellowButton = new Button(getButtonX(0), getButtonY(0), Color.YELLOW, pane, spikes, "yellow");
        pane.getChildren().add(newYellowButton.getShape());
        buttons.add(newYellowButton);
        
        Button landingButtonPurp = new Button(getButtonX(3), getButtonY(3), Color.PURPLE, pane, spikes, "purple");
        pane.getChildren().add(landingButtonPurp.getShape());
        buttons.add(landingButtonPurp);

        Button blueButton = new Button(getButtonX(1), getButtonY(7), Color.BLUE, pane, spikes, "blue");
        pane.getChildren().add(blueButton.getShape());
        buttons.add(blueButton);
        
        
    }

    private double getButtonX(int col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }

    private double getButtonY(int row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }


    

private void addWallObstacles(Pane pane) {
    // Define offset values
    double wall2YOffset = 20; // Adjust this to shift wall2 upward
    double verticalWallYOffset = 30; // Adjust this to shift vertical walls upward
    double offset = 22;
    double horizontalShift = 10;
    //int obstacleIndex = 0;
    int checkerboardWidth = checkerboard.getCheckerboardWidth();
    int checkerboardHeight = checkerboard.getCheckerboardHeight();

    // Calculate the center of the room
    int centerRow = checkerboardHeight / 2;
    int centerColumn = checkerboardWidth / 2;

    // Move the center wall one block to the left
    int newCenterColumn = centerColumn - 1;

    // Calculate the total number of obstacles
    int totalObstacles = 2 + (checkerboardWidth - newCenterColumn - 1) + (centerRow) + 1 + 1;
    wallObstacles = new Rectangle[totalObstacles];

    // Hitbox size reduction
    double hitboxReduction = 50; // Reduce hitbox by 50 pixels
    double extraWidth = 20; // Extra width for stretching the rectangles
    double extraHeight = 20; // Adding extra height the rectangles

    // 1. Wall in the center of the room (shifted one block to the left)
Rectangle wall1 = new Rectangle(
    (newCenterColumn * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
    (centerRow * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - offset, // Shifting wall1 upward by offset
    (checkerboard.getCellSize() - hitboxReduction), // Increased width
    (checkerboard.getCellSize() - hitboxReduction) + extraHeight // Height remains the same
);
wall1.setFill(Color.BROWN);
pane.getChildren().add(wall1);
wallObstacles[0] = wall1;

    // 2. Wall in the center row of the leftmost column
Rectangle wall2 = new Rectangle(
    // Apply horizontal shift
    (0 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - horizontalShift,
    (centerRow * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
    (checkerboard.getCellSize() - hitboxReduction) + extraWidth, // Increased width
    checkerboard.getCellSize() - hitboxReduction // Height remains the same
);
wall2.setFill(Color.BROWN);
pane.getChildren().add(wall2);
wallObstacles[1] = wall2;

    // 3. Horizontal obstacles extending from center to the right side of the board
    int obstacleIndex = 2;
for (int col = newCenterColumn + 1; col < checkerboardWidth; col++) {
    Rectangle horizontalWall = new Rectangle(
        // Shift the X position to the left by subtracting horizontalShift
        (col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - horizontalShift,
        (centerRow * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
        (checkerboard.getCellSize() - hitboxReduction) + extraWidth, // Increased width
        checkerboard.getCellSize() - hitboxReduction // Height remains the same
    );
    horizontalWall.setFill(Color.BROWN);
    pane.getChildren().add(horizontalWall);
    wallObstacles[obstacleIndex++] = horizontalWall;
}

    // 4. Vertical obstacles extending from center to the top of the board
for (int row = centerRow - 1; row >= 0; row--) {
    Rectangle verticalWall = new Rectangle(
        ((centerColumn - 1) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
        (row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - offset, // Shifting vertical walls upward by offset
        (checkerboard.getCellSize() - hitboxReduction), // Increased width
        (checkerboard.getCellSize() - hitboxReduction) + extraHeight // Height remains the same
    );
    verticalWall.setFill(Color.BROWN);
    pane.getChildren().add(verticalWall);
    wallObstacles[obstacleIndex++] = verticalWall;
}


    // 5. Move obstacle from (3, 3) to (5, 2)
    // Move obstacle from (5, 2)
int toX = 5;
int toY = 2;
Rectangle movedWall = new Rectangle(
    // Apply horizontal shift
    (toX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - horizontalShift,
    (toY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
    (checkerboard.getCellSize() - hitboxReduction) + extraWidth, // Increased width
    checkerboard.getCellSize() - hitboxReduction // Height remains the same
);
movedWall.setFill(Color.BROWN);
pane.getChildren().add(movedWall);
wallObstacles[obstacleIndex++] = movedWall;

    // Add obstacle at (4, 2)
Rectangle obstacleAt4x2 = new Rectangle(
    // Apply horizontal shift
    (4 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2 - horizontalShift,
    (2 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()) + hitboxReduction / 2,
    (checkerboard.getCellSize() - hitboxReduction) + extraWidth, // Increased width
    checkerboard.getCellSize() - hitboxReduction // Height remains the same
);
obstacleAt4x2.setFill(Color.BROWN);
pane.getChildren().add(obstacleAt4x2);
wallObstacles[obstacleIndex++] = obstacleAt4x2;
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




    // Method to check for collisions between character and walls
    private boolean checkForCollisions() {
        double characterX = character.getLayoutX();
        double characterY = character.getLayoutY();
        double characterWidth = character.getFitWidth();
        double characterHeight = character.getFitHeight();

        // Check for collision with each wall obstacle
        for (Rectangle wall : wallObstacles) {
            double wallX = wall.getX();
            double wallY = wall.getY();
            double wallWidth = wall.getWidth();
            double wallHeight = wall.getHeight();

            if (characterX < wallX + wallWidth &&
                characterX + characterWidth > wallX &&
                characterY < wallY + wallHeight &&
                characterY + characterHeight > wallY) {
                return true;
            }
        }
        return false;
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