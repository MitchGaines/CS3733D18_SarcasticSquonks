package edu.wpi.cs3733d18.teamS.pathfind;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The Class manages the AStar path algorithm and primarily finds the optimal path with the lowest search time
 * based on heuristics.
 *
 * @author Noah Hillman
 * @version %I%, %G%
 */
public class AStar extends SearchAlgorithm {

    @Override
    int prioritySort(AStarNode node1, AStarNode node2) {
        return (int)(node1.getFCost() - node2.getFCost());
    }

    @Override
    void updateCosts(AStarNode neighbor, AStarNode current, AStarNode goal) {
        neighbor.setParent(current);
        neighbor.newGCost(neighbor);
        neighbor.newHCost(goal);
        neighbor.calcFCost();
    }
}
