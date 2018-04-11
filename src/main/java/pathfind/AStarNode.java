package pathfind;
import controller.HomePageController;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * AStarNode.java
 * Nodes that have costs and neighbors for a-star calculations
 * @author Noah Hillman
 * @author Mathew Puentes
 * @author Mitch Gaines
 * @version %I%, %G%
 * Date: April 2, 2018
 */
public class AStarNode {
    /**
     * Stores and ArrayList of AStarNodes for the neighboring nodes.
     */
    private ArrayList<AStarNode> neighbors = new ArrayList<AStarNode>();

    /**
     * Stores the parent node of the current node.
     */
    private AStarNode parent = null;

    /**
     * Stores the f_cost which is the sum of the g and h cost.
     */
    private double f_cost;

    /**
     * Stores the heuristic cost.
     */
    private double h_cost;

    /**
     * Stores the goal cost.
     */
    private double g_cost;

    /**
     * Stores the 2D x and y coordinate of the node.
     */
    private int x_coord, y_coord;

    /**
     * Stores the 3D x and y coordinate of the node.
     */
    private int x_coord_3d,y_coord_3d;

    /**
     * Stores the floor the node is on.
     */
    String floor;

    /**
     * Stores the abbreviated name of the node.
     */
    String short_name;

    /**
     * Stores the full official name of the node.
     */
    String long_name;

    /**
     * Stores the id of the node.
     */
    String id;


    /**
     * Constructs an AStarNode by taking in coordinates, a long and short name the floor it is on, an a unique id.
     * @param id String to differentiate the node from others.
     * @param x_coord 2D x coordinate of node.
     * @param y_coord 2D x coordinate of node.
     * @param x_coord_3d 3D x coordinate of node.
     * @param y_coord_3d 3D y coordinate of node.
     * @param short_name The short abbreviated name of the node.
     * @param long_name The long official name of the node.
     * @param floor The floor the node is on.
     */
    public AStarNode(String id, int x_coord, int y_coord, int x_coord_3d, int y_coord_3d, String short_name, String long_name, String floor){
        this.id = id;
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.short_name = short_name;
        this.long_name = long_name;
        this.x_coord_3d = x_coord_3d;
        this.y_coord_3d = y_coord_3d;
        this.floor = floor;
    }

    //TODO write javadoc comments for getters and setters
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

    //returns the euclidean to the given node

    /**
     * Calculates the distance between the nodes.
     * @param next The next node.
     * @return the distance in pixels in between nodes.
     */
    public double distanceTo(AStarNode next){
        double xDist = Math.abs(this.x_coord - next.x_coord);
        double yDist = Math.abs(this.y_coord - next.y_coord);
        return Math.sqrt((Math.pow(xDist,2) + Math.pow(yDist,2)));
    }

    /**
     * Calculates the F cost for the algorithm calculation which is the sum of the G and H cost.
     */
    public void calcFCost(){
        this.f_cost = this.g_cost + this.h_cost;
    }

    /**
     * Calculates the new G cost based on the previous node.
     * @param previous the previous node on the path.
     */
    public void newGCost(AStarNode previous){
        int floor_cost = 0;
        if(!this.floor.equals(previous.floor)){
            int next_floor = Map.floor_ids.indexOf(this.floor);
            int previous_floor = Map.floor_ids.indexOf(previous.floor);

            floor_cost = Math.abs(previous_floor-next_floor)*Map.FLOOR_CHANGE_COST;
        }
        this.g_cost = previous.g_cost + this.distanceTo(previous) + floor_cost;
    }

    /**
     * Calculates the new H Cost.
     * @param goal the destination node.
     */
    public void newHCost(AStarNode goal){
        this.h_cost = this.distanceTo(goal);
    }

    //returns if the id's

    /**
     * Checks the id of an AStarNode and compares it with this one.
     * @param AStarNode1 A different AStarNode to compare to.
     * @return true if the ids equal each other false otherwise.
     */
    public boolean checkID(AStarNode AStarNode1){
        return this.id.equals(AStarNode1.id);
    }


    public AStarNode getParent() {
        return parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public int getXCoord() {
        return x_coord;
    }

    public int getYCoord() {
        return y_coord;
    }

    public String getLongName() {
        return long_name;
    }

    public double getHCost() {
        return h_cost;
    }

    public String getId() {
        return id;
    }
  
    public int getX_coord_3d() {
        return x_coord_3d;
    }

    public int getY_coord_3d() {
        return y_coord_3d;
    }

    /**
     * Returns the ArrayList of the neighboring nodes for an AStarNode.
     * @return the ArrayList of neighboring nodes excluding stairs if the function on the
     * homepage controller is selected.
     */
    public ArrayList<AStarNode> getNeighbors() {
        if(!HomePageController.includeStairs()){
            ArrayList<AStarNode> toRemove = new ArrayList<>();
            for(AStarNode neighbor: this.neighbors){
                if(neighbor.getID().contains("STAI") && !this.floor.equals(neighbor.floor)){
                    toRemove.add(neighbor);
                }
            }
            this.neighbors.removeAll(toRemove);
        }
        return this.neighbors;
    }

    /**
     * Adds a Neighbor to the neighbors ArrayList.
     * @param neighbor the neighboring node.
     */
    public void addNeighbor(AStarNode neighbor){
        this.neighbors.add(neighbor);
    }
}
