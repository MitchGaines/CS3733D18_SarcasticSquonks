package pathfind;

import java.util.HashMap;

public interface ISearchAlgorithm  {

    public AStarNode findPath(String start, String goal, HashMap<String, AStarNode> algorithm_node_map);
}
