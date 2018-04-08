package pathfind;
import data.Node;
import data.Edge;
import database.Storage;
import java.util.*;

/**
 * Pathfinder.java
 * loads from database and calculates the fastest path between given nodes
 * Author: Noah Hillman
 * Date: April 2, 2018
 */

public class Pathfinder {
    HashMap<String, AStarNode> a_star_node_map = new HashMap<>();
    HashMap<String, Node> nodes = new HashMap();
    HashMap<String, Edge> edges = new HashMap();
    AStarNode goal;
    AStarNode start;
    public Path path = new Path();

    public Pathfinder(){
    }

    /**
     * populateMap
     * populates the fathfinder hashmap of a star nodes based on the given nodes and edges.
     * updates the pathfinder path attribute
     *
     * @param nodes hashmap of nodes, with the nodes id as the key
     * @param edges hashmap of edges, which contain node ids to be neighbored
     */
    private void populateMap(HashMap<String, Node> nodes, HashMap<String, Edge> edges){
        for(Node node: nodes.values()){
            a_star_node_map.put(node.getNodeID(), new AStarNode(node.getNodeID(), node.getXCoord(), node.getYCoord(), node.getXCoord3D(), node.getYCoord3D(), node.getShortName(), node.getLongName()));
        }

        for(Edge connection: edges.values()){
            if(a_star_node_map.containsKey(connection.getStartNode()) && a_star_node_map.containsKey(connection.getEndNode())){
                a_star_node_map.get(connection.getStartNode()).neighbors.add(a_star_node_map.get(connection.getEndNode()));
                a_star_node_map.get(connection.getEndNode()).neighbors.add(a_star_node_map.get(connection.getStartNode()));
            }
        }
    }

    private void loadDBData(){
        //TODO update once database gets fixed to pull nodes and edges
        Storage db_storage = Storage.getInstance();
        for (Node node: db_storage.getAllNodes()) {
            nodes.put(node.getNodeID(), node);
        }
        for (Edge edge: db_storage.getAllEdges()) {
            edges.put(edge.getEdgeID(), edge);
        }
        populateMap(nodes, edges);
    }

    //returns the shortest path (list of nodes) between two nodes from the a_star_node_map
    public void findShortestPath(String startID, String goalID){
        loadDBData();
        if(a_star_node_map.size()==0){
            return;
        }
        this.start = this.a_star_node_map.get(startID);
        this.goal = this.a_star_node_map.get(goalID);
        //queue of all Nodes generated but not yet searched (queued in order of increasing cost)
        PriorityQueue<AStarNode> open_a_star_nodes = new PriorityQueue<>(10, heuristicComparator);
        //list of searched nodes that shouldn't be searched again
        LinkedList<AStarNode> closed_a_star_nodes = new LinkedList<>();
        //search the starting node first
        open_a_star_nodes.add(this.start);

        while(!open_a_star_nodes.isEmpty()){
            AStarNode current_node = open_a_star_nodes.poll();
            closed_a_star_nodes.add(current_node);

            if(current_node.checkID(this.goal)){
                reconstructPath(current_node);
                return;
            }

            for(AStarNode neighbor: current_node.neighbors){
                if(!closed_a_star_nodes.contains(neighbor)){
                    if(!open_a_star_nodes.contains(neighbor)){
                        neighbor.setParent(current_node);
                        neighbor.newGCost(current_node);
                        neighbor.newHCost(this.goal);
                        neighbor.calcFCost();
                        open_a_star_nodes.add(neighbor);
                    }
                    else{
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
        String list = "";
        for (AStarNode nodes: closed_a_star_nodes){
            list += nodes.getId() + System.lineSeparator();
        }

        return;
    }
    //returns the path (ordered start ---> finish) given the last node
    private Path reconstructPath(AStarNode end){
        AStarNode previous = end;
        while(true){
            path.a_star_node_path.add(previous);
            if(previous.checkID(this.start)){

                Collections.reverse(path.a_star_node_path);
                return path;
            }
            previous = previous.getParent();
        }
    }

    //comparator to organize how the priority queue sorts items (based on heuristic)
    private static Comparator<AStarNode> heuristicComparator = (AStarNode1, AStarNode2) -> (int) (AStarNode1.getFCost() - AStarNode2.getFCost());
}
