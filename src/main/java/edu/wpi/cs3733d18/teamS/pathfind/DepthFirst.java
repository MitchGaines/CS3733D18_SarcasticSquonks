package edu.wpi.cs3733d18.teamS.pathfind;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The Class manages the Depth First path algorithm changes and finds a path (**not optimal**).
 *
 * @author Noah Hillman
 * @version %I%, %G%
 */
public class DepthFirst extends SearchAlgorithm {
    @Override
    void updateCosts(AStarNode neighbor, AStarNode current, AStarNode goal) {
        neighbor.setParent(current);
        neighbor.setGCost(current.getGCost() + 1);
    }

    @Override
    int prioritySort(AStarNode node1, AStarNode node2) {
        return (int) (node2.getGCost() - node1.getGCost());
    }
}

