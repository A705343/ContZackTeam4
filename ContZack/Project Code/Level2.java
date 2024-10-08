import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.input.*;


public class Level2 {
    private ImageView character;
    private final int frameWidth = 21;
    private final int frameHeight = 35;
    private Checkerboard checkerboard;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CharacterMover characterMover;
    private Stage primaryStage;
    private Runnable onLevelComplete;
    private Runnable onLevelComplete2;
    private Arrow arrow;
    private Arrow secondArrow;
    private ImageView bottomArrow;
    private double cellSize;
    private Pane pane;
    private Launchpad launchpad;
    private Launchpad secondLaunchpad;
    private AnimationTimer timer;
    private final int startX;
    private final int startY;
    private Game game;
    private Pane gamePane;
    private Spikes spikes;
    private StackPane root;
    private Scene gameScene;
    private Main main; // Reference to the main class
    
    private List<Wall> walls = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();
    private List<Spikes> spikesList = new ArrayList<>();

    public Level2(Main main, Stage primaryStage, int startX, int startY) {
        this.main = main;
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
        gamePane = new Pane();
        initializeCheckerboard();
        spikes = new Spikes(gamePane);
    }
    
     private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) { // Pause on ESC key press
            main.pauseGame(); // Call the pause method in main
        }
    }

    
  //   public Level2(Pane gamePane) {
