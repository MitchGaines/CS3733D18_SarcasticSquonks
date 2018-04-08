package pathfind;

import org.apache.commons.codec.binary.Base64;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Path.java
 * Holds path information from A-star and calculates text directions
 * Author: Noah Hillman, Mitch Gaines
 * Date: April 2, 2018
 */

public class Path {
    private static final double FEET_PER_PIXEL = .323;
    private static final int MIN_TURN_ANGLE = 30;
    ArrayList<AStarNode> a_star_node_path = new ArrayList<>();

    public Path(){ }


    /**
     * Once path is generated, will generate text descriptions for turns based on the path nodes
     *
     * Logically, this only vaguely makes sense if you ignore basic logic behind programming,
     * but it seems to work so I'm rolling with it. Over and out.
     *
     * @return URL based on calculated turns at each node
     */
    public URL getPathDirections() throws MalformedURLException {
        String host = "159.203.189.146";
        int port = 3000;
        String protocol = "http";
        String language = "en"; //TODO add multilangauge support
        String path  = "/" + language + "/";

        if(this.a_star_node_path.size() == 0){
            return new URL(protocol, host, port, path);
        }
        String path_description = "";
        path_description += "Directions from: " + a_star_node_path.get(0).getLongName()
                + " to: " + a_star_node_path.get(a_star_node_path.size()-1).getLongName() + "."
                + System.lineSeparator();

        path_description += "First, begin walking towards " + a_star_node_path.get(1).short_name + "."
                + System.lineSeparator();
        for (int i = 1; i < (a_star_node_path.size()-1); i++) {
            int distance = calcDistance(a_star_node_path.get(i-1), a_star_node_path.get(i));

            while(i + 1 < a_star_node_path.size() - 1
                    && calcTurnDir(a_star_node_path.get(i), a_star_node_path.get(i + 1)) == 'S'){
                distance += calcDistance(a_star_node_path.get(i), a_star_node_path.get(i + 1));
                i++;
            }

            path_description += "In " + distance + " feet, "
                    + calcTurn(a_star_node_path.get(i),a_star_node_path.get(i+1))
                    + "." + System.lineSeparator();
        }
        path_description += "Destination is "
                + calcDistance(a_star_node_path.get(a_star_node_path.size()-2), a_star_node_path.get(a_star_node_path.size()-1))
                + " feet ahead.";

        byte[] path_bytes = path_description.getBytes();
        String enc_bytes = Base64.encodeBase64String(path_bytes);
        path += enc_bytes;

        return new URL(protocol, host, port, path);
    }

    private int calcDistance(AStarNode current_node, AStarNode next_node){
        return (int)(current_node.distanceTo(next_node)* FEET_PER_PIXEL);
    }

    /**
     * calcTurn
     * returns a string for the turning directing based on where the user is walking from
     * @param current_node node where the turn occurs
     * @param next_node node where the turn will lead
     * @return string for the turning direction: turn left, turn right, continue straight
     */
    private char calcTurnDir(AStarNode current_node, AStarNode next_node){
        int previous_x = current_node.getParent().getXCoord();
        int previous_y = current_node.getParent().getYCoord();
        int current_x = current_node.getXCoord();
        int current_y = current_node.getYCoord();
        int next_x = next_node.getXCoord();
        int next_y = next_node.getYCoord();

        double previous_angle = angle(previous_x, previous_y, current_x, current_y);
        double new_angle = angle(current_x, current_y, next_x, next_y);
        double angle = new_angle-previous_angle;

        if(angle <= -MIN_TURN_ANGLE){
            return 'L';
        } else if(angle >= MIN_TURN_ANGLE){
            return 'R';
        } else {
            return 'S';
        }
    }

    private String calcTurn(AStarNode current_node, AStarNode next_node){
        int previous_x = current_node.getParent().getXCoord();
        int previous_y = current_node.getParent().getYCoord();
        int current_x = current_node.getXCoord();
        int current_y = current_node.getYCoord();
        int next_x = next_node.getXCoord();
        int next_y = next_node.getYCoord();

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
        return a_star_node_path;
    }

    private double angle(int x1, int y1, int x2, int y2){
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }
}
