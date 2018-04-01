package pathfind;
import data.Node;
import data.Edge;

import java.util.*;

public class Pathfinder {
    public LinkedList<AStarNode> path = new LinkedList<AStarNode>();
    public HashMap<String, AStarNode> a_star_node_map = new HashMap<>();
    public AStarNode goal;
    public AStarNode start;
    public Pathfinder(){
    }

    /**
     * populates the fathfinder hashmap of a star nodes based on the given nodes and edges.
     *
     * @param nodes hashmap of nodes, with the nodes id as the key
     * @param edges hashmap of edges, which contain node ids to be neighbored
     */
    public void populateMap(HashMap<String, Node> nodes, HashMap<String, Edge> edges){
        for(Node node: nodes.values()){
            a_star_node_map.put(node.getNodeID(), new AStarNode(node.getNodeID(), node.getXCoord(), node.getYCoord()));
        }
        for(Edge connection: edges.values()){
            a_star_node_map.get(connection.getStartNode()).neighbors.put(connection.getEndNode(), a_star_node_map.get(connection.getEndNode()));
            a_star_node_map.get(connection.getEndNode()).neighbors.put(connection.getStartNode(), a_star_node_map.get(connection.getStartNode()));
        }
    }

    //returns the shortest path (list of nodes) between two nodes from the a_star_node_map
    public Path findShortestPath(String startID, String goalID){
        this.start = this.a_star_node_map.get(startID);
        this.goal = this.a_star_node_map.get(goalID);

        //queue of all Nodes generated but not yet searched (queued in order of increasing cost)
        PriorityQueue<AStarNode> open_a_star_nodes = new PriorityQueue<>(10, heuristicComparator);
        //list of searched nodes that shouldn't be searched again
        LinkedList<AStarNode> closed_a_star_nodes = new LinkedList<>();
        //search the starting node first
        open_a_star_nodes.add(this.start);

        //run until there are no more searchable nodes (or program finds a path)
        while(!open_a_star_nodes.isEmpty()){
            //take the lowest f score off the priority queue
            AStarNode current_node = open_a_star_nodes.poll();
            //System.out.println("current_node node: " + current_node.getID());
            //add the current_node to closed list
            closed_a_star_nodes.add(current_node);

            if(current_node.checkID(this.goal)){
                //System.out.println("Arrived at Goal!");
                return reconstructPath(current_node);
            }
            //loop over the current_node node's neighbors
            for(AStarNode neighbor: current_node.neighbors.values()){
                //System.out.println("found neighbor: " + neighbor.getID());
                //make sure they haven't been searched before
                if(!closed_a_star_nodes.contains(neighbor)){
                    //check if they haven't been discovered before
                    if(!open_a_star_nodes.contains(neighbor)){
                        //add them to the open set of nodes and update parent, g, h, and f costs
                        neighbor.setParent(current_node);
                        neighbor.newGCost(current_node);
                        neighbor.newHCost(this.goal);
                        neighbor.calcFCost();
                        open_a_star_nodes.add(neighbor);
                    }
                    else{
                        //check if they are a better path (lower gcost), if so, update their cost and parent
                        double temp_g_cost = current_node.getGCost() + current_node.distanceTo(neighbor);
                        if(temp_g_cost < neighbor.getGCost()){
                            neighbor.setParent(current_node);
                            neighbor.newGCost(current_node);
                            neighbor.newHCost(this.goal);
                            neighbor.calcFCost();
                        }
                    }
                }
            }
        }
        System.out.println("could not find suitable path");
        return new Path();
    }
    //returns the path (ordered start ---> finish) given the last node
    private Path reconstructPath(AStarNode end){

        AStarNode previous = end;
        Path path = new Path();
        while(true){
            path.node_path.add(previous);
            if(previous.checkID(this.start)){
                System.out.println("reconstructed path with " + path.node_path.size() + " steps");
                Collections.reverse(path.node_path);
                return path;
            }
            previous = previous.getParent();
        }
    }
    //comparator to organize how the priority queue sorts items (based on heuristic)
    private static Comparator<AStarNode> heuristicComparator = (AStarNode1, AStarNode2) -> (int) (AStarNode1.getFCost() - AStarNode2.getFCost());
}