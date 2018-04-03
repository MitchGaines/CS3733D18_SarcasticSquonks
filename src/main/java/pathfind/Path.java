package pathfind;

import java.util.ArrayList;

/**
 * Path.java
 * Holds path information from A-star and calculates text directions
 * Author: Noah Hillman
 * Date: April 2, 2018
 */

public class Path {
    ArrayList<AStarNode> a_star_node_path = new ArrayList<>();

    public Path(){
    }

    public String getPathDirections(){
        String path = "";
        path += "path from: " + a_star_node_path.get(0).getShortName() + " to: " + a_star_node_path.get(a_star_node_path.size()-1).getShortName() + System.lineSeparator();
        for (int i = 1; i < (a_star_node_path.size()-1) ; i++) {
        }
        //TODO add path descriptions
        return path;
    }

    private String calcTurn(AStarNode current_node, AStarNode next_node){
        int previous_x = current_node.getParent().getX_coord();
        int previous_y = current_node.getParent().getY_coord();
        int current_x = current_node.getX_coord();
        int current_y = current_node.getY_coord();
        int next_x = next_node.getX_coord();
        int next_y = next_node.getY_coord();

        double previous_angle = angle(previous_x, previous_y, current_x, current_y);
        double new_angle = angle(current_x, current_y, next_x, next_y);
        double angle = new_angle-previous_angle;
        if(angle <= -30){
            return "turn left";
        } else if(angle >= 30){
            return "turn right";
        } else {
            return "continue straight";
        }
    }

    public ArrayList<AStarNode> getAStarNodePath() {
        return a_star_node_path;
    }

    private double angle(int x1, int y1, int x2, int y2){
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }
}
