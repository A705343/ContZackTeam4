import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.input.KeyCode;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.*;
import javafx.scene.layout.Pane;

public class Level3 {
    private Pane gamePane;
    private ImageView character;
    private ImageView background;
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
    private AnimationTimer timer;
    private final int startX;
    private final int startY;
    private List<Button> buttons = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private Spikes spikes;  // Use this single instance
    private List<Spikes> spikesList = new ArrayList<>();
    private Screwdriver screwdriver;
     // Declare isScrewdriverCollected variable
    private boolean isScrewdriverCollected;
    private boolean buttonTimer = false;

     

    private Rectangle[] doors = new Rectangle[4];
    private boolean[] doorsOpen = new boolean[4];
    private int openDoorIndex = 0;

    public Level3(Stage primaryStage, int startX, int startY) {
        this.primaryStage = primaryStage;
        this.startX = startX;
        this.startY = startY;
        gamePane = new Pane();
        initializeCheckerboard();
        spikes = new Spikes(gamePane);  // Initialize spikes here
         screwdriver = new Screwdriver(0, 0);
         
             }

    private void initializeCheckerboard() {
        this.checkerboard = new Checkerboard(6, 9, 80, 2);
        gamePane.getChildren().add(checkerboard.getPane());
    }

    public void start() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");

        checkerboard = new Checkerboard(6, 6, 100, 1);

