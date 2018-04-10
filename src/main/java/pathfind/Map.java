package pathfind;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public abstract class Map {
    public static String[] floor_ids = {"L2", "L1", "1", "2", "3"};
    ImageView map;
    Polyline path_polyline;
    Polyline path_polyline_2;
    ImageView destination_img;
    ScrollPane scroll_pane;
    int floor;
    ArrayList<AStarNode> path;

    /**
     * Draws path on map
     * @param path List of nodes to draw along
     * @author Will Lucca
     */
    public void drawPath(ArrayList<AStarNode> path) {
        this.path = path;
        drawPath();
    }

    public void setPath(ArrayList<AStarNode> path){
        this.path = path;
    }

    public ArrayList<AStarNode> getPath(){
        return path;
    }

    public void setFloor(int floor){
        this.floor = floor;
    }

    public abstract void drawPath();
}