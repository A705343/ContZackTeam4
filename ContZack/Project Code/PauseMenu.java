import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PauseMenu {
    private Stage pauseStage;
    private Game game;
    public PauseMenu() {
        pauseStage = new Stage();

        VBox layout = new VBox(10); // Set spacing between buttons
        
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            hideMenu(); // Hide the menu when resuming
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            SaveLoadManager.saveGame(game);
            System.out.println("Game Saved!"); // Placeholder
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            // Implement exit functionality
            System.exit(0); // Or handle exit more gracefully
        });

        layout.getChildren().addAll(resumeButton, saveButton, exitButton);

        Scene scene = new Scene(layout, 300, 200);
        pauseStage.setScene(scene);
    }

    public void showMenu() {
        pauseStage.show();
    }

    public void hideMenu() {
        pauseStage.hide(); 
    }

    public boolean isShowing() {
        return pauseStage.isShowing(); 
    }
}
