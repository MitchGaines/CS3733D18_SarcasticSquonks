package pathfind;

import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Map {
    public static ArrayList<String> floor_ids = new ArrayList<>(Arrays.asList(new String[]{"L2", "L1", "1", "2", "3"}));
//    public static String[] floor_ids = {"L2", "L1", "1", "2", "3"};
    public final static int FLOOR_CHANGE_COST = 340; //in pixel cost (approx 12 pixels/second for walking speed)

    ImageView map;
    Polyline path_polyline;
    Polyline path_polyline_2;
    ImageView destination_img;
    AnchorPane map_anchor_pane;
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

    public int getFloor() {return floor;}

    public abstract void drawPath();


    public void addIcon(double xcoord, double ycoord, String path){
        ImageView pin = new ImageView(path);
        pin.setX(xcoord - (pin.getImage().getWidth() / 2));
        pin.setY(ycoord - (pin.getImage().getHeight() / 2));

        pin.setId("temporaryIcon");
        map_anchor_pane.getChildren().add(pin);
    }

    public void clearIcons(){
        ArrayList<Node> toRemove = new ArrayList<>();
        for(Node n : map_anchor_pane.getChildren()){
            if(n.getId().equals("temporaryIcon")){
                toRemove.add(n);
            }
        }
        map_anchor_pane.getChildren().removeAll(toRemove);
    }
}