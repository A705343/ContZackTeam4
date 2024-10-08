import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.*;

public class Spikes {
    private final Map<String, Image> spikeImages = new HashMap<>();
    private final Map<String, Image> spikeDownImages = new HashMap<>();
    //private final int horSpikeWidth = 18, horSpikeHeight = 75, vertSpikeWidth = 75, vertSpikeHeight = 18;
    private boolean isUp;
    private String type;
    private Pane pane;
    private ImageView view;
    double x, y;
    private static int spikeIDCounter = 1;  // Counter for unique spike ID
    

    // List of spike objects (each with ImageView and its corresponding boundary)
    private static List<Spike> spikeList = new ArrayList<>();
    private List<Spike> yellowSpikes = new ArrayList<>();
    private List<Spike> blueSpikes = new ArrayList<>();
    private List<Spike> orangeSpikes = new ArrayList<>();
    private List<Spike> purpleSpikes = new ArrayList<>();

    // Constructor to initialize spike images
    public Spikes(Pane pane) {
        this.pane = pane;
        
        loadSpikeImages();
    }
    
    private void loadSpikeImages() {
        spikeImages.put("blue", new Image(("Assets/BHor.png")));
        spikeImages.put("orange", new Image(("Assets/OHor.png")));
        spikeImages.put("purple", new Image(("Assets/PHor.png")));
        spikeImages.put("yellow", new Image(("Assets/YHor.png")));
        spikeImages.put("ehor", new Image(("Assets/EHor.png")));
        spikeImages.put("evert", new Image(("Assets/EVert.png")));
        spikeImages.put("blue_vert", new Image(("Assets/BVert.png")));
        spikeImages.put("green_vert", new Image(("Assets/GVert.png")));

        spikeDownImages.put("blue", new Image(("Assets/EHor.png")));
        spikeDownImages.put("orange", new Image(("Assets/EHor.png")));
        spikeDownImages.put("purple", new Image(("Assets/EHor.png")));
        spikeDownImages.put("yellow", new Image(("Assets/EHor.png")));
        spikeDownImages.put("ehor", new Image(("Assets/EHor.png")));
        spikeDownImages.put("evert", new Image(("Assets/EVert.png")));
        spikeDownImages.put("blue_vert", new Image(("Assets/EVert.png")));
        spikeDownImages.put("green_vert", new Image(("Assets/EVert.png")));
    }

   public void addSpikeToGame(Pane pane, String type, double x, double y, boolean isUp, double width, double height) {
    Image spikeImage = isUp ? spikeImages.get(type.toLowerCase()) : spikeDownImages.get(type.toLowerCase());

    if (spikeImage == null) {
        System.out.println("Unknown spike type: " + type);
        return;
    }

    // Create new ImageView for the spike
    ImageView spikeView = new ImageView(spikeImage);
    
    // Set the size based on the orientation (horizontal or vertical)
    spikeView.setFitWidth(width);  
    spikeView.setFitHeight(height); 

    // Set position of the spike
    spikeView.setX(x);
    spikeView.setY(y);

    // Add to the pane and list
    pane.getChildren().add(spikeView);
    Spike newSpike = new Spike(spikeView, type, spikeIDCounter++, isUp);
    spikeList.add(newSpike); // Add to spikeList
}


    public boolean isCollidable(String type) {
        return !type.equalsIgnoreCase("ehor") && !type.equalsIgnoreCase("evert");
    }
    
public void removeSpikeFromGame(int spikeID) {
    Spike spikeToRemove = null;
    for (Spike spike : spikeList) {
        if (spike.getID() == spikeID) {
            spikeToRemove = spike;
            break;
        }
    }

    if (spikeToRemove != null) {
        pane.getChildren().remove(spikeToRemove.getView());
        spikeList.remove(spikeToRemove); // Remove from spikeList as well
    }
}

    // Get all spikes
    public List<Spike> getAllSpikes() {
        List<Spike> allSpikes = new ArrayList<>();
        allSpikes.addAll(yellowSpikes);
        allSpikes.addAll(blueSpikes);
        allSpikes.addAll(orangeSpikes);
        allSpikes.addAll(purpleSpikes);
        allSpikes.addAll(spikeList);
        return allSpikes;
    }

    
    // Public inner class to store both spike ImageView and its boundary
    public static class Spike {
        private final ImageView view;
        private String type;
        private final int id;  // Unique identifier for each spike
        private boolean isUp;

        public Spike(ImageView view, String type, int id, boolean isUp) {
            this.view = view;
            this.type = type;
            this.id = id;  // Assign the unique ID to the spike
            this.isUp = isUp;
        }

        public ImageView getView() {
            return view;
        }

        public String getType() {
            return type;
        }

        public int getID() {
            return id;  // Return the unique ID
        }
        
        public boolean isSpikeUp() {
            return isUp;
        }
    }

  public boolean checkCollisionWithPlayer(ImageView player) {
    for (Spike spike : spikeList) {
        if (spike.isSpikeUp() && player.getBoundsInParent().intersects(spike.getView().getBoundsInParent())) {
            return true; // Player collides with active spikes
        }
    }
    return false; // No collision with spikes
}

    public boolean isSpikeUp() {
        return isUp;
    }

    public String getType() {
        return type;
    }
    
    public ImageView getView() {
        return view;
    }
    
  public void toggleSpikeState(Spike spike) {
    boolean newState = !spike.isSpikeUp();
    
    double currentWidth = spike.getView().getFitWidth();
    double currentHeight = spike.getView().getFitHeight();
    String spikeType = spike.getType();
    double currentX = spike.getView().getX();
    double currentY = spike.getView().getY();

    // Debugging output
    System.out.println("Toggling Spike: " + spikeType + ", Current State: " + spike.isSpikeUp() + ", New State: " + newState);

    // Remove the current spike from the game
    removeSpikeFromGame(spike.getID());
    addSpikeToGame(pane, spikeType, currentX, currentY, newState, currentWidth, currentHeight);
}
    
    public void updateSpikeImage(Spike spike, boolean isUp) {
    Image spikeImage = isUp ? spikeImages.get(spike.getType().toLowerCase()) : spikeDownImages.get(spike.getType().toLowerCase());
    if (spikeImage != null) {
        spike.getView().setImage(spikeImage);
        spike.isUp = isUp;
        refreshSpikeView(spike); // Refresh view to ensure changes take effect
    } else {
        System.out.println("Unknown spike type: " + spike.getType());
    }
}

private void refreshSpikeView(Spike spike) {
    pane.getChildren().remove(spike.getView()); // Remove the current view
    pane.getChildren().add(spike.getView()); // Re-add the updated view
}


public void clearSpikesFromGame(Pane pane) {
    for (Spike spike : spikeList) {
        pane.getChildren().remove(spike.getView());
    }
    spikeList.clear();  // Clears the spike list to remove all spikes
}


}