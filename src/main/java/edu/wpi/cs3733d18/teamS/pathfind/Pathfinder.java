package edu.wpi.cs3733d18.teamS.pathfind;
import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.data.Edge;
import edu.wpi.cs3733d18.teamS.database.Storage;
import javafx.scene.control.Alert;

import java.util.*;

/**
 * Pathfinder.java
 * Loads from edu.wpi.cs3733d18.teamS.database and calculates the fastest path between given nodes.
 * @author Noah Hillman
 * @version %I%, %G%
 * Date: April 2, 2018
 */

public class Pathfinder {
    /**
     * Stores a HashMap of the Algorithm nodes on the map.
     */
    HashMap<String, AStarNode> algorithm_node_map = new HashMap<>();

    /**
     * Stores a HashMap of the Nodes.
     */
    HashMap<String, Node> nodes = new HashMap();

    /**
     * Stores a HashMap of the Edges.
     */
    HashMap<String, Edge> edges = new HashMap();

    /**
     * Stores the Goal node.
     */
    AStarNode goal;

    /**
     * Stores the Start node.
     */
    AStarNode start;

    /**
     * Stores the path.
     */
    public Path pathfinder_path = new Path();

    /**
     * Stores the path finding algorithm.
     */
    private ISearchAlgorithm algorithm;

    /**
     * Sets the Path finding algorithm.
     * @param algorithm
     */
    public Pathfinder(ISearchAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    /**
     * populateMap
     * populates the pathfinder HashMap of a star nodes based on the given nodes and edges
     * updates the pathfinder path attribute.
     *
     * @param nodes HashMap of nodes, with the nodes id as the key.
     * @param edges HashMap of edges, which contain node ids to be neighbored.
     */
    private void populateMap(HashMap<String, Node> nodes, HashMap<String, Edge> edges){
        for(Node node: nodes.values()){
            algorithm_node_map.put(node.getNodeID(), new AStarNode(node.getNodeID(), node.getXCoord(), node.getYCoord(), node.getXCoord3D(), node.getYCoord3D(), node.getShortName(), node.getLongName(), node.getNodeFloor()));
        }

        for(Edge connection: edges.values()){
            if(algorithm_node_map.containsKey(connection.getStartNode()) && algorithm_node_map.containsKey(connection.getEndNode())){
                algorithm_node_map.get(connection.getStartNode()).addNeighbor(algorithm_node_map.get(connection.getEndNode()));
                algorithm_node_map.get(connection.getEndNode()).addNeighbor(algorithm_node_map.get(connection.getStartNode()));
            }
        }
    }

    /**
     * Loads the Database info.
     */
    private void loadDBData(){
        //TODO update once edu.wpi.cs3733d18.teamS.database gets fixed to pull nodes and edges
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

    /**
     * Finds the shortest path between nodes by taking in their Id's.
     * @param startID Start node's Id.
     * @param goalID Destination node's Id.
     */
    public void findShortestPath(String startID, String goalID){
        loadDBData();
        if(algorithm_node_map.size()==0){
            return;
        }
        this.start = algorithm_node_map.get(startID);
        this.goal = algorithm_node_map.get(goalID);

        //call the algorithm
        AStarNode path_end = algorithm.findPath(startID, goalID, algorithm_node_map);
        if(path_end.getID().contains(goalID)){
            reconstructPath(path_end);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING); //TODO language alltext
            alert.setTitle("Cannot Find Path");
            alert.setHeaderText("There was no viable path generated to your goal");
            alert.setContentText("Please select a new goal location or ask for assistance");
            alert.showAndWait();
        }
        return;
    }

    //returns the pathfinder_path (ordered start ---> finish) given the last node

    /**
     * Reconstructs the path by following the parent nodes to the start node.
     * @param end The end node
     * @return the reconstructed path.
     */
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
