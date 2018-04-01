package pathfind;

import java.util.HashMap;

public class AStarNode {
    public HashMap<String, AStarNode> neighbors = new HashMap<>();
    private AStarNode parent = null;
    private double f_cost;
    private double h_cost;
    private double g_cost;
    private int x_coord, y_coord;
    String id;

    public AStarNode(String id, int xcoord, int ycoord){
        this.id = id;
        this.x_coord = xcoord;
        this.y_coord = ycoord;
    }

    //getters
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

}
