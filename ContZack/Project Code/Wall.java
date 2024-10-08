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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.*;

public class Wall {
   private Rectangle wall;
   // x posi, y posi, x size, y size, color, gamepane
   public Wall(int xPos, int yPos, int sizeX, int sizeY, Color Color, Pane pane) {
      wall = new Rectangle(xPos, yPos, sizeX, sizeY);
      wall.setFill(Color); // for diff levels set the color
      pane.getChildren().add(wall); // auto adds wall to the gamepane
   }
   
   public Rectangle getWall() {
      return wall;
   }
}