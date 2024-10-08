import java.io.*;
import java.util.*;
import javafx.scene.paint.Color;

public class SaveLoadManager {

    private static final String SAVE_DIRECTORY = "saveData/";

    // Ensure the saveData directory exists
    public static void ensureSaveDirectoryExists() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();  // Create directory if it doesn't exist
        }
    }

    // Save the game state to a file with an incrementing number in the filename
    public static void saveGame(Game game) {
        ensureSaveDirectoryExists();

        // Find the next available save file number
        int saveNumber = getNextSaveNumber();
        String fileName = "game_save" + saveNumber + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_DIRECTORY + fileName))) {
           
            
            System.out.println("Game saved successfully as " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the game state from a file
    public static void loadGame(Game game, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_DIRECTORY + fileName))) {
            String line;

     
            System.out.println("Game loaded successfully from " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Find the next available save file number by checking the existing files in the saveData directory
    private static int getNextSaveNumber() {
        File directory = new File(SAVE_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game_save") && name.endsWith(".txt"));

        int maxNumber = 0;
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String numberPart = fileName.replaceAll("[^0-9]", "");  // Extract the numeric part
                if (!numberPart.isEmpty()) {
                    int number = Integer.parseInt(numberPart);
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                }
            }
        }

        return maxNumber + 1;  // Increment to get the next save number
    }
}
