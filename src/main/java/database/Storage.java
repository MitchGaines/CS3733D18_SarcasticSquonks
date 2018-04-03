package database;

/**
 * Storage.java
 * The controller for the Apache Database
 * Author: Joseph Turcotte
 * Date: March 29, 2018
 */

import data.Edge;
import data.Node;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

public class Storage { // TODO: consider using constants (i.e NODE.DB_ID = "node_id")

    // fields
    private IDatabase database;

    /**
     * Empty constructor for storage
     */
    private Storage() {
    }

    /**
     * Get instance of Storage object
     */
    public static Storage getInstance() {
        return StorageHolder.instance;
    }

    /**
     * Sets the database for the controller to interact with
     * @param database the database to connect to
     */
    public void setDatabase(IDatabase database) {

        // configure database and connect
        this.database = database;
        database.connect();

        // create tables for database
        if (!database.doesTableExist("NODES")) {
            database.createTable("NODES", new String[]{
                    String.format("%s VARCHAR (100) PRIMARY KEY", "node_id"),
                    String.format("%s INT", "x_coord"),
                    String.format("%s INT", "y_coord"),
                    String.format("%s VARCHAR (100)", "floor"),
                    String.format("%s VARCHAR (100)", "building"),
                    String.format("%s VARCHAR (100)", "node_type"),
                    String.format("%s VARCHAR (100)", "long_name"),
                    String.format("%s VARCHAR (100)", "short_name"),
                    String.format("%s VARCHAR (100)", "team_assigned"),
                    String.format("%s INT", "x_coord_3d"),
                    String.format("%s INT", "y_coord_3d")
            });
        }

        if (!database.doesTableExist("EDGES")) {
            database.createTable("EDGES", new String[]{
                    String.format("%s VARCHAR (100)", "edge_id"),
                    String.format("%s VARCHAR (100)", "start_node"),
                    String.format("%s VARCHAR (100)", "end_node")
            });
        }
    }

    // ----------- NODE METHODS ------------- //

    /**
     * Inserts the fields of a new node into the nodes table
     * @param node the Node object to insert into the nodes table
     */
    public void saveNode(Node node) {
        database.insert("NODES", new String[] {
                database.addQuotes(node.getNodeID()),
                String.valueOf(node.getXCoord()),
                String.valueOf(node.getYCoord()),
                database.addQuotes(node.getNodeFloor()),
                database.addQuotes(node.getNodeBuilding()),
                database.addQuotes(node.getNodeType()),
                database.addQuotes(node.getLongName()),
                database.addQuotes(node.getShortName()),
                database.addQuotes(node.getTeamAssigned()),
                String.valueOf(node.getXCoord3D()),
                String.valueOf(node.getYCoord3D())
        });
    }

    /**
     * Removes a node entry from the nodes table
     * @param node the Node object to delete from the nodes table
     */
    public void deleteNode(Node node) {
        database.delete("NODES", "node_id = '" + node.getNodeID() + "'", null);
    }

    /**
     * Updates the nodes table entry for a given node
     * @param node the Node object to update in the nodes table
     */
    public void updateNode(Node node) {
        String[] values = new String[] {
                String.format("%s = '%s'", "node_id", node.getNodeID()),
                String.format("%s = %d", "x_coord", node.getXCoord()),
                String.format("%s = %d", "y_coord", node.getYCoord()),
                String.format("%s = '%s'", "floor", node.getNodeFloor()),
                String.format("%s = '%s'", "building", node.getNodeBuilding()),
                String.format("%s = '%s'", "node_type", node.getNodeType()),
                String.format("%s = '%s'", "long_name", node.getLongName()),
                String.format("%s = '%s'", "short_name", node.getShortName()),
                String.format("%s = '%s'", "team_assigned", node.getTeamAssigned()),
                String.format("%s = %d", "x_coord_3d", node.getXCoord3D()),
                String.format("%s = %d", "y_coord_3d", node.getYCoord3D())
        };

        database.update("NODES", values, "node_id = '" + node.getNodeID() + "'", null);
    }

    /**
     * Retrieves a specific node from the nodes table based on its id
     * @param node_id the id of the node to extract from the nodes table
     * @return a Node object corresponding to the node requested
     */
    public Node getNodeByID(String node_id) {
        ResultSet result_set = database.query("NODES", null,
                "node_id = '" + node_id + "'", null, null);
        return getNode(result_set);
    }

    /**
     * Retrieves all nodes from the nodes table
     * @return a List of all of the nodes in the nodes table
     */
    public List<Node> getAllNodes() {
        ResultSet result_set = database.query("NODES", null,
                null, null, null);
        return getNodes(result_set);
    }

