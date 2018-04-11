package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * This abstract Class deals with the Map and related methods.
 * @author Will Lucca
 * @author Mathew Puentes
 * @author Mitch Gaines
 * @version %I%, %G%
 */
public abstract class Map {

    /**
     * Stores an ArrayList of floor ids.
     */
    public static ArrayList<String> floor_ids = new ArrayList<>(Arrays.asList(new String[]{"L2", "L1", "1", "2", "3"}));
//    public static String[] floor_ids = {"L2", "L1", "1", "2", "3"};

    /**
     * Stores the cost for changing floors used in path finding calculations.
     */
    public final static int FLOOR_CHANGE_COST = 340; //in pixel cost (approx 12 pixels/second for walking speed)


    /**
     * Stores the Map image.
     */
    ImageView map;

    /**
     * Stores a polyline for the path.
     */
    Polyline path_polyline;

    /**
     * Stores a polyline for the path
     */
    Polyline path_polyline_2;

    /**
     * Stores the image of the destination.
     */
    ImageView destination_img;

    /**
     * Stores the anchor pane for the map.
     */
    AnchorPane map_anchor_pane;

    /**
     * Stores the scroll plane.
     */
    ScrollPane scroll_pane;

    /**
     * Stores the number of floors.
     */
    int floor;

    /**
     * Stores an ArrayList of AStarNodes for the path.
     */
    ArrayList<AStarNode> path;

    /**
     * drawPath
     * Draws path on map by taking in the the ArrayList of AStarNodes.
     * @param path List of nodes to draw along.
     */
    public void drawPath(ArrayList<AStarNode> path) {
        this.path = path;
        drawPath();
    }

    /**
     * setPath
     * Sets the Path, which is composed of and ArrayList of AStarNodes.
     * @param path List of nodes to draw along.
     */
    public void setPath(ArrayList<AStarNode> path){
        this.path = path;
    }

    /**
     * getPath
     * Retrieves the path.
     * @return the path of AStarNodes.
     */
    public ArrayList<AStarNode> getPath(){
        return path;
    }

    /**
     * setFloor
     * Sets the floor of the map.
     * @param floor the floor of the map.
     */
    public void setFloor(int floor){
        this.floor = floor;
    }

    public int getFloor() {return floor;}

    public abstract void drawPath();


    /**
     * addIcon
     * Takes in the x coordinate, the y coordinate, and image path and adds an icon in the specific location.
     * @param xcoord the x coordinate for where the icon is located.
     * @param ycoord the y coordinate for where the icon is located.
     * @param path the path of the image.
     */
    public void addIcon(double xcoord, double ycoord, String path){
        ImageView pin = new ImageView(path);
        pin.setX(xcoord - (pin.getImage().getWidth() / 2));
        pin.setY(ycoord - (pin.getImage().getHeight() / 2));

        pin.setId("temporaryIcon");
        map_anchor_pane.getChildren().add(pin);
    }

    /**
     * Clears the Icons from the map.
     */
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