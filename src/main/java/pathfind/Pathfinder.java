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
    HashMap<String, AStarNode> algorithm_node_map = new HashMap<>();
    HashMap<String, Node> nodes = new HashMap();
    HashMap<String, Edge> edges = new HashMap();
    AStarNode goal;
    AStarNode start;
    public Path pathfinder_path = new Path();
    private ISearchAlgorithm algorithm;

    public Pathfinder(ISearchAlgorithm algorithm){
        this.algorithm = algorithm;
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
            algorithm_node_map.put(node.getNodeID(), new AStarNode(node.getNodeID(), node.getXCoord(), node.getYCoord(), node.getXCoord3D(), node.getYCoord3D(), node.getShortName(), node.getLongName(), node.getNodeFloor()));
        }

        for(Edge connection: edges.values()){
            if(algorithm_node_map.containsKey(connection.getStartNode()) && algorithm_node_map.containsKey(connection.getEndNode())){
                algorithm_node_map.get(connection.getStartNode()).neighbors.add(algorithm_node_map.get(connection.getEndNode()));
                algorithm_node_map.get(connection.getEndNode()).neighbors.add(algorithm_node_map.get(connection.getStartNode()));
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
        if(algorithm_node_map.size()==0){
            return;
        }
        this.start = algorithm_node_map.get(startID);
        this.goal = algorithm_node_map.get(goalID);

        //call the algorithm
        reconstructPath(algorithm.findPath(startID, goalID, algorithm_node_map));
        return;
    }

    //returns the pathfinder_path (ordered start ---> finish) given the last node
    private Path reconstructPath(AStarNode end){
        AStarNode previous = end;
        while(true){
            pathfinder_path.algorithm_node_path.add(previous);
            if(previous.checkID(this.start)){

                Collections.reverse(pathfinder_path.algorithm_node_path);
                return pathfinder_path;
            }
            previous = previous.getParent();
        }
    }
}