    /**
     * Parses the nodes table and adds nodes to a list
     * @param result_set the table of node entries
     * @return a List of the node entries in the nodes table
     */
    private List<Node> getNodes(ResultSet result_set) {
        List<Node> nodes = new LinkedList<>();

        // if there were no results from the query, return null
        if (result_set == null) {
            return nodes;
        }

        // loop through result set and add to list
        Node node;
        while ((node = getNode(result_set)) != null) {
            nodes.add(node);
        }

        return nodes;
    }

    /**
     * Retrieves one node from the nodes table
     * @param result_set a single entry from the nodes table
     * @return a node corresponding to the nodes table entry
     */
    private Node getNode(ResultSet result_set) {

        // if there is no node to get, return null
        try {
            if (result_set == null || !result_set.next()) {
                return null;
            }

            // extract fields from table and put into Node object
            String node_id = result_set.getString("node_id");
            int x_coord = result_set.getInt("x_coord");
            int y_coord = result_set.getInt("y_coord");
            String floor = result_set.getString("floor");
            String building = result_set.getString("building");
            String node_type = result_set.getString("node_type");
            String long_name = result_set.getString("long_name");
            String short_name = result_set.getString("short_name");
            String team_assigned = result_set.getString("team_assigned");
            int x_coord_3d = result_set.getInt("x_coord_3d");
            int y_coord_3d = result_set.getInt("y_coord_3d");

            Node n = new Node(node_id, x_coord, y_coord, floor, building, node_type,
                    long_name, short_name, team_assigned, x_coord_3d, y_coord_3d);

            return n;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ----------- EDGE METHODS ------------- //

    /**
     * Inserts the fields of a new edge into the edges table
     * @param edge the Edge to be inserted into the edges table
     */
    public void saveEdge(Edge edge) {
        database.insert("EDGES", new String[] {
                database.addQuotes(edge.getEdgeID()),
                database.addQuotes(edge.getStartNode()),
                database.addQuotes(edge.getEndNode())
        });
    }

    /**
     * Deletes the edges table entry for the given edge
     * @param edge the Edge to be deleted from the edges table
     */
    public void deleteEdge(Edge edge) {
        database.delete("EDGES", "edge_id = '" + edge.getEdgeID() + "'", null);
    }

    /**
     * Updates the edges table entry for the given edge
     * @param edge the Edge to be updated in the edges table
     */
    public void updateEdge(Edge edge) {
        String[] values = new String[] {
                String.format("%s = '%s'", "edge_id", edge.getEdgeID()),
                String.format("%s = '%s'", "start_node", edge.getStartNode()),
                String.format("%s = '%s'", "end_node", edge.getEndNode())
        };

        database.update("EDGES", values, "edge_id = '" + edge.getEdgeID() + "'", null);
    }

    /**
     * Retrieves a specific Edge from the edges table, based on its id
     * @param edge_id the id of the edge to retrieve from the edges table
     * @return
     */
    public Edge getEdgeByID(String edge_id) {
        ResultSet result_set = database.query("EDGES", null,
                "edge_id = '" + edge_id + "'", null, null);
        return getEdge(result_set);
    }

    /**
     * Retrieves all edges from the edges table
     * @return a List containing all of the edges in the edges table
     */
    public List<Edge> getAllEdges() {
        ResultSet result_set = database.query("EDGES", null,
                null, null, null);
        return getEdges(result_set);
    }

    /**
     * Parses the edges table and adds edges to a list
     * @param result_set the table of edges to parse
     * @return a List containing the edges in the edges table
     */
    private List<Edge> getEdges(ResultSet result_set) {
        List<Edge> edges = new LinkedList<>();

        // if there were no results from the query, return null
        if (result_set == null) {
            return edges;
        }

        // loop through result set and add to list
        Edge edge;
        while ((edge = getEdge(result_set)) != null) {
            edges.add(edge);
        }

        return edges;
    }

    /**
     * Retrieves one edge from the edges table
     * @param result_set a single entry in the edges table
     * @return an Edge corresponding to the entry in the edges table
     */
    private Edge getEdge(ResultSet result_set) {

        // if there is no edge to get, return null
        try {
            if (result_set == null || !result_set.next()) {
                return null;
            }

            // extract fields from table and put into Edge object
            String edge_id = result_set.getString("edge_id");
            String start_node = result_set.getString("start_node");
            String end_node = result_set.getString("end_node");

            Edge e = new Edge(edge_id, start_node, end_node);

            return e;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get database
     * @return the database
     */
    public IDatabase getDatabase() {
        return database;
    }

    /**
     * Storage holder class (singleton)
     */
    private static class StorageHolder {
        private static final Storage instance = new Storage();
    }
}

