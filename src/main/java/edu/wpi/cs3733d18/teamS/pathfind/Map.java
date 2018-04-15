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
     * Stores the cost for changing floors used in seg_path finding calculations.
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
     * Stores an ArrayList of AStarNodes for the seg_path.
     */
    private Path path;
    public boolean is_3D;


    public Map(AnchorPane map_anchor_pane, boolean is_3D) {
        this.map_anchor_pane = map_anchor_pane;
        this.is_3D = is_3D;
    }

    /**
     * getPath
     * Retrieves the seg_path.
     *
     * @return the seg_path of AStarNodes.
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


    public ArrayList<Node> nextStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.nextSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }

    public ArrayList<Node> prevStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.prevSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }

    public ArrayList<Node> thisStep(boolean is_3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        fx_nodes.addAll(path.thisSeg(is_3D));
        fx_nodes.addAll(path.genIcons(is_3D));
        return fx_nodes;
    }
}