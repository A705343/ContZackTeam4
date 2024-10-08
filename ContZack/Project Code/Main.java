import javafx.scene.image.ImageView;
import javafx.scene.Scene; // Add this for Scene
import javafx.scene.image.Image; // Add this for Image
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    private Stage primaryStage;
    private Game game;
    private Pane gamePane;
    private PauseMenu pauseMenu;
    private boolean isPaused = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMainScreen();
         pauseMenu = new PauseMenu(); // Initialize once, reuse it

         // Set up the close request handler to save the game
       primaryStage.setOnCloseRequest(event -> {
             SaveLoadManager.saveGame(game);  // Call the save method on close
             System.exit(0);  // Ensure the application exits after saving
         });
    }

    private void showMainScreen() {
        MainScreen mainScreen = new MainScreen(primaryStage);
        mainScreen.setOnStartLevel1(this::startLevel1);
        primaryStage.setScene(mainScreen.getScene());
        primaryStage.setTitle("Contraption Zack");
        primaryStage.show();
    }    
    
     // Pause game method
    public void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            pauseMenu.showMenu();
        } else {
            isPaused = false;
            pauseMenu.hideMenu(); // Just hide the menu, don’t dispose it
        }
    }
    public void resumeGame() {
        isPaused = false;  // Reset the paused state
    }
    

    // Start Level 1
    private void startLevel1() {
        Level1 level1 = new Level1(this, primaryStage, 2, 5);  // Pass default starting positions
        level1.setOnLevelComplete(this::startLevel2);    // Set callback for level completion
        level1.start();                                  // Start Level 1
    }

    // Start Level 2
    private void startLevel2() {
        Level2 level2 = new Level2(this, primaryStage, 2, 10);
        level2.setOnLevelComplete(this::startLevel3);
        level2.setOnLevelComplete2(this::startLevel1);  // Pass default starting positions
        level2.start();                                  // Start Level 2
    }
    
     // Start Level 3
    private void startLevel3() {
        Level3 level3 = new Level3(primaryStage, 3, 10);
        level3.setOnLevelComplete(this::startLevel4); 
        level3.setOnLevelComplete2(this::startLevel6); // Pass default starting positions
        level3.start();                                  // Start Level 3
    }
    
     
    // Start Level 4
    private void startLevel4() {
        Level4 level4 = new Level4(primaryStage, 1, 5); 
        level4.setOnLevelComplete(this::startLevel5);
        level4.setOnLevelComplete2(this::startLevel3);  // Pass default starting positions
        level4.start();                                  // Start Level 4
    }
    
    //  Start Level 5
     private void startLevel5() {
         Level5 level5 = new Level5(primaryStage, 1, 4);
         level5.setOnLevelComplete(this::startLevel7);
         level5.setOnLevelComplete2(this::startLevel4);  // Pass default starting positions
         level5.start();                                  // Start Level 5    
        }
        
        //  Start Level 6
     private void startLevel6() {
         Level6 level6 = new Level6(primaryStage, 5, 3);
         level6.setOnLevelComplete(this::startLevel7);
         level6.setOnLevelComplete2(this::startLevel7);
          level6.setOnLevelComplete3(this::startLevel3);  // Pass default starting positions
         level6.start();                                  // Start Level 6    
        }
        
        private void startLevel7() {
         Level7 level7 = new Level7(primaryStage, 1, 6);
         level7.setOnLevelComplete(this::startLevel8);
         level7.setOnLevelComplete2(this::startLevel8);
         level7.setOnLevelComplete3(this::startLevel6);
         level7.setOnLevelComplete4(this::startLevel6);  // Pass default starting positions
         level7.start();                                  // Start Level 6    
        }
        
         private void startLevel8() {
         Level8 level8 = new Level8(primaryStage, 1, 6);
         level8.setOnLevelComplete(this::startLevel9);
         level8.setOnLevelComplete2(this::startLevel7);
        // level8.setOnLevelComplet3(this::startLevel);
         level8.setOnLevelComplete4(this::startLevel7);  // Pass default starting positions
         level8.start();                                  // Start Level 6    
        }
        
        
         private void startLevel9() {
         Level9 level9 = new Level9(primaryStage, 1, 0);
         level9.setOnLevelComplete(this::startLevel8);
         level9.setOnLevelComplete2(this::startLevel8);
         level9.setOnLevelComplete3(this::startLevel10);
          // Pass default starting positions
         level9.start();                                  // Start Level 6    
        }

 private void startLevel10() {
         Level10 level10 = new Level10(primaryStage, 2, 2);
         level10.setOnLevelComplete(this::startLevel9);
         level10.setOnLevelComplete2(this::showGameOverImage);
 
         level10.start();                                  
        }

 private void showGameOverImage() {
        // Logic to display the game over image
        Pane gameOverPane = new Pane();
        ImageView gameOverImageView = new ImageView("Assets/gameover.png"); // Specify your image path
        gameOverPane.getChildren().add(gameOverImageView);
        Scene gameOverScene = new Scene(gameOverPane, 800, 600); // Adjust size as needed
        primaryStage.setScene(gameOverScene);
    }

 
    public static void main(String[] args) {
        launch(args);
    }
}