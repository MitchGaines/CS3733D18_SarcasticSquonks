package edu.wpi.cs3733d18.teamS.pathfind;

/**
 * The Class manages the breadth First path algorithm changes and finds a path by searching outwards (not movement
 * cost based).
 *
 * @author Noah Hillman
 * @version %I%, %G%
 */
public class BreadthFirst extends SearchAlgorithm {
    @Override
    void updateCosts(AStarNode neighbor, AStarNode current, AStarNode goal) {
        neighbor.setParent(current);
        neighbor.setGCost(current.getGCost() + 1);
    }

    @Override
    int prioritySort(AStarNode node1, AStarNode node2) {
        return (int) (node1.getGCost() - node2.getGCost());
    }
}
