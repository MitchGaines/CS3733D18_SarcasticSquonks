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
public class Dijkstras extends SearchAlgorithm {
    @Override
    void updateCosts(AStarNode neighbor, AStarNode current, AStarNode goal) {
        neighbor.setParent(current);
        neighbor.newGCost(current);
    }

    @Override
    int prioritySort(AStarNode node1, AStarNode node2) {
        return (int)(node1.getGCost() - node2.getGCost());
    }
}
