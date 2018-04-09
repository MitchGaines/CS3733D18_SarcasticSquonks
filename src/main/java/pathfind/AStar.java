package pathfind;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStar implements ISearchAlgorithm {

    @Override
    public AStarNode findPath(String start_id, String goal_id, HashMap<String, AStarNode> algorithm_node_map) {
        AStarNode start = algorithm_node_map.get(start_id);
        AStarNode goal = algorithm_node_map.get(goal_id);

        //queue of all Nodes generated but not yet searched (queued in order of increasing cost)
        PriorityQueue<AStarNode> open_a_star_nodes = new PriorityQueue<>(10, heuristicComparator);
        //list of searched nodes that shouldn't be searched again
        LinkedList<AStarNode> closed_a_star_nodes = new LinkedList<>();
        //search the starting node first
        open_a_star_nodes.add(start);

        //run until there are no more searchable nodes (or program finds a pathfinder_path)
        while(!open_a_star_nodes.isEmpty()){
            //take the lowest f score off the priority queue
            AStarNode current_node = open_a_star_nodes.poll();
            //add the current_node to closed list
            closed_a_star_nodes.add(current_node);

            if(current_node.checkID(goal)){
                //System.out.println(closed_a_star_nodes.size() + " searched nodes");
                return current_node;
            }
            //loop over the current_node node's neighbors
            for(AStarNode neighbor: current_node.neighbors){
                //make sure they haven't been searched before
                if(!closed_a_star_nodes.contains(neighbor)){
                    //check if they haven't been discovered before
                    if(!open_a_star_nodes.contains(neighbor)){
                        //add them to the open set of nodes and update parent, g, h, and f costs
                        neighbor.setParent(current_node);
                        neighbor.newGCost(current_node);
                        neighbor.newHCost(goal);
                        neighbor.calcFCost();
                        open_a_star_nodes.add(neighbor);
                    }
                    else{
                        //check if they are a better pathfinder_path (lower gcost), if so, update their cost and parent
                        double temp_g_cost = current_node.getGCost() + current_node.distanceTo(neighbor);
                        if(temp_g_cost < neighbor.getGCost()){
                            neighbor.setParent(current_node);
                            neighbor.newGCost(current_node);
                            neighbor.newHCost(goal);
                            neighbor.calcFCost();
                        }
                    }
                }
            }
        }
        return start;
    }

    //comparator to organize how the priority queue sorts items (based on heuristic)
    private static Comparator<AStarNode> heuristicComparator = (AStarNode1, AStarNode2) -> (int) (AStarNode1.getFCost() - AStarNode2.getFCost());
}
