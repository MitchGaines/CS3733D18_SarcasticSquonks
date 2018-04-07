package database;

import data.Edge;
import data.Node;
import service.ServiceRequest;
import service.ServiceType;
import user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

/**
 * Storage.java
 * The controller for the Apache Database
 * @author Joseph Turcotte
 * @version 1.0
 * Date: March 29, 2018
 * Modified: April 6, 2018
 */
public class Storage {

    /**
     * Stores the database.
     */
    private IDatabase database;

    // to exclude auto-generated IDs from tables
    private final String USER_VALUES = String.format("( %s, %s, %s, %s )",
            "username", "password", "user_type", "can_mod_map");

    private final String SERVICE_VALUES = String.format(" ( %s, %s, %s, %s, %s, %s )",
            "title", "description", "service_type", "requester_id", "fulfiller_id", "location");

    /**
     * Empty constructor for storage.
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
                    String.format("%s VARCHAR (100) PRIMARY KEY", "edge_id"),
                    String.format("%s VARCHAR (100)", "start_node"),
                    String.format("%s VARCHAR (100)", "end_node")
            });
        }

        if (!database.doesTableExist("USERS")) {
            database.createTable("USERS", new String[]{
                    String.format("%s BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)", "user_id"),
                    String.format("%s VARCHAR (100)", "username"),
                    String.format("%s VARCHAR (100)", "password"),
                    String.format("%s VARCHAR (16)", "user_type"),
                    String.format("%s BOOLEAN", "can_mod_map")
            });
        }

        if (!database.doesTableExist("SERVICES")) {
            database.createTable("SERVICES", new String[] {
                    String.format("%s BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)", "service_id"),
                    String.format("%s VARCHAR (100)", "title"),
                    String.format("%s VARCHAR (100)", "description"),
                    String.format("%s VARCHAR (100)", "service_type"),
                    String.format("%s BIGINT", "requester_id"),
                    String.format("%s BIGINT", "fulfiller_id"),
                    String.format("%s VARCHAR (100)", "location")
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
     * @return an Edge object corresponding to the edge with the given id
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

    // ---------------- USER METHODS ----------------- //

    /**
     * Inserts the fields of a new user object into the users table
     * @param user the User object to store in the table
     */
    public void saveUser(User user) { // TODO using plain password might be dangerous
        database.insert("USERS" + USER_VALUES, new String[] {
                database.addQuotes(user.getUsername()),
                database.addQuotes(user.getPlainPassword()),
                database.addQuotes(user.getType().toString()),
                String.valueOf(user.canModMap())
        });
    }

    /**
     * Deletes the given user from the users table
     * @param user the user to delete from the users table
     */
    public void deleteUser(User user) {
        database.delete("USERS", "user_id = " + user.getUserID(), null);
    }

    /**
     * Updates a user in the users table with new values
     * @param user the user to update in the database, with the new values
     */
    public void updateUser(User user) {
        String[] values = new String[] {
                String.format("%s = '%s'", "username", user.getUsername().replaceAll("'", "''")),
                String.format("%s = '%s'", "password", new String(user.getPasswordSalt()).replaceAll("'", "''")),
                String.format("%s = '%s'", "user_type", user.getType().toString()),
                String.format("%s = %b", "can_mod_map", user.canModMap())
        };

        database.update("USERS", values, "user_id = " + user.getUserID(), null);
    }

    /**
     * Gets a user from the users table by unique id
     * @param id the user id of the user in the table
     * @return the user with the given user id
     */
    public User getUserByID(long id) {
        ResultSet r_set = database.query("USERS", null,
                "user_id = " + id, null, null);
        return getUser(r_set);
    }

    /**
     * Gets a user from the table by username only
     * @param username the username of the user to retrieve
     * @return a user with the given username
     */
    public User getUserByName(String username) {
        ResultSet r_set = database.query("USERS", null,
                "username = '" + username + "'", null, null);
        return getUser(r_set);
    }

    /**
     * Gets a user from the users table by username and password
     * @param username username of the user to retrieve
     * @param password password of the user to retrieve
     * @return a user with the given name and password
     */
    public User getUserByCredentials(String username, String password) {
        ResultSet r_set = database.query(
                "USERS",
                null,
                "username = ? AND password = ?",
                new String[]{username, password},
                null
        );

        return getUser(r_set);
    }

    /**
     * Gets a list of all users in the users table
     * @return a List of users in the users table
     */
    public List<User> getAllUsers() {
        ResultSet r_set = database.query("USERS", null, null, null, null);
        return getUsers(r_set);
    }

    /**
     * Private method for parsing result set
     * @param r_set ResultSet containing table entries
     * @return a List of users from the table
     */
    private List<User> getUsers(ResultSet r_set) {
        List<User> users = new LinkedList<>();

        // return an empty list if query didn't return anything
        if (r_set == null) {
            return users;
        }

        User user;
        while ((user = getUser(r_set)) != null) {
            users.add(user);
        }

        return users;
    }

