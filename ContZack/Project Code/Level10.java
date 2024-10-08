import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.input.KeyCode;
import javafx.scene.input.*;

public class Level10 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private AnimationTimer timer;
    private List<Wall> walls = new ArrayList<>();
    private final int startX;
    private final int startY;
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;
    private List<Switch> switches = new ArrayList<>(); // List to hold multiple switches
     private List<Button> buttons = new ArrayList<>();
    private List<Spikes> spikesList = new ArrayList<>();
     private Spikes spikes;
      private Pane gamePane;
      private Arrow arrow;
    private Arrow secondArrow;
     private ImageView gameOverImage; // Image view for the game over image

    public Level10(Stage primaryStage, int startX, int startY) {
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
        gamePane = new Pane();
        
        spikes = new Spikes(gamePane);
       
       
    }

    public void start() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");
         spikes.clearSpikesFromGame(pane);
        // Initialize the checkerboard for Level 10 (6x6 grid)
        checkerboard = new Checkerboard(6, 6, 100, 1);
        
        // Initialize the canvas for drawing game objects
        Canvas canvas = new Canvas(
                (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
                (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        initializeGameBoard(pane);
        
        // Create and add the arrow
      int arrowX = 0; // Column (0-indexed)
      int arrowY = 1; // Row (0-indexed)
      int arrowSize = 100;
      arrow = new Arrow(
         "Assets/arrow.png",
         0, 0, // Initial position
         arrowSize, arrowSize
      );
      arrow.setPosition(
         arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
         arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
      );
      arrow.getImageView().setRotate(90);
      pane.getChildren().add(arrow.getImageView());
  
      // Add the second arrow in the top left corner
      double secondArrowX = 5; // Column (0-indexed)
      double secondArrowY = 5; // Row (0-indexed)
      secondArrow = new Arrow(
         "Assets/arrow.png",
         0, 0,
         arrowSize, arrowSize
      );
      secondArrow.setPosition(
         secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
         secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
      );
      //secondArrow.getImageView().setRotate(180);
      pane.getChildren().add(secondArrow.getImageView());
        
      arrow.getImageView().toFront(); // Move the arrow to the front
      secondArrow.getImageView().toFront(); // Move the arrow to the front


        // Character initialization
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);
        
        
        // Create a single Spikes instance for the entire level
         Spikes spikesManager = new Spikes(gamePane);
         
         
         
         // Add blue spikes
         Spikes blueSpike1 = new Spikes(gamePane);
         blueSpike1.addSpikeToGame(pane, "blue", 425, 500, true, 75, 18);
         spikesList.add(blueSpike1);

 
        // Button dimensions
        double buttonWidth = 50; // Width of the button
        double buttonHeight = 50; // Height of the button

        // Initialize and add buttons to the pane
        addButtonsToPane(pane, buttonWidth, buttonHeight);


        // Set up character movement
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

        Scene scene = new Scene(pane, canvas.getWidth(), canvas.getHeight());

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                characterMover.moveCharacter();
                checkButtonInteraction();  // Check all buttons for interaction
                checkWallCollisions();
                checkSpikeCollisions();
                checkLevelCompletion();
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();

        // Create walls for the level
        createWalls(pane);
    }
    
    

    private void initializeGameBoard(Pane pane) {
        double canvasWidth = (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize();
        double canvasHeight = (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize();

        double backgroundWidth = checkerboard.getCheckerboardWidth() * checkerboard.getCellSize();
        double backgroundHeight = checkerboard.getCheckerboardHeight() * checkerboard.getCellSize();

        ImageView background = new ImageView("Assets/stoneFloor.jpg");
      

        // Set background size to match the board
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

        // Center the background
        double offsetX = (canvasWidth - backgroundWidth) / 2;
        double offsetY = (canvasHeight - backgroundHeight) / 2;

        background.setLayoutX(offsetX);
        background.setLayoutY(offsetY);

        pane.getChildren().add(background);
    }

    private void setCharacterStartPosition(int x, int y) {
        double xPos = x * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double yPos = y * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        character.setLayoutX(xPos);
        character.setLayoutY(yPos);
    }

   private void createWalls(Pane pane) {
    int wallThickness = 10; // Thickness of the wall
    int cellSize = checkerboard.getCellSize();
    // Horizontal wall at (3, 1) and (3, 2)
        walls.add(new Wall(6 * cellSize, 5 * cellSize, 1 * cellSize, wallThickness, Color.BLACK, pane));   
        walls.add(new Wall(5 * cellSize, 5 * cellSize, 1 * cellSize, wallThickness, Color.BLACK, pane)); 
        walls.add(new Wall(3 * cellSize, 5 * cellSize, 1 * cellSize, wallThickness, Color.BLACK, pane));
        walls.add(new Wall(2 * cellSize, 5 * cellSize, 1 * cellSize, wallThickness, Color.BLACK, pane)); 
        //vertical
         walls.add(new Wall(2 * cellSize, 5 * cellSize, wallThickness, 5 * cellSize, Color.BLACK, pane));
         walls.add(new Wall(2 * cellSize, 6 * cellSize, wallThickness, 5 * cellSize, Color.BLACK, pane));
          // Adding 3 switches to the upper-right corner
    for (int i = 0; i < 3; i++) {
        Switch newSwitch = new Switch(null, "Assets/switch.png"); // Replace 'null' with conveyor belts or other interactive objects if needed
        pane.getChildren().add(newSwitch.getSwitchView());
        
        // Position the switches in the upper-right corner, adjusting their layout
        newSwitch.getSwitchView().setLayoutX(6 * cellSize ); // Adjust the X position for each switch
        newSwitch.getSwitchView().setLayoutY(2 * cellSize + (i * 75)); // Place them in the first row

        // Add the switch to the list of switches
        switches.add(newSwitch);
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

    
     private void addButtonsToPane(Pane pane, double buttonWidth, double buttonHeight) {
      

        Button blueButton = new Button(getButtonX(2), getButtonY(2), Color.BLUE, pane, spikes, "blue");
        pane.getChildren().add(blueButton.getShape());
        buttons.add(blueButton);
        
       
    }

    private double getButtonX(int col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }

    private double getButtonY(int row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
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
        if (arrow.checkCollision(character)) {
            System.out.println("Collision detected with arrow, moving to level \"6\".");
            completeLevel();
            timer.stop();
        } else if (secondArrow.checkCollision(character)) {
            System.out.println("Game Over");
             
            completeLevel2();
            timer.stop();
        }
    }
}