        Canvas canvas = new Canvas(
            (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize(),
            (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize()
        );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        initializeGameBoard(pane);
        
  isScrewdriverCollected = false;
  
  
        // Adding walls
        walls.add(new Wall(300, 420, 10, 80, Color.BLACK, pane));
        walls.add(new Wall(500, 420, 10, 80, Color.BLACK, pane));
        walls.add(new Wall(100, 460, 200, 20, Color.BLACK, pane));
        walls.add(new Wall(500, 460, 200, 20, Color.BLACK, pane));
        walls.add(new Wall(100, 340, 240, 20, Color.BLACK, pane));
        walls.add(new Wall(470, 340, 240, 20, Color.BLACK, pane));
        walls.add(new Wall(100, 570, 210, 20, Color.BLACK, pane));
        walls.add(new Wall(500, 570, 200, 20, Color.BLACK, pane));
        walls.add(new Wall(300, 200, 230, 20, Color.BLACK, pane));
        walls.add(new Wall(300, 100, 600, 120, Color.BLACK, pane));
        walls.add(new Wall(100, 100, 100, 100, Color.BLACK, pane));
        
        
       // Add the screwdriver to the pane at the top-right position
double screwdriverX = (6) * checkerboard.getCellSize();
double screwdriverY = 2.5 * checkerboard.getCellSize();
screwdriver.setPosition(screwdriverX, screwdriverY);
pane.getChildren().add(screwdriver.getImageView());

        // Create and add the arrows
        int arrowX = 1;
        int arrowY = 0;
        int arrowSize = 100;
        arrow = new Arrow(
            "Assets/arrow.png",
            0, 0,
            arrowSize, arrowSize
        );
        arrow.setPosition(
            arrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
            arrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
        );
        arrow.getImageView().setRotate(90);
        pane.getChildren().add(arrow.getImageView());

        double secondArrowX = 0;
        double secondArrowY = 1;
        secondArrow = new Arrow(
            "Assets/arrow.png",
            0.8, 0.2,
            arrowSize, arrowSize
        );
        secondArrow.setPosition(
            secondArrowX * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize(),
            secondArrowY * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize()
        );
        pane.getChildren().add(secondArrow.getImageView());

        // Character initialization
        Image characterImage = new Image("Assets/jack1.png");
        character = new ImageView(characterImage);
        character.setFitWidth(frameWidth);
        character.setFitHeight(frameHeight);
        pane.getChildren().add(character);
        setCharacterStartPosition(startX, startY);

        characterMover = new CharacterMover(character, frameWidth, frameHeight, pressedKeys, checkerboard);

        double buttonWidth = 50; // Width of the button
        double buttonHeight = 50; // Height of the button

        // Initialize and add buttons to the pane
        addButtonsToPane(pane, 25);

        // Ensure the character is in front
        character.toFront();
        spikes.clearSpikesFromGame(pane);
        // Add spikes using the single 'spikes' instance
        spikes.addSpikeToGame(pane, "blue_vert", 300, 215, true, 25, 120);
        spikes.addSpikeToGame(pane, "purple", 340, 330, true, 140, 20);
        spikes.addSpikeToGame(pane, "yellow", 340, 350, true, 140, 20);
        spikes.addSpikeToGame(pane, "green_vert", 510, 215, true, 25, 120);

        // Create and add the 4 doors
        for (int i = 0; i < 4; i++) {
            doors[i] = new Rectangle(10, 70);
            doors[i].setFill(Color.BLACK);
            doors[i].setVisible(true);
            if (i < 2) {
                doors[i].setLayoutX(300);
            } else {
                doors[i].setLayoutX(500);
            }

            if (i % 2 == 0) {
                doors[i].setLayoutY(200 + 150);
            } else {
                doors[i].setLayoutY(200 + (2 * 150));
            }

            doorsOpen[i] = false;
            pane.getChildren().add(doors[i]);
        }

        // Set the first door to be open initially
        doorsOpen[openDoorIndex] = true;
        doors[openDoorIndex].setVisible(false);

        // Timeline for doors
        Timeline doorTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            doorsOpen[openDoorIndex] = false;
            doors[openDoorIndex].setVisible(true);

            openDoorIndex = (openDoorIndex + 1) % 4;

            doorsOpen[openDoorIndex] = true;
            doors[openDoorIndex].setVisible(false);
        }));
        doorTimeline.setCycleCount(Timeline.INDEFINITE);
        doorTimeline.play();

        Scene scene = new Scene(pane, canvas.getWidth(), canvas.getHeight());

        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());

            // Check for 'E' key press
            if (event.getCode() == KeyCode.E) {
                checkButtonInteraction();  // Call method to check button interaction
            }
        });

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // AnimationTimer for character movement and collision checks
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                 gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas

                // Redraw all game objects
                for (Button button : buttons) {
                    button.draw(gc); // Draw all buttons
                }
                  
                characterMover.moveCharacter();
                checkLevelCompletion();
                checkDoorCollisions();
                checkWallCollisions();
                checkSpikeCollisions();
                character.toFront();
                checkScrewdriverCollision();  // Check for screwdriver collision
                }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
   private void checkScrewdriverCollision() {
    if (!isScrewdriverCollected && character.getBoundsInParent().intersects(screwdriver.getImageView().getBoundsInParent())) {
        isScrewdriverCollected = true;
        System.out.println("Screwdriver collected!");

        // Remove or hide the screwdriver after collection
       // gamePane.getChildren().remove(screwdriver.getImageView());  // Option 1: remove it from the pane
         screwdriver.getImageView().setVisible(false);  // Option 2: make it invisible
    }
}


    private void initializeGameBoard(Pane pane) {
        double canvasWidth = (checkerboard.getCheckerboardWidth() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize();
        double canvasHeight = (checkerboard.getCheckerboardHeight() + 2 * checkerboard.getBorderSize()) * checkerboard.getCellSize();

        double backgroundWidth = checkerboard.getCheckerboardWidth() * checkerboard.getCellSize();
        double backgroundHeight = checkerboard.getCheckerboardHeight() * checkerboard.getCellSize();

        background = new ImageView("Assets/stoneFloor.jpg");

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
            System.out.println("Collision detected with arrow, moving to level 4.");
            completeLevel2();
            timer.stop();
        }
    }

    private void moveArrowToBlock(int row, int col) {
        double x = col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        double y = row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize();
        arrow.setPosition(x, y);
    }

    private void checkWallCollisions() {
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

    private void checkDoorCollisions() {
        for (int i = 0; i < 4; i++) {
            if (!doorsOpen[i] && character.getBoundsInParent().intersects(doors[i].getBoundsInParent())) {
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
        }
    }

    private void addButtonsToPane(Pane pane, double buttonRadius) {
    Button room1Button = new Button(getButtonX(-0.4), getButtonY(3.4), Color.PURPLE, buttonRadius, pane, spikes, "purple");
        pane.getChildren().add(room1Button.getCircularShape());
        buttons.add(room1Button);

        Button room2Button = new Button(getButtonX(0.4), getButtonY(3.4), Color.GREEN, buttonRadius, pane, spikes, "green_vert");
        pane.getChildren().add(room2Button.getCircularShape());
        buttons.add(room2Button);

        Button room3Button = new Button(getButtonX(4.6), getButtonY(3.4), Color.YELLOW, buttonRadius, pane, spikes, "yellow");
        pane.getChildren().add(room3Button.getCircularShape());
        buttons.add(room3Button);

        Button room4Button = new Button(getButtonX(-0.4), getButtonY(2.2), Color.YELLOW, buttonRadius, pane, spikes, "yellow");
        pane.getChildren().add(room4Button.getCircularShape());
        buttons.add(room4Button);

        Button room5Button = new Button(getButtonX(4.6), getButtonY(2.2), Color.PURPLE, buttonRadius, pane, spikes, "purple");
        pane.getChildren().add(room5Button.getCircularShape());
        buttons.add(room5Button);

        Button room6Button = new Button(getButtonX(3.8), getButtonY(2.2), Color.BLUE, buttonRadius, pane, spikes, "blue_vert");
        pane.getChildren().add(room6Button.getCircularShape());
        buttons.add(room6Button);
}




private void checkButtonInteraction() {
    if (!pressedKeys.contains(KeyCode.E)) {
        return;
    }
     
                     
    for (Button button : buttons) {
        if (!button.isActivated() && character.getBoundsInParent().intersects(button.getCircularShape().getBoundsInParent())) {
            button.activate(); // Activate the button
            
                          
        }
         // Create a Timeline to deactivate the button after 10 seconds
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                     // Deactivate the button after 10 seconds
                    // button.deactivate();
                      }));
                timer.setCycleCount(1); // The timer runs once
                timer.play(); // Start the timer


    }
}
    private double getButtonX(double col) {
        return col * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2;
    }

    private double getButtonY(double row) {
        return row * checkerboard.getCellSize() + checkerboard.getBorderSize() * checkerboard.getCellSize() + checkerboard.getCellSize() / 2;
    }

    private void checkSpikeCollisions() {
        for (Spikes.Spike spike : spikes.getAllSpikes()) {
            if (spike.isSpikeUp()) {
                if (character.getBoundsInParent().intersects(spike.getView().getBoundsInParent())) {
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
            }
        }
    }
}