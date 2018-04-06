package pathfind;
import java.util.ArrayList;

/**
 * AStarNode.java
 * Nodes that have costs and neighbors for a-star calculations
 * Author: Noah Hillman
 * Date: April 2, 2018
 */

public class AStarNode {
    public ArrayList<AStarNode> neighbors = new ArrayList<AStarNode>();
    private AStarNode parent = null;
    private double f_cost;
    private double h_cost;
    private double g_cost;
    private int x_coord, y_coord;
    String short_name;
    String long_name;
    String id;

    public AStarNode(String id, int xcoord, int ycoord, String short_name, String long_name){
        this.id = id;
        this.x_coord = xcoord;
        this.y_coord = ycoord;
        this.short_name = short_name;
        this.long_name = long_name;
    }

    public double getFCost() {
        this.f_cost = this.g_cost + this.h_cost;
        return f_cost;
    }

    //returns the euclidean to the given node
    public double distanceTo(AStarNode next){
        double xDist = Math.abs(this.x_coord - next.x_coord);
        double yDist = Math.abs(this.y_coord - next.y_coord);
        return Math.sqrt((Math.pow(xDist,2) + Math.pow(yDist,2)));
    }

    public void calcFCost(){
        this.f_cost = this.g_cost + this.h_cost;
    }

    public void newGCost(AStarNode previous){
        this.g_cost = previous.g_cost + this.distanceTo(previous);
    }

    public void newHCost(AStarNode goal){
        this.h_cost = this.distanceTo(goal);
    }

    //returns if the id's
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

    public String getShortName() {
        return short_name;
    }

    public String getLongName() {
        return long_name;
    }


    public double getHCost() {
        return h_cost;
    }

    public double getGCost() {
        return g_cost;
    }

    public String getId() {
        return id;
    }
}
