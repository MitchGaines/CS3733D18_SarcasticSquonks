package pathfind;

import java.util.ArrayList;

/**
 * Path.java
 * Holds pathfinder_path information from A-star and calculates text directions
 * Author: Noah Hillman
 * Date: April 2, 2018
 */

public class Path {
    private static final double FEET_PER_PIXEL = .323;
    private static final int MIN_TURN_ANGLE = 30;
    ArrayList<AStarNode> algorithm_node_path = new ArrayList<>();

    public Path(){

    }

    public void setAlgorithm_node_path(ArrayList<AStarNode> algorithm_node_path) {
        this.algorithm_node_path = algorithm_node_path;
    }



    /**
     * Once pathfinder_path is generated, will generate text descriptions for turns based on the pathfinder_path nodes
     *
     * @return String based on calculated turns at each node
     */
    public String getPathDirections() {
        if(this.algorithm_node_path.size() == 0){
            return "No pathfinder_path found";
        }
        String path_description = "";
        path_description += "Directions from: " + algorithm_node_path.get(0).getShortName() + " to: " + algorithm_node_path.get(algorithm_node_path.size()-1).getShortName() + System.lineSeparator();
        path_description += "First, begin walking towards " + algorithm_node_path.get(1).short_name + System.lineSeparator();
        for (int i = 1; i < (algorithm_node_path.size()-1) ; i++) {
            path_description += "In " + calcDist(algorithm_node_path.get(i-1), algorithm_node_path.get(i)) + ", " + calcTurn(algorithm_node_path.get(i), algorithm_node_path.get(i+1)) + "." + System.lineSeparator();
        }
        path_description += "Destination is " + calcDist(algorithm_node_path.get(algorithm_node_path.size()-2), algorithm_node_path.get(algorithm_node_path.size()-1)) + " ahead.";
        return path_description;
    }

    private String calcDist(AStarNode current_node, AStarNode next_node){
        return (String.valueOf((int)(current_node.distanceTo(next_node)* FEET_PER_PIXEL)) + " feet");
    }

    /**
     * calcTurn
     * returns a string for the turning directing based on where the user is walking from
     * @param current_node node where the turn occurs
     * @param next_node node where the turn will lead
     * @return string for the turning direction: turn left, turn right, continue straight
     */
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
        if(angle <= -MIN_TURN_ANGLE){
            return "turn left";
        } else if(angle >= MIN_TURN_ANGLE){
            return "turn right";
        } else {
            return "continue straight";
        }
    }

    public ArrayList<AStarNode> getAStarNodePath() {
        return algorithm_node_path;
    }

    private double angle(int x1, int y1, int x2, int y2){
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }
}
