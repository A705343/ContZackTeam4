import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class MainScreen {
    private Scene scene;
    private Runnable onStartLevel1;
    private Runnable onStartLevel2;
    private Game game;  // Reference to the game object
    private Stage primaryStage; // Reference to the main stage

    public MainScreen(Stage primaryStage) {
        this.primaryStage = primaryStage; // Set the stage reference
        
        // Image and UI Setup
        Image image = new Image("https://images.launchbox-app.com/0d66f8bb-6536-4339-991d-99aba6cd37e9.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        // Buttons
        Button startButton = new Button("Start");
        Button loadButton = new Button("Load");

        startButton.setMaxSize(100, 200);
        loadButton.setMaxSize(100, 200);

        VBox buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, loadButton);

        Text devText = new Text("By: Danny Medina, Ara Garabedian, and Jason Halabo");
        devText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        devText.setFill(Color.WHITE);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(imageView, devText, buttonBox);
        vbox.setStyle("-fx-background-color: black;");

        startButton.setOnAction(event -> {
            if (onStartLevel1 != null) {
                onStartLevel1.run();  // Call the start level 1 callback
            }
        });
        
        loadButton.setOnAction(event -> {
            // Show FileChooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.setInitialDirectory(new File("saveData"));
            
            // Pass the primaryStage (the window) to showOpenDialog
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                SaveLoadManager.loadGame(game, file.getName());  // Load the game from the file
                startLoadedLevel();  // Transition to the loaded level
            }
            System.out.println("Load button clicked");
        });

        scene = new Scene(vbox, 1280, 960);
    }
    
    // Method to return the main scene
    public Scene getScene() {
        return scene;
    }

    // Method to set the callback for starting Level 1
    public void setOnStartLevel1(Runnable onStartLevel1) {
        this.onStartLevel1 = onStartLevel1;
    }

    public void setOnStartLevel2(Runnable onStartLevel2) {
        this.onStartLevel2 = onStartLevel2;
    }

    // After loading the game, transition to the loaded level
   private void startLoadedLevel() {
    
}
}