package pathfind;

import java.util.ArrayList;

/**
 * Holds path information from A-star and calculates text directions
 * @author Noah Hillman
 * @version %I%, %G%
 * Date: April 2, 2018
 */
public class Path {
    /**
     * Stores the AStar calculated pathway.
     */
    ArrayList<AStarNode> a_star_node_path = new ArrayList<>();

    /**
     * initializes the path.
     */
    public Path(){
    }

    /**
     * Retrieves the pathway directions.
     * @return the directions to the destination.
     */
    public String getPathDirections(){
        String path = "";
        path += "path from: " + a_star_node_path.get(0).getShortName() + " to: " + a_star_node_path.get(a_star_node_path.size()-1).getShortName() + System.lineSeparator();
        for (int i = 1; i < (a_star_node_path.size()-1) ; i++) {
        }
        //TODO add path descriptions
        return path;
    }

    /**
     * Calculates whether or not the pathway turn left right or continue straight.
     * @param current_node the current node location.
     * @param next_node the next node location.
     * @return turn left if the angle is less than -30 degrees
     *         right if greater than 30, straight otherwise.
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

    /**
     * Calculates the angle between two coordinates.
     * @param x1 x coordinate of the first node.
     * @param y1 y coordinate of the first node
     * @param x2 x coordinate of the second node.
     * @param y2 y coordinate of the second node
     * @return the angle between the two nodes.
     */
    private double angle(int x1, int y1, int x2, int y2){
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }
}
