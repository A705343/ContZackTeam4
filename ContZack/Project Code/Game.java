import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<GameObject> gameObjects;

    public Game() {
        gameObjects = new ArrayList<>();
        
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(GameObject obj) {
        gameObjects.add(obj);
    }

    public void clearGameObjects() {
        gameObjects.clear();
    }
}