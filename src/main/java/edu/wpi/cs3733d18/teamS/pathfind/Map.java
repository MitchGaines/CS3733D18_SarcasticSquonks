package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This Class deals with the Map and related methods.
 *
 * @author Will Lucca
 * @author Mathew Puentes
 * @author Mitch Gaines
 * @version %I%, %G%
 */
public class Map {

    /**
     * Stores the cost for changing floors used in path finding calculations.
     */
    final static int FLOOR_CHANGE_COST = 340; //in pixel cost (approx 12 pixels/second for walking speed)
    /**
     * Stores an ArrayList of floor ids.
     */
    public static ArrayList<String> floor_ids = new ArrayList<>(Arrays.asList("L2", "L1", "1", "2", "3"));
//    public static final int MAP_WIDTH_3D = 5000;
//    public static final int MAP_HEIGHT_3D = 2774;
//    public static final int MAP_WIDTH_2D = 5000;
//    public static final int MAP_HEIGHT_2D = 3400;
    /**
     * Stores the image of the destination.
     */
    private AnchorPane map_anchor_pane;
    /**
     * Stores the number of floors.
     */
    private int floor;
    /**
     * Stores an ArrayList of AStarNodes for the path.
     */
    private Path path;
    private boolean is_3D;

    /**
     * Constructs a 3D map with path drawing capabilities
     *
     * @param floor int
     */
    public Map(AnchorPane map_anchor_pane, int floor, boolean is_3D) {
        this.map_anchor_pane = map_anchor_pane;
        this.floor = floor;
        this.is_3D = is_3D;
    }

    /**
     * getPath
     * Retrieves the path.
     *
     * @return the path of AStarNodes.
     */
    public Path getPath() {
        return path;
    }

    /**
     * setPath
     * Sets the Path, which is composed of and ArrayList of AStarNodes.
     *
     * @param path List of nodes to draw along.
     */
    public void setPath(Path path) {
        this.path = path;
    }

    public int getFloor() {
        return floor;
    }

    /**
     * setFloor
     * Sets the floor of the map.
     *
     * @param floor the floor of the map.
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Clears the Icons from the map.
     */
    public void clearIcons() {
        ArrayList<Node> toRemove = new ArrayList<>();
        for (Node n : map_anchor_pane.getChildren()) {
            if (n.getId().equals("temporaryIcon")) {
                toRemove.add(n);
            }
        }
        map_anchor_pane.getChildren().removeAll(toRemove);
    }

    /**
     * drawPath
     * Draws a path onto of the map image by drawing a line between nodes in the path of the object.
     */
    public void drawPath() {
        if (path == null) {
            return;
        }
        map_anchor_pane.getChildren().addAll(path.generateLinesAndAnts(is_3D, floor));
        map_anchor_pane.getChildren().addAll(path.generateIcons(is_3D, floor));
    }
}