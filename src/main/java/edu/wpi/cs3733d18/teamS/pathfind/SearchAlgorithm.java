package edu.wpi.cs3733d18.teamS.pathfind;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The Abstract class that holds common functions for the different types of path finding algorithms.
 *
 * @author Noah Hillman
 * @version %I%, %G%
 */
public abstract class SearchAlgorithm {

    /**
     * finds path using a priority queue of unsearched nodes, each algorithm sorts and compares the priority queue
     * differently
     *
     * @param start_id
     * @param goal_id
     * @param algorithm_node_map
     * @return
     */
    AStarNode findPath(String start_id, String goal_id, HashMap<String, AStarNode> algorithm_node_map) {
        AStarNode start = algorithm_node_map.get(start_id);
        AStarNode goal = algorithm_node_map.get(goal_id);

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
                return current_node;
            }
            //loop over the current_node node's neighbors
            for (AStarNode neighbor : current_node.getNeighbors()) {
                //make sure they haven't been searched before
                if (!closed_a_star_nodes.contains(neighbor)) {
                    //check if they haven't been discovered before
                    if (!open_a_star_nodes.contains(neighbor)) {
                        //add them to the open set of nodes and update parent, g, h, and f costs
                        updateCosts(neighbor, current_node, goal);
                        open_a_star_nodes.add(neighbor);
                    } else {
                        //check if they are a better pathfinder_path (lower gcost), if so, update their cost and parent
                        double temp_g_cost = current_node.getGCost() + current_node.distanceTo(neighbor);
                        if (temp_g_cost < neighbor.getGCost()) {
                            updateCosts(neighbor, current_node, goal);
                        }
                    }
                }
            }
        }
        return start;
    }

    /**
     * Updates costs for nodes, which may later be used to sort/compare best nodes
     *
     * @param neighbor
     * @param current
     * @param goal
     */
    abstract void updateCosts(AStarNode neighbor, AStarNode current, AStarNode goal);

    /**
     * comparator for priority queue sorting method
     */
    Comparator<AStarNode> heuristicComparator = (AStarNode1, AStarNode2) ->
            (int) (prioritySort(AStarNode1, AStarNode2));

    abstract int prioritySort(AStarNode node1, AStarNode node2);
}
