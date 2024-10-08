import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Button extends GameObject {
    private boolean activated;
    private boolean dimmed;  // New field to track if the button is dimmed
    private Color color;
    private Rectangle shape; // For rectangular buttons
    private Circle circularShape; // For circular buttons
    private Pane gamePane;
    private Spikes spikes;
    private String spikeType;
    private boolean hasSpikeSwapping;  // Flag to check if spike swapping is enabled
    


    // Constructor for a basic button (rectangular, no spike swapping)
    public Button(double x, double y, Color color) {
        super(x, y);
        this.color = color;
        this.activated = false;
        this.dimmed = false;  // Initialize dimmed to false
        this.shape = new Rectangle(x, y, 50, 50);
        shape.setFill(color);
        this.hasSpikeSwapping = false;  // No spike swapping for this button
    }

    // Constructor for a basic circular button
    public Button(double x, double y, Color color, double radius, Pane gamePane, Spikes spikes, String spikeType) {
        super(x, y);
        this.color = color;
        this.activated = false;
        this.dimmed = false;  // Initialize dimmed to false
        this.circularShape = new Circle(radius);
        this.spikes = new Spikes(gamePane);
        this.gamePane = gamePane;
        circularShape.setFill(color);
        circularShape.setCenterX(x + radius); // Center X of the circle
        circularShape.setCenterY(y + radius); // Center Y of the circle
        this.spikeType = spikeType;
        this.hasSpikeSwapping = true;  // No spike swapping for this button
            }

    // Constructor for buttons that perform spike swapping (rectangular)
    public Button(double x, double y, Color color, Pane gamePane, Spikes spikes, String spikeType) {
        super(x, y);
        this.color = color;
        this.activated = false;
        this.dimmed = false;  // Initialize dimmed to false
        this.shape = new Rectangle(x, y, 50, 50);
        shape.setFill(color);
        this.gamePane = gamePane;
        this.spikes = new Spikes(gamePane);
        this.spikeType = spikeType;
        this.hasSpikeSwapping = true;  // Spike swapping is enabled for this button
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Draw rectangular button
        if (shape != null) {
            gc.setFill(activated ? Color.GREEN : (dimmed ? Color.GRAY : color));  // Dimmed button is gray
            gc.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
        }
        
        // Draw circular button
        if (circularShape != null) {
            gc.setFill(activated ? Color.GREEN : (dimmed ? Color.GRAY : color));  // Dimmed button is gray
            gc.fillOval(circularShape.getCenterX() - circularShape.getRadius(), 
                        circularShape.getCenterY() - circularShape.getRadius(), 
                        circularShape.getRadius() * 2, 
                        circularShape.getRadius() * 2);
        }
    }

    // Activate the button (swap spikes)
    public void activate() {
        if (!activated) {
            activated = true;
            System.out.println("Button activated!");

            // Dimming the button
            dimmed = true;  // Set the dim state
            updateOpacity();
            
            // If the button is linked to spikes, swap the spikes
            if (hasSpikeSwapping) {
                swapSpikes();  // Pass the spike type to swapSpikes
            }
     
        }
    }
    
    public void deactivate() {
    // Logic to deactivate the button
    activated = false; // Assuming `activated` is a boolean field in your Button class
    // Add any visual feedback for deactivation, if necessary
     // Dimming the button
            dimmed = false;  // Set the dim state
            updateOpacity();
            
            // If the button is linked to spikes, swap the spikes
            if (hasSpikeSwapping) {
                swapSpikes();  // Pass the spike type to swapSpikes
            }
            
}

   


    // Set the button to dimmed state and update its opacity
    public void setDimmed(boolean dimmed) {
        this.dimmed = dimmed;
        updateOpacity();
    }

    // Check if the button is dimmed
    public boolean isDimmed() {
        return dimmed;
    }

    // Update the opacity of the button shapes based on their state
    private void updateOpacity() {
        Platform.runLater(() -> {
            if (shape != null) {
                shape.setOpacity(dimmed ? 0.2 : 1.0);  // 0.2 opacity when dimmed
            }
            if (circularShape != null) {
                circularShape.setOpacity(dimmed ? 0.2 : 1.0);  // 0.2 opacity when dimmed
            }
        });
    }

    private void swapSpikes() {
    System.out.println("Swapping spikes!");
    List<Spikes.Spike> allSpikes = spikes.getAllSpikes();
    System.out.println("Number of spikes: " + allSpikes.size());

    for (Spikes.Spike spike : allSpikes) {
        if (spike.getType().equalsIgnoreCase(this.spikeType) || spike.getType().equalsIgnoreCase(this.spikeType + "_vert")) {
            spikes.toggleSpikeState(spike);
            System.out.println("Toggling Spike: " + spike.getType() + ", Current State: " + spike.isSpikeUp() + ", New State: " + !spike.isSpikeUp());
        
           if (circularShape != null) {  // Check if it's a circular button and the spike
               System.out.println("timer");
               if(!spike.isSpikeUp()) {
               System.out.println("moo");
               Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10000), event -> {
               // Toggle the spike back up after 10 seconds (without checking initially)
               spikes.toggleSpikeState(spike);
               System.out.println("Spike reset: " + spike.getType() + " is now up.");
            }));
    
            // Set the timeline to play once
            timeline.setCycleCount(1);
    
            // Start the timeline
            timeline.play();
            }
           }
        }
    }
}

   public void setClip(Shape clipShape) {
        shape.setClip(clipShape);  // Assuming buttonShape is a JavaFX Node
    }

    public boolean isActivated() {
        return activated;
    }

    // Getters for shapes
    public Rectangle getShape() {
        return shape;  // Only return the shape if it's not activated
    }

    public Circle getCircularShape() {
        return circularShape;  // Only return the circular shape if it's not activated
    }
}