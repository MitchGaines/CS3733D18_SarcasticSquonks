package edu.wpi.cs3733d18.teamS.pathfind;

import java.util.HashMap;

/**
 *
 * The Interface that helps differentiate the different types of path finding algorithms.
 * @author Noah Hillman
 * @version %I%, %G%
 */
public interface ISearchAlgorithm  {

    public AStarNode findPath(String start, String goal, HashMap<String, AStarNode> algorithm_node_map);
}
