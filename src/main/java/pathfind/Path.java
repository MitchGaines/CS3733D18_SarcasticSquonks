package pathfind;

import java.util.ArrayList;

public class Path {
    ArrayList<AStarNode> node_path = new ArrayList<>();
    /*
    put more attributes here later:
    total distance, elevators used, etc...
     */
    public Path(){
    }

    public ArrayList<AStarNode> getNode_path() {
        return node_path;
    }
}