    /**
     * Private method for retrieving a user from a database query
     * @param r_set The ResultSet containing a single table entry
     * @return a User object corresponding to the single table entry
     */
    private User getUser(ResultSet r_set) {
        try {
            // if we don't have anything in the result set
            if (r_set == null || !r_set.next()) {
                return null;
            }

            // extract fields from result set and store in user object
            long id = r_set.getLong("user_id");
            String username = r_set.getString("username");
            String password = r_set.getString("password");
            User.user_type user_type = User.user_type.valueOf(r_set.getString("user_type"));
            boolean can_mod_map = r_set.getBoolean("can_mod_map");

            User user = new User(username, password, user_type);
            user.setUserID(id);
            user.setCanModMap(can_mod_map);

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- SERVICE REQUEST METHODS --------------- //

//    /**
//     * Inserts the fields of a new service request object into the services table
//     * @param request the ServiceRequest object to store in the table
//     */
//    public void saveRequest(ServiceRequest request) {
//        database.insert("SERVICES" + SERVICE_VALUES, new String[] {
//                database.addQuotes(request.getTitle()),
//                database.addQuotes(request.getDescription()),
//                database.addQuotes(request.getServiceType().getName()),
//                String.valueOf(request.getRequesterID()),
//                String.valueOf(request.getFulfillerID()),
//                database.addQuotes(request.getLocation())
//        });
//    }
//
//    /**
//     * Removes a request from the requests table
//     * @param request the request to remove from the table
//     */
//    public void deleteRequest(ServiceRequest request) {
//        database.delete("SERVICES", "service_id = " + request.getRequestID(), null);
//    }
//
//    /**
//     * Updates a request in the requests table with new values
//     * @param request the request to update in the database, with the new values
//     */
//    public void updateRequest(ServiceRequest request) {
//        String[] values = new String[] {
//                String.format("%s = '%s'", "title", request.getTitle().replaceAll("'", "''")),
//                String.format("%s = '%s'", "description", request.getDescription().replaceAll("'", "''")),
//                String.format("%s = '%s'", "service_type", request.getServiceType().getName().replaceAll("'", "''")),
//                String.format("%s = %d", "requester_id", request.getRequesterID()),
//                String.format("%s = %d", "fulfiller_id", request.getFulfillerID()),
//                String.format("%s = '%s'", "location", request.getLocation())
//        };
//
//        database.update("SERVICES", values, "service_id = " + request.getRequestID(), null);
//    }
//
//    /**
//     * Gets a service request from the table by id
//     * @param id the id of the service request to retrieve
//     * @return the service request corresponding to the given id
//     */
//    public ServiceRequest getRequestByID(long id) {
//        ResultSet r_set = database.query(
//                "SERVICES",
//                null,
//                "service_id = " + id,
//                null,
//                null
//        );
//
//        return getRequest(r_set);
//    }
//
//    /**
//     * Gets all requests of a specific type
//     * @return a List of service requests corresponding to a specific type
//     */
//    public List<ServiceRequest> getRequestsByType(ServiceType type) {
//        ResultSet r_set = database.query(
//                "SERVICES",
//                null,
//                "service_type = '" + type.getName() + "'",
//                null,
//                null
//        );
//
//        return getRequests(r_set);
//    }
//
//    /**
//     * Gets all service requests by a specific user
//     * @param user the user who requested services
//     * @return a List of service requests from the given user
//     */
//    public List<ServiceRequest> getAllRequestsByUser(User user) {
//        ResultSet r_set = database.query(
//                "SERVICES",
//                null,
//                "requester_id = " + user.getUserID(),
//                null,
//                null
//        );
//
//        return getRequests(r_set);
//    }
//
//    /**
//     * Gets all service requests assigned to a specific user
//     * @param user the user to retrieve service requests for
//     * @return a List of service requests for the given user
//     */
//    public List<ServiceRequest> getAllRequestsAssignedToUser(User user) {
//        ResultSet r_set = database.query(
//                "SERVICES",
//                null,
//                "fulfiller_id = " + user.getUserID(),
//                null,
//                null
//        );
//
//        return getRequests(r_set);
//    }
//
//    /**
//     * Gets all service requests in the requests table
//     * @return a List containing all of the requests in the table
//     */
//    public List<ServiceRequest> getAllServiceRequests() {
//        ResultSet r_set = database.query(
//                "SERVICES",
//                null,
//                null,
//                null,
//                null
//        );
//
//        return getRequests(r_set);
//    }
//
//    /**
//     * private method for parsing result set
//     * @param r_set the result set containing service requests
//     * @return a List of the service request objects from the table
//     */
//    private List<ServiceRequest> getRequests(ResultSet r_set) {
//        List<ServiceRequest> requests = new LinkedList<>();
//
//        // return an empty list if query didn't return anything
//        if (r_set == null) {
//            return requests;
//        }
//
//        ServiceRequest request;
//        while ((request = getRequest(r_set)) != null) {
//            requests.add(request);
//        }
//
//        return requests;
//    }
//
//    /**
//     * private method for parsing result set
//     * @param r_set the result set consisting of a single table entry
//     * @return the service request object corresponding to the request
//     */
//    private ServiceRequest getRequest(ResultSet r_set) {
//
//        try {
//            // if we don't have anything in the result set
//            if (r_set == null || !r_set.next()) {
//                return null;
//            }
//
//            // extract fields from result set and store in service request object
//            long service_id = r_set.getLong("service_id");
//            String title = r_set.getString("title");
//            String description = r_set.getString("description");
//            long request_id = r_set.getLong("requester_id");
//            long fulfill_id = r_set.getLong("fulfiller_id");
//            String location = r_set.getString("location");
//
//            // ServiceType is an object, so it must be created (default is non-emergency and no fulfillers) TODO
//            ServiceType service_type;
//            service_type = ServiceType.createServiceType(r_set.getString("service_type"), false, null);
//
//            ServiceRequest request = ServiceRequest.createService(service_id, title, description, service_type, request_id, fulfill_id, location);
//
//            return request;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
    /**
     * Get database.
     * @return the database.
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

