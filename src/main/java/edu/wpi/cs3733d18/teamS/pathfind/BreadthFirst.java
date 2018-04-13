package edu.wpi.cs3733d18.teamS.pathfind;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The Class manages the Breadth First path algorithm and primarily finds the path from a location.
 *
 * @author Noah Hillman
 * @version %I%, %G%
 */
public class BreadthFirst implements ISearchAlgorithm {

    private static Comparator<AStarNode> heuristicComparator = (AStarNode1, AStarNode2) -> (int) (AStarNode1.getGCost() - AStarNode2.getGCost());

    /**
     * findPath (Breadth First edition)
     * Finds the pathway between two points using the Breadth First algorithm, This takes in a starting node's id, and end
     * locations id and takes in a HashMap that contains all the nodes of the hospital.
     *
     * @param start_id           The String Id of the node that the person is starting at.
     * @param goal_id            The String Id of the node that the person wants to get to.
     * @param algorithm_node_map The HashMap of all the nodes within the hospital.
     * @return Returns an ArrayList of AStarNodes that results in the pathway.
     */
    @Override
    public AStarNode findPath(String start_id, String goal_id, HashMap<String, AStarNode> algorithm_node_map) {
        AStarNode start = algorithm_node_map.get(start_id);

        //queue of all Nodes generated but not yet searched (queued in order of increasing cost)
        PriorityQueue<AStarNode> open_a_star_nodes = new PriorityQueue<>(10, heuristicComparator);
        //list of searched nodes that shouldn't be searched again
        LinkedList<AStarNode> closed_a_star_nodes = new LinkedList<>();
        //search the starting node first
        open_a_star_nodes.add(start);

        //run until there are no more searchable nodes (or program finds a pathfinder_path)
        while (!open_a_star_nodes.isEmpty()) {
            //take the lowest f score off the priority queue
            AStarNode current_node = open_a_star_nodes.poll();
            //add the current_node to closed list
            closed_a_star_nodes.add(current_node);

            if (current_node.getID().contains(goal_id)) {
                //System.out.println(closed_a_star_nodes.size() + " searched nodes");
                return current_node;
            }
            //loop over the current_node node's neighbors
            for (AStarNode neighbor : current_node.getNeighbors()) {
                //make sure they haven't been searched before
                if (!closed_a_star_nodes.contains(neighbor)) {
                    //check if they haven't been discovered before
                    if (!open_a_star_nodes.contains(neighbor)) {
                        //add them to the open set of nodes and update parent, g, h, and f costs
                        neighbor.setParent(current_node);
                        neighbor.newGCost(current_node);
                        open_a_star_nodes.add(neighbor);
                    } else {
                        //check if they are a better pathfinder_path (lower gcost), if so, update their cost and parent
                        double temp_g_cost = current_node.getGCost() + current_node.distanceTo(neighbor);
                        if (temp_g_cost < neighbor.getGCost()) {
                            neighbor.setParent(current_node);
                            neighbor.newGCost(current_node);
                        }
                    }
                }
            }
        }
        return start;
    }
}