//         this.gamePane = gamePane;
//         this.startX = 0; // Provide default value or change based on your needs
//         this.startY = 0; // Provide default value or change based on your needs
//         initializeCheckerboard();
//         
//     }
    
    private void initializeCheckerboard() {
        this.checkerboard = new Checkerboard(6, 9, 80, 2); // Assuming 6x9 checkerboard and block size of 80
        gamePane.getChildren().add(checkerboard.getPane());
    }
    
    

    public void start() {
        pane = new Pane();
        pane.setStyle("-fx-background-color: black;");
        
         // StackPane to hold both the game and the pause menu
        root = new StackPane();
        root.getChildren().add(gamePane);

       

        // Create scene using StackPane
        gameScene = new Scene(root, 
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(), 
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        // Initialize the checkerboard for Level 2 (6x9)
        checkerboard = new Checkerboard(6, 9, 80, 1); // 6 columns, 9 rows, cell size 80, border size 1
        

        // Initialize the canvas for drawing game objects
        Canvas canvas = new Canvas(
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        
        // Add the canvas to the pane
        pane.getChildren().add(canvas);
        initializeGameBoard(pane);
        
        // List of blacked-out blocks (e.g., [x, y] coordinates)
        walls.add(new Wall(80, 400, 80, 80, Color.DARKGRAY, pane)); // left side after first spike
        walls.add(new Wall(480, 400, 80, 80, Color.DARKGRAY, pane)); // right side after first spike
        // 400 for left side middle is important info
        walls.add(new Wall(80, 80, 240, 160, Color.BLACK, pane)); // top left blockage
        walls.add(new Wall(400, 80, 160, 160, Color.BLACK, pane)); // top right blockage
        walls.add(new Wall(240, 320, 160, 160, Color.BLACK, pane)); // middle square thing
        walls.add(new Wall(160, 560, 320, 80, Color.BLACK, pane)); // bottomish long rectangle
        walls.add(new Wall(80, 720, 160, 80, Color.BLACK, pane)); // left side spawn
        walls.add(new Wall(400, 720, 160, 80, Color.BLACK, pane)); // right side spawn

        // Load character image
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);

        // Set the character image size
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);
        
        int arrowX = 0; // Example column (0-indexed)
        int arrowY = 3; // Example row (0-indexed)
        int arrowSize = 80; // Size of the arrow image
        arrow = new Arrow(
            "Assets/arrow.png",
            0, 0, // Initial position
            arrowSize, arrowSize
        );
        pane.getChildren().add(arrow.getImageView());

        // Move the arrow to the desired position
        moveArrowToBlock(arrow, arrowX, arrowY);
        arrow.getImageView().setRotate(90);  // Rotate arrow
            
        // Add the second arrow at column 0, row 7
int secondArrowX = 3; // Column 0
int secondArrowY = 8; // Row 7 (0-indexed)
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


            

        // Add character to pane and set its starting position
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        // Set up character movement
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);
        
        // Create a single Spikes instance for the entire level
         Spikes spikesManager = new Spikes(gamePane);
         
         // Add blue spikes
         Spikes blueSpike1 = new Spikes(gamePane);
         blueSpike1.addSpikeToGame(pane, "blue", 162, 442, true, 75, 18);
         spikesList.add(blueSpike1);
         
         Spikes blueSpike2 = new Spikes(gamePane);
         blueSpike2.addSpikeToGame(pane, "blue", 325, 219, true, 75, 18);
         spikesList.add(blueSpike2);
         
         Spikes blueSpike3 = new Spikes(gamePane);   // vertical
         blueSpike3.addSpikeToGame(pane, "blue", 381, 241, true, 18, 75);    
         spikesList.add(blueSpike3);  
         
         Spikes blueSpike4 = new Spikes(gamePane); // vertical     
         blueSpike4.addSpikeToGame(pane, "blue", 240, 240, false, 18, 75);   
         spikesList.add(blueSpike4); 
         
         Spikes blueSpike5 = new Spikes(gamePane);      
         blueSpike5.addSpikeToGame(pane, "blue", 401, 442, false, 75, 18);   
         spikesList.add(blueSpike5);   
         
         // Add orange spikes
         Spikes orangeSpike1 = new Spikes(gamePane);
         orangeSpike1.addSpikeToGame(pane, "orange", 401, 460, true, 75, 18);
         spikesList.add(orangeSpike1);  
         
         Spikes orangeSpike2 = new Spikes(gamePane);
         orangeSpike2.addSpikeToGame(pane, "orange", 325, 180, true, 75, 18);
         spikesList.add(orangeSpike2);  
         
         Spikes orangeSpike3 = new Spikes(gamePane);
         orangeSpike3.addSpikeToGame(pane, "orange", 162, 460, true, 75, 18);
         spikesList.add(orangeSpike3);  
         
         // Add purple spike
         Spikes purpleSpike = new Spikes(gamePane);
         purpleSpike.addSpikeToGame(pane, "purple", 325, 160, true, 75, 18);
         spikesList.add(purpleSpike);  
         
         // Add yellow spikes
         Spikes yellowSpike1 = new Spikes(gamePane);
         yellowSpike1.addSpikeToGame(pane, "yellow", 325, 200, true, 75, 18);
         spikesList.add(yellowSpike1);  
         
         Spikes yellowSpike2 = new Spikes(gamePane);
         yellowSpike2.addSpikeToGame(pane, "yellow", 83, 618, true, 75, 18);
         spikesList.add(yellowSpike2);  
         
         Spikes yellowSpike3 = new Spikes(gamePane);
         yellowSpike3.addSpikeToGame(pane, "yellow", 482, 620, false, 75, 18);
         spikesList.add(yellowSpike3);
       

        
        // Initialize and add the launchpad to the pane
        double launchpadStartX = 3 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadStartY = 7 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        //where the player will land
        double launchpadTargetX = (3 + 3) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double launchpadTargetY = 8 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        
        launchpad = new Launchpad(launchpadStartX, launchpadStartY, launchpadTargetX, launchpadTargetY);
        pane.getChildren().add(launchpad.getImageView());
        
        // Initialize and add the second launchpad to the left of the first one
         double secondLaunchpadStartX = (2) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();  // Move 2 cells to the left
         double secondLaunchpadStartY = 7 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         // Where the player will land for the second launchpad
         double secondLaunchpadTargetX = (1) * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         double secondLaunchpadTargetY = 8 * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
         
         secondLaunchpad = new Launchpad(secondLaunchpadStartX, secondLaunchpadStartY, secondLaunchpadTargetX, secondLaunchpadTargetY);
         // Rotate the launchpad 180 degrees
         secondLaunchpad.getImageView().setRotate(180);
         pane.getChildren().add(secondLaunchpad.getImageView());
         

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
        
        // Initialize character mover
        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

        // Use AnimationTimer for smooth movement and animation
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas

                // Redraw all game objects
                for (Button button : buttons) {
                    button.draw(gc); // Draw all buttons
                }
                characterMover.moveCharacter();  // Handle character movement
                checkLevelCompletion();
                checkLaunchpadInteraction();  // Check for launchpad interaction
                checkButtonInteraction();  // Check all buttons for interaction
                checkWallCollisions();
                checkSpikeCollisions();
                character.toFront();
            
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

    private void initializeGameBoard(Pane pane) {
    double checkerboardWidth = checkerboard.getCheckerboardWidth() * checkerboard.getCellSize();
    double checkerboardHeight = checkerboard.getCheckerboardHeight() * checkerboard.getCellSize();
    double borderSize = checkerboard.getBorderSize() * checkerboard.getCellSize();

    checkerboard.getPane().setLayoutX(borderSize);
    checkerboard.getPane().setLayoutY(borderSize);
    pane.getChildren().add(checkerboard.getPane());
}

    private void moveArrowToBlock(Arrow arrow, int row, int col) {
    double cellSize = checkerboard.getCellSize();
    double borderSize = checkerboard.getBorderSize() * cellSize;
    
    double x = col * cellSize + borderSize;
    double y = row * cellSize + borderSize;
    
    arrow.setPosition(x, y);
    System.out.println("Arrow position set to: (" + x + ", " + y + ")");
    }

    private void setCharacterStartPosition(int x, int y) {
        double xPos = x * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double yPos = y * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();

        character.setLayoutX(xPos);
        character.setLayoutY(yPos);
        
        System.out.println("Character starting at X: " + character.getLayoutX() + ", Y: " + character.getLayoutY());
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



    private void checkLevelCompletion() {
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

    private void addButtonsToPane(Pane pane, double buttonWidth, double buttonHeight) {
        // Define and add buttons to the pane
        Button newYellowButton = new Button(getButtonX(0), getButtonY(7), Color.YELLOW, pane, spikes, "yellow");
        pane.getChildren().add(newYellowButton.getShape());
        buttons.add(newYellowButton);
        
        Button landingButtonPurp = new Button(getButtonX(3 + 2), getButtonY(7), Color.PURPLE, pane, spikes, "purple");
        pane.getChildren().add(landingButtonPurp.getShape());
        buttons.add(landingButtonPurp);

        Button orangeButton = new Button(getButtonX(3 + 1), getButtonY(5), Color.ORANGE, pane, spikes, "orange");
        pane.getChildren().add(orangeButton.getShape());
        buttons.add(orangeButton);
        
        //second orange button
        Button newOrangeButton = new Button(getButtonX(0), getButtonY(2), Color.ORANGE, pane, spikes, "orange");
        pane.getChildren().add(newOrangeButton.getShape());
        buttons.add(newOrangeButton);

        Button yellowButton = new Button(getButtonX(3), getButtonY(5), Color.YELLOW, pane, spikes, "yellow");
        pane.getChildren().add(yellowButton.getShape());
        buttons.add(yellowButton);

        Button blueButton = new Button(getButtonX(2), getButtonY(5), Color.BLUE, pane, spikes, "blue");
        pane.getChildren().add(blueButton.getShape());
        buttons.add(blueButton);
        
        //second blue button
        Button newBlueButton = new Button(getButtonX(5), getButtonY(2), Color.BLUE, pane, spikes, "blue");
        pane.getChildren().add(newBlueButton.getShape());
        buttons.add(newBlueButton);

        Button newPurpleButton = new Button(getButtonX(1), getButtonY(5), Color.PURPLE, pane, spikes, "purple");
        pane.getChildren().add(newPurpleButton.getShape());
        buttons.add(newPurpleButton);
    }

    private double getButtonX(int col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
    }

    private double getButtonY(int row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2 - 25;
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