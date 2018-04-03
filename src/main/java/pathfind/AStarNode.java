package pathfind;
import data.Node;
import java.util.ArrayList;
import java.util.HashMap;

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
    String id;

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

    public int getX_coord() {
        return x_coord;
    }

    public int getY_coord() {
        return y_coord;
    }
}
