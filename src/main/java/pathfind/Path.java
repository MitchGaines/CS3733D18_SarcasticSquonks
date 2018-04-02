package pathfind;

import java.util.ArrayList;

public class Path {
    ArrayList<AStarNode> a_star_node_path = new ArrayList<>();
    /*
    put more attributes here later:
    total distance, elevators used, etc...
     */
    public Path(){
    }


    public String getPathDirections(){
        String path = "";
        path += "path from: " + a_star_node_path.get(0).getShortName() + " to: " + a_star_node_path.get(a_star_node_path.size()-1).getShortName() + System.lineSeparator();
        //TODO add path descriptions
        return path;
    }

    public String calcTurn(AStarNode current_node, AStarNode next_node){
        int previous_x = current_node.getParent().getX_coord();
        int previous_y = current_node.getParent().getY_coord();
        int current_x = current_node.getX_coord();
        int current_y = current_node.getY_coord();
        int next_x = next_node.getX_coord();
        int next_y = next_node.getY_coord();

        double start_angle = 45;
        double new_angle = 30;
        double angle = new_angle-start_angle;
        if(angle <= -30){
            return "turn left";
        } else if(angle >= 30){
            return "turn right";
        } else {
            return "continue";
        }
    }
}
