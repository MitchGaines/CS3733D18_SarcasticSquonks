package pathfind;
import data.Node;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that manages Nodes that have costs and neighbors for a-star calculations
 * @author Noah Hillman
 * Date: April 2, 2018
 *
 */
public class AStarNode {
    /**
     * Stores neighboring nodes in an ArrayList.
     */
    public ArrayList<AStarNode> neighbors = new ArrayList<AStarNode>();

    /**
     * Stores the parent node.
     */
    private AStarNode parent = null;

    /**
     * Stores the f_cost, (g_cost + h_cost).
     */
    private double f_cost;

    /**
     * Stores the h_cost, estimated cost to get to goal.
     */
    private double h_cost;

    /**
     * Stores the g_cost, estimated cost to node.
     */
    private double g_cost;

    /**
     * stores the x and y coordinates.
     */
    private int x_coord, y_coord;

    /**
     * Stores the abbreviated node name.
     */
    String short_name;

    /**
     * Stores the node id.
     */
    String id;

    /**
     * Constructs and AStarNode using the id, x and y coordinate and the abbreviated name.
     * @param id the node id.
     * @param xcoord the x coordinate.
     * @param ycoord the y coordinate.
     * @param short_name the abbreviated name.
     */
    public AStarNode(String id, int xcoord, int ycoord, String short_name){
        this.id = id;
        this.x_coord = xcoord;
        this.y_coord = ycoord;
        this.short_name = short_name;
    }

    //getters
    public String getShortName(){
        return this.short_name;
    }
    public double getGCost(){
        return this.g_cost;
    }

    public String getID(){
        return this.id;
    }

    public double getFCost() {
        this.f_cost = this.g_cost + this.h_cost;
        return f_cost;
    }

    public AStarNode getParent(){
        return this.parent;
    }

    //setters
    public void setParent(AStarNode AStarNode){
        this.parent = AStarNode;
    }

    //returns the euclidean to the given node

    /**
     * Retrieves the Euclidean to to the given node.
     * @param next the next node.
     * @return the euclidean distance to the next node.
     */
    public double distanceTo(AStarNode next){
        double xDist = Math.abs(this.x_coord - next.x_coord);
        double yDist = Math.abs(this.y_coord - next.y_coord);
        return Math.sqrt((Math.pow(xDist,2) + Math.pow(yDist,2)));
    }

    /**
     * Calculates the f_cost.
     */
    public void calcFCost(){
        this.f_cost = this.g_cost + this.h_cost;
    }

    /**
     * Calculates the new g_cost from the next node.
     * @param previous the previous AStrarNode
     */
    public void newGCost(AStarNode previous){
        this.g_cost = previous.g_cost + this.distanceTo(previous);
    }

    /**
     * Calculats the new h_cost.
     * @param goal the final destination.
     */
    public void newHCost(AStarNode goal){
        this.h_cost = this.distanceTo(goal);
    }

    //returns if the id's

    /**
     * Checks the two AStarNodes and returns true if the id's are the same.
     * @param AStarNode1 the new node.
     * @return a boolean value, true if the ids match,
     *                          false otherwise.
     */
    public boolean checkID(AStarNode AStarNode1){
        return this.id.equals(AStarNode1.id);
    }

    public int getX_coord() {
        return x_coord;
    }

    public int getY_coord() {
        return y_coord;
    }
}
