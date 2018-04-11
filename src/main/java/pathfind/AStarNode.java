package pathfind;
import controller.HomePageController;

import java.util.ArrayList;

/**
 * AStarNode.java
 * Nodes that have costs and neighbors for a-star calculations
 * Author: Noah Hillman
 * Date: April 2, 2018
 */

public class AStarNode {
    private ArrayList<AStarNode> neighbors = new ArrayList<AStarNode>();
    private AStarNode parent = null;
    private double f_cost;
    private double h_cost;
    private double g_cost;
    private int x_coord, y_coord;
    private int x_coord_3d,y_coord_3d;
    String floor;
    String short_name;
    String long_name;
    String id;


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
    public double distanceTo(AStarNode next){
        double xDist = Math.abs(this.x_coord - next.x_coord);
        double yDist = Math.abs(this.y_coord - next.y_coord);
        return Math.sqrt((Math.pow(xDist,2) + Math.pow(yDist,2)));
    }

    public void calcFCost(){
        this.f_cost = this.g_cost + this.h_cost;
    }

    public void newGCost(AStarNode previous){
        int floor_cost = 0;
        if(!this.floor.equals(previous.floor)){
            int previous_floor = 0;
            int next_floor = 0;
            for(int i=0; i<Map.floor_ids.length; i++){
                if(Map.floor_ids[i].equals(this.floor)){
                    next_floor = i;
                    break;
                }
            }
            for(int i=0; i<Map.floor_ids.length; i++){
                if(Map.floor_ids[i].equals(previous.floor)){
                    previous_floor = i;
                    break;
                }
            }
            floor_cost = Math.abs(previous_floor-next_floor)*Map.FLOOR_CHANGE_COST;
        }
        this.g_cost = previous.g_cost + this.distanceTo(previous) + floor_cost;
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

    public ArrayList<AStarNode> getNeighbors() {
        if(!HomePageController.includeStairs()){
            for(AStarNode neighbor: this.neighbors){
                if(neighbor.getID().contains("STAI") && !this.floor.equals(neighbor.floor)){
                    this.neighbors.remove(neighbor);
                }
            }
        }
        return this.neighbors;
    }

    public void addNeighbor(AStarNode neighbor){
        this.neighbors.add(neighbor);
    }
}
