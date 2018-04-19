package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
    /**
     * Stores the image of the destination.
     */
    private AnchorPane map_anchor_pane;
    /**
     * Stores an ArrayList of AStarNodes for the path.
     */
    public static Path path;

    /**
     * Stores a Boolean for whether or not the requested map needs to be in 3D or not.
     */
    public boolean is_3D;


    /**
     * Constructs a Map by taking in the anchor pane and a Boolean for whether or not it is 3d.
     *
     * @param map_anchor_pane The anchor pane for the scene.
     * @param is_3D           boolean true if the map is 3d, false otherwise.
     */
    public Map(AnchorPane map_anchor_pane, boolean is_3D) {
        this.map_anchor_pane = map_anchor_pane;
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
     * Returns an ArrayList of Nodes for the Next segment of the path, which occurs whenever there is a floor change.
     *
     * @param is_3D True if the map is in 3d mode, false if in 2D.
     * @return The Nodes for the Previous segment of the path.
     */
    public ArrayList<Node> nextStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.nextSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }

    /**
     * Returns an ArrayList of Nodes for the Previous segment of the path, which occurs whenever there is a floor change.
     *
     * @param is_3D True if the map is in 3d mode, false if in 2D.
     * @return The Nodes for the Previous segment of the path.
     */
    public ArrayList<Node> prevStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.prevSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }

    /**
     * Returns an ArrayList of Nodes for the current segment of the path, which occurs whenever there is a floor change,
     * this function is primarily used when changing from 2D to 3D map view or vice versa.
     *
     * @param is_3D True if the map is in 3d mode, false if in 2D.
     * @return The Nodes for the current segment of the path.
     */
    public ArrayList<Node> thisStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.thisSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }
}