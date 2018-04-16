package edu.wpi.cs3733d18.teamS.database;

import edu.wpi.cs3733d18.teamS.data.Edge;
import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import edu.wpi.cs3733d18.teamS.user.LoginHandler;
import edu.wpi.cs3733d18.teamS.user.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Storage.java
 * The edu.wpi.cs3733d18.teamS.controller for the Apache Database
 * @author Joseph Turcotte
 * @version %I%, %G%
 * Date: March 29, 2018
 * Modified: April 6, 2018
 */
public class Storage {

    // to exclude auto-generated IDs from tables
    private final String USER_VALUES = String.format("( %s, %s, %s, %s, %s, %s )",
            "username", "password", "first_name", "last_name", "user_type", "can_mod_map");
    private final String SERVICE_VALUES = String.format(" ( %s, %s, %s, %s, %s, %s, %s, %s )",
            "title", "description", "service_type", "requester_id", "fulfiller_id", "location", "request_time", "fulfill_time");
    /**
     * Stores the edu.wpi.cs3733d18.teamS.database.
     */
    private IDatabase database;
    // to parse dates
    private DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Empty constructor for storage.
     */
    private Storage() {
    }

    /**
     * Get instance of Storage object.
     */
    public static Storage getInstance() {
        return StorageHolder.instance;
    }

    // ----------- NODE METHODS ------------- //

    /**
     * Inserts the fields of a new node into the nodes table.
     *
     * @param node the Node object to insert into the nodes table.
     */
    public void saveNode(Node node) {
        database.insert("NODES", new String[]{
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
     * Removes a node entry from the nodes table.
     *
     * @param node the Node object to delete from the nodes table.
     */
    public void deleteNode(Node node) {
        database.delete("NODES", "node_id = '" + node.getNodeID() + "'", null);
    }

    /**
     * Updates the nodes table entry for a given node.
     *
     * @param node the Node object to update in the nodes table.
     */
    public void updateNode(Node node) {
        String[] values = new String[]{
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
     * Retrieves a specific node from the nodes table based on its id.
     *
     * @param node_id the id of the node to extract from the nodes table.
     * @return a Node object corresponding to the node requested.
     */
    public Node getNodeByID(String node_id) {
        ResultSet result_set = database.query("NODES", null,
                "node_id = '" + node_id + "'", null, null);
        return getNode(result_set);
    }

    /**
     * Retrieves all nodes from the nodes table.
     *
     * @return a List of all of the nodes in the nodes table.
     */
    public List<Node> getAllNodes() {
        ResultSet result_set = database.query("NODES", null,
                null, null, null);
        return getNodes(result_set);
    }

    /**
     * Parses the nodes table and adds nodes to a list.
     *
     * @param result_set the table of node entries.
     * @return a List of the node entries in the nodes table.
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
     * Retrieves one node from the nodes table.
     *
     * @param result_set a single entry from the nodes table.
     * @return a node corresponding to the nodes table entry.
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
     * Inserts the fields of a new edge into the edges table.
     *
     * @param edge the Edge to be inserted into the edges table.
     */
    public void saveEdge(Edge edge) {
        database.insert("EDGES", new String[]{
                database.addQuotes(edge.getEdgeID()),
                database.addQuotes(edge.getStartNode()),
                database.addQuotes(edge.getEndNode())
        });
    }

    /**
     * Deletes the edges table entry for the given edge.
     *
     * @param edge the Edge to be deleted from the edges table.
     */
    public void deleteEdge(Edge edge) {
        database.delete("EDGES", "edge_id = '" + edge.getEdgeID() + "'", null);
    }

    /**
     * Updates the edges table entry for the given edge.
     *
     * @param edge the Edge to be updated in the edges table.
     */
    public void updateEdge(Edge edge) {
        String[] values = new String[]{
                String.format("%s = '%s'", "edge_id", edge.getEdgeID()),
                String.format("%s = '%s'", "start_node", edge.getStartNode()),
                String.format("%s = '%s'", "end_node", edge.getEndNode())
        };

        database.update("EDGES", values, "edge_id = '" + edge.getEdgeID() + "'", null);
    }

    /**
     * Retrieves a specific Edge from the edges table, based on its id.
     *
     * @param edge_id the id of the edge to retrieve from the edges table.
     * @return an Edge object corresponding to the edge with the given id.
     */
    public Edge getEdgeByID(String edge_id) {
        ResultSet result_set = database.query("EDGES", null,
                "edge_id = '" + edge_id + "'", null, null);
        return getEdge(result_set);
    }

    /**
     * Retrieves all edges from the edges table.
     *
     * @return a List containing all of the edges in the edges table.
     */
    public List<Edge> getAllEdges() {
        ResultSet result_set = database.query("EDGES", null,
                null, null, null);
        return getEdges(result_set);
    }

    /**
     * Parses the edges table and adds edges to a list.
     *
     * @param result_set the table of edges to parse.
     * @return a List containing the edges in the edges table.
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
     * Retrieves one edge from the edges table.
     *
     * @param result_set a single entry in the edges table.
     * @return an Edge corresponding to the entry in the edges table.
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
     * Inserts the fields of a new edu.wpi.cs3733d18.teamS.user object into the users table.
     *
     * @param user the User object to store in the table.
     */
    public void saveUser(User user) {
        database.insert("USERS" + USER_VALUES, new String[]{
                database.addQuotes(user.getUsername()),
                database.addQuotes(new String(user.getEncodedPassword(), Charset.forName("UTF-8"))),
                database.addQuotes(user.getFirstName()),
                database.addQuotes(user.getLastName()),
                database.addQuotes(user.getType().toString()),
                String.valueOf(user.canModMap())
        });
    }

    /**
     * Deletes the given edu.wpi.cs3733d18.teamS.user from the users table.
     *
     * @param user the edu.wpi.cs3733d18.teamS.user to delete from the users table.
     */
    public void deleteUser(User user) {
        database.delete("USERS", "user_id = " + user.getUserID(), null);
    }

    /**
     * Updates a edu.wpi.cs3733d18.teamS.user in the users table with new values.
     *
     * @param user the edu.wpi.cs3733d18.teamS.user to update in the edu.wpi.cs3733d18.teamS.database, with the new values.
     */
    public void updateUser(User user) {
        String[] values = new String[]{
                String.format("%s = '%s'", "username", user.getUsername().replaceAll("'", "''")),
                String.format("%s = '%s'", "password", new String(user.getEncodedPassword(), Charset.forName("UTF-8")).replaceAll("'", "''")),
                String.format("%s = '%s'", "first_name", user.getFirstName().replaceAll("'", "''")),
                String.format("%s = '%s'", "last_name", user.getLastName().replaceAll("'", "''")),
                String.format("%s = '%s'", "user_type", user.getType().toString()),
                String.format("%s = %b", "can_mod_map", user.canModMap())
        };

        database.update("USERS", values, "user_id = " + user.getUserID(), null);
    }

    /**
     * Gets a edu.wpi.cs3733d18.teamS.user from the users table by unique id.
     *
     * @param id the edu.wpi.cs3733d18.teamS.user id of the edu.wpi.cs3733d18.teamS.user in the table.
     * @return the edu.wpi.cs3733d18.teamS.user with the given edu.wpi.cs3733d18.teamS.user id.
     */
    public User getUserByID(long id) {
        ResultSet r_set = database.query("USERS", null,
                "user_id = " + id, null, null);
        return getUser(r_set);
    }

    /**
     * Gets a edu.wpi.cs3733d18.teamS.user from the table by username only.
     *
     * @param username the username of the edu.wpi.cs3733d18.teamS.user to retrieve.
     * @return a edu.wpi.cs3733d18.teamS.user with the given username.
     */
    public User getUserByName(String username) {
        ResultSet r_set = database.query("USERS", null,
                "username = '" + username + "'", null, null);
        return getUser(r_set);
    }

    /**
     * Gets a edu.wpi.cs3733d18.teamS.user from the users table by username and password.
     *
     * @param username username of the edu.wpi.cs3733d18.teamS.user to retrieve.
     * @param password password of the edu.wpi.cs3733d18.teamS.user to retrieve.
     * @return a edu.wpi.cs3733d18.teamS.user with the given name and password.
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
     * Gets a list of all users in the users table.
     *
     * @return a List of users in the users table.
     */
    public List<User> getAllUsers() {
        ResultSet r_set = database.query("USERS", null, null, null, null);
        return getUsers(r_set);
    }

    /**
     * Private method for parsing result set.
     *
     * @param r_set ResultSet containing table entries.
     * @return a List of users from the table.
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
     * Private method for retrieving a edu.wpi.cs3733d18.teamS.user from a edu.wpi.cs3733d18.teamS.database query.
     *
     * @param r_set The ResultSet containing a single table entry.
     * @return a User object corresponding to the single table entry.
     */
    private User getUser(ResultSet r_set) {
        try {
            // if we don't have anything in the result set
            if (r_set == null || !r_set.next()) {
                return null;
            }

            // extract fields from result set and store in edu.wpi.cs3733d18.teamS.user object
            long id = r_set.getLong("user_id");
            String username = r_set.getString("username");
            String password = r_set.getString("password");
            String first_name = r_set.getString("first_name");
            String last_name = r_set.getString("last_name");
            User.user_type user_type = User.user_type.valueOf(r_set.getString("user_type"));
            boolean can_mod_map = r_set.getBoolean("can_mod_map");

            User user = new User(username, password, first_name, last_name, user_type, can_mod_map);
            user.setUserID(id);

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- SERVICE REQUEST METHODS --------------- //

    /**
     * Inserts the fields of a new edu.wpi.cs3733d18.teamS.service request object into the services table.
     *
     * @param request the ServiceRequest object to store in the table.
     */
    public void saveRequest(ServiceRequest request) {

        // check if fulfiller id is null
        String fulfiller_string = request.getFulfiller() == null ? "null" : String.valueOf(request.getFulfiller().getUserID());

        database.insert("SERVICES" + SERVICE_VALUES, new String[]{
                database.addQuotes(request.getTitle()),
                database.addQuotes(request.getDescription()),
                database.addQuotes(request.getServiceType().getName()),
                String.valueOf(request.getRequester().getUserID()),
                fulfiller_string,
                database.addQuotes(request.getLocation().getNodeID()),
                database.addQuotes(dtf.print(request.getRequestedDate())),
                database.addQuotes(dtf.print(request.getFulfilledDate()))
        });
    }

    /**
     * Removes a request from the requests table.
     *
     * @param request the request to remove from the table.
     */
    public void deleteRequest(ServiceRequest request) {
        database.delete("SERVICES", "service_id = " + request.getRequester().getUserID(), null);
    }

    /**
     * Updates a request in the requests table with new values.
     *
     * @param request the request to update in the edu.wpi.cs3733d18.teamS.database, with the new values.
     */
    public void updateRequest(ServiceRequest request) {

        String[] values = new String[]{
                String.format("%s = '%s'", "title", request.getTitle().replaceAll("'", "''")),
                String.format("%s = '%s'", "description", request.getDescription().replaceAll("'", "''")),
                String.format("%s = '%s'", "service_type", request.getServiceType().getName().replaceAll("'", "''")),
                String.format("%s = %d", "requester_id", request.getRequester().getUserID()),
                String.format("%s = %d", "fulfiller_id", request.getFulfiller().getUserID()),
                String.format("%s = '%s'", "location", request.getLocation().getNodeID()),
                String.format("%s = '%s'", "request_time", dtf.print(request.getRequestedDate())),
                String.format("%s = '%s'", "fulfill_time", dtf.print(request.getFulfilledDate()))
        };

        database.update("SERVICES", values, "service_id = " + request.getRequester().getUserID(), null);
    }

    /**
     * Gets a edu.wpi.cs3733d18.teamS.service request from the table by id.
     *
     * @param id the id of the edu.wpi.cs3733d18.teamS.service request to retrieve.
     * @return the edu.wpi.cs3733d18.teamS.service request corresponding to the given id.
     */
    public List<ServiceRequest> getRequestByID(long id) {
        ResultSet r_set = database.query(
                "SERVICES",
                null,
                "service_id = " + id,
                null,
                null
        );

        return getRequests(r_set);
    }

    /**
     * Gets all requests of a specific type.
     *
     * @return a List of edu.wpi.cs3733d18.teamS.service requests corresponding to a specific type.
     */
    public List<ServiceRequest> getRequestsByType(ServiceType type) {
        ResultSet r_set = database.query(
                "SERVICES",
                null,
                "service_type = '" + type.getName() + "'",
                null,
                null
        );

        return getRequests(r_set);
    }

    /**
     * Gets all edu.wpi.cs3733d18.teamS.service requests by a specific edu.wpi.cs3733d18.teamS.user.
     *
     * @param user the edu.wpi.cs3733d18.teamS.user who requested services.
     * @return a List of edu.wpi.cs3733d18.teamS.service requests from the given edu.wpi.cs3733d18.teamS.user.
     */
    public List<ServiceRequest> getAllRequestsByUser(User user) {
        ResultSet r_set = database.query(
                "SERVICES",
                null,
                "requester_id = " + user.getUserID(),
                null,
                null
        );

        return getRequests(r_set);
    }

    /**
     * Gets all edu.wpi.cs3733d18.teamS.service requests assigned to a specific edu.wpi.cs3733d18.teamS.user.
     *
     * @param user the edu.wpi.cs3733d18.teamS.user to retrieve edu.wpi.cs3733d18.teamS.service requests for.
     * @return a List of edu.wpi.cs3733d18.teamS.service requests for the given edu.wpi.cs3733d18.teamS.user.
     */
    public List<ServiceRequest> getAllRequestsAssignedToUser(User user) {
        ResultSet r_set = database.query(
                "SERVICES",
                null,
                "fulfiller_id = " + user.getUserID(),
                null,
                null
        );

        return getRequests(r_set);
    }

    /**
     * Gets all edu.wpi.cs3733d18.teamS.service requests in the requests table.
     *
     * @return a List containing all of the requests in the table.
     */
    public List<ServiceRequest> getAllServiceRequests() {
        ResultSet r_set = database.query(
                "SERVICES",
                null,
                null,
                null,
                null
        );

        return getRequests(r_set);
    }

    /**
     * private method for parsing result set.
     *
     * @param r_set the result set containing edu.wpi.cs3733d18.teamS.service requests.
     * @return a List of the edu.wpi.cs3733d18.teamS.service request objects from the table.
     */
    private List<ServiceRequest> getRequests(ResultSet r_set) {
        List<ServiceRequest> requests = new LinkedList<>();
        List<Long> id_requesters = new LinkedList<>();
        List<Long> id_fulfillers = new LinkedList<>();
        List<String> types = new LinkedList<>();
        List<String> locations = new LinkedList<>();
        List<DateTime> requestDateTimes = new LinkedList<>();
        List<DateTime> fulfillDateTimes = new LinkedList<>();

        // return an empty list if query didn't return anything
        if (r_set == null) {
            return requests;
        }

        ServiceRequest request;
        while ((request = getRequest(r_set)) != null) {
            try {
                id_requesters.add(r_set.getLong("requester_id"));
                id_fulfillers.add(r_set.getLong("fulfiller_id"));
                types.add(r_set.getString("service_type"));
                locations.add(r_set.getString("location"));
                requestDateTimes.add(new DateTime(r_set.getDate("request_time")));
                fulfillDateTimes.add(new DateTime(r_set.getDate("fulfill_time")));
                requests.add(request);
            } catch (SQLException e) {
                // empty
            }
        }

        // second for loop to create the edu.wpi.cs3733d18.teamS.service requests
        int length = id_requesters.size();
        for (int i = 0; i < length; i++) {
            ServiceType serviceType = getServiceTypeByName(types.get(i));
            User requester = getUserByID(id_requesters.get(i));
            User fulfiller = getUserByID(id_fulfillers.get(i));
            Node location = getNodeByID(locations.get(i));
            DateTime requestDateTime = requestDateTimes.get(i);
            DateTime fulfillDateTime = fulfillDateTimes.get(i);

            requests.get(i).setService_type(serviceType);
            requests.get(i).setRequester(requester);
            requests.get(i).setFulfiller(fulfiller);
            requests.get(i).setLocation(location);
            requests.get(i).setRequestedDate(requestDateTime);
            requests.get(i).setFulfilledDate(fulfillDateTime);
        }

        return requests;
    }

    /**
     * private method for parsing result set.
     *
     * @param r_set the result set consisting of a single table entry.
     * @return the edu.wpi.cs3733d18.teamS.service request object corresponding to the request.
     */
    private ServiceRequest getRequest(ResultSet r_set) {

        try {
            // if we don't have anything in the result set
            if (r_set == null || !r_set.next()) {
                return null;
            }

            // extract fields from result set and store in edu.wpi.cs3733d18.teamS.service request object
            long service_id = r_set.getLong("service_id");
            String title = r_set.getString("title");
            String description = r_set.getString("description");

            // set some request fields to null, then fill them in after
            ServiceRequest request = ServiceRequest.createService(title, description, null, null, null);
            request.setRequestID(service_id);

            return request;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ------------------------- SERVICE TYPE OPERATIONS ----------------------- //

    /**
     * Inserts a edu.wpi.cs3733d18.teamS.service type into the edu.wpi.cs3733d18.teamS.service types table.
     *
     * @param type the edu.wpi.cs3733d18.teamS.service type to add to the table.
     */
    public void saveServiceType(ServiceType type) {
        database.insert("TYPES", new String[]{
                database.addQuotes(type.getName()),
                String.valueOf(type.isEmergency())
        });
    }

    /**
     * Updates a edu.wpi.cs3733d18.teamS.service type in the edu.wpi.cs3733d18.teamS.service types table.
     *
     * @param type the edu.wpi.cs3733d18.teamS.service type to be updated.
     */
    public void updateServiceType(ServiceType type) {
        String values[] = new String[]{
                String.format("%s = '%s'", "name", type.getName()),
                String.format("%s = %b", "emergency", type.isEmergency())
        };

        database.update("TYPES", values, "name = '" + type.getName() + "'", null);
    }

    /**
     * gets a edu.wpi.cs3733d18.teamS.service request by name.
     *
     * @param name the name of the edu.wpi.cs3733d18.teamS.service type.
     * @return the edu.wpi.cs3733d18.teamS.service type corresponding to the given name.
     */
    public ServiceType getServiceTypeByName(String name) {
        ResultSet r_set = database.query(
                "TYPES",
                null,
                "name = '" + name + "'",
                null,
                null
        );

        return getServiceType(r_set);
    }

    /**
     * Gets all edu.wpi.cs3733d18.teamS.service types from the edu.wpi.cs3733d18.teamS.database.
     *
     * @return a List of all edu.wpi.cs3733d18.teamS.service types in the edu.wpi.cs3733d18.teamS.database.
     */
    public List<ServiceType> getAllServiceTypes() {
        ResultSet r_set = database.query(
                "TYPES",
                null,
                null,
                null,
                null
        );

        return getServiceTypes(r_set);
    }

    /**
     * Private method for parsing result set.
     *
     * @param r_set a set of all of the entries in the table.
     * @return a List of ServiceTypes corresponding to table entries.
     */
    private List<ServiceType> getServiceTypes(ResultSet r_set) {
        List<ServiceType> types = new LinkedList<>();
        //List<String> type_names = new LinkedList<>();
        //List<HashSet<User>> fulfiller_list = new LinkedList<>();

        // if there were no results from the query, return null
        if (r_set == null) {
            return types;
        }

        // loop through result set and add to list
        ServiceType type;
        while ((type = getServiceType(r_set)) != null) {
            //type_names.add(type.getName());
            types.add(type);
        }

        // second for loop to set fulfillers
        int length = types.size();
        for (int i = 0; i < length; i++) {
            HashSet<User> users = getAllFulfillersByType(types.get(i));
            types.get(i).setFulfillers(users);
        }

        return types;
    }

    /**
     * Retrieves a single edu.wpi.cs3733d18.teamS.service type from the table.
     *
     * @param r_set a single entry in the table.
     * @return a edu.wpi.cs3733d18.teamS.service type object from the table.
     */
    private ServiceType getServiceType(ResultSet r_set) {
        try {
            // if we don't have anything in the result set
            if (r_set == null || !r_set.next()) {
                return null;
            }

            // extract fields from result set and store in service type object
            String name = r_set.getString("name");
            boolean emergency = r_set.getBoolean("emergency");

            // create unfinished service type and fill in later
            ServiceType serviceType = ServiceType.createServiceType(name, emergency, new HashSet<>());

            return serviceType;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // --------------------- FULFILLER METHODS ------------------- //

    /**
     * Inserts a fulfiller into the fulfillers table
     *
     * @param type the service type the fulfiller is associated with
     * @param fulfiller the user representing the fulfiller
     */
    public void saveFulfiller(ServiceType type, User fulfiller) {
        database.insert("FULFILLERS", new String[]{
                database.addQuotes(type.getName()),
                String.valueOf(fulfiller.getUserID())
        });
    }

    /**
     * Gets all fulfillers of a specific type from the table
     *
     * @return a List containing all of the fulfillers of the given type in the table
     */
    public HashSet<User> getAllFulfillersByType(ServiceType type) {
        ResultSet r_set = database.query(
                "FULFILLERS",
                null,
                "service_type = '" + type.getName() + "'",
                null,
                null
        );

        return getFulfillers(r_set);
    }

    /**
     * Private method for parsing result set
     *
     * @param r_set a set of all of the entries in the table
     * @return a List of Users corresponding to table entries
     */
    private HashSet<User> getFulfillers(ResultSet r_set) {
        HashSet<User> fulfillers = new HashSet<>();
        List<Long> fulfiller_ids = new LinkedList<>();

        // if there were no results from the query, return null
        if (r_set == null) {
            return fulfillers;
        }

        // loop through result set and add to list
        User fulfiller;
        while ((fulfiller = getFulfiller(r_set)) != null) {
            try {
                // extract user ids from result set and add to arrays
                long id = r_set.getLong("fulfiller_user_id");

                fulfiller_ids.add(id);
            } catch (SQLException e) {
                // empty
            }
        }

        // second for loop to construct fulfiller array
        int length = fulfiller_ids.size();
        for (int i = 0; i < length; i++) {
            User fulfiller_user = getUserByID(fulfiller_ids.get(i));

            fulfillers.add(fulfiller_user);
        }

        return fulfillers;
    }

    /**
     * Retrieves a single fulfiller from the table
     *
     * @param r_set a single entry in the table
     * @return a User object from the table
     */
    private User getFulfiller(ResultSet r_set) {
        try {
            // if we don't have anything in the result set
            if (r_set == null || !r_set.next()) {
                return null;
            }

            // set fields as garbage values, then fill in later
            User fulfiller = new User("", "", "","",null, false);

            return fulfiller;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get edu.wpi.cs3733d18.teamS.database.
     *
     * @return the edu.wpi.cs3733d18.teamS.database.
     */
    public IDatabase getDatabase() {
        return database;
    }

    /**
     * Sets the edu.wpi.cs3733d18.teamS.database for the edu.wpi.cs3733d18.teamS.controller to interact with.
     *
     * @param database the edu.wpi.cs3733d18.teamS.database to connect to.
     */
    public void setDatabase(IDatabase database) {

        // configure edu.wpi.cs3733d18.teamS.database and connect
        this.database = database;
        database.connect();

        // create tables for edu.wpi.cs3733d18.teamS.database
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
                    String.format("%s VARCHAR (100)", "first_name"),
                    String.format("%s VARCHAR (100)", "last_name"),
                    String.format("%s VARCHAR (16)", "user_type"),
                    String.format("%s BOOLEAN", "can_mod_map")
            });

            // generate users and add to database
            LoginHandler.generateInitialUsers();
        }

        if (!database.doesTableExist("SERVICES")) {
            database.createTable("SERVICES", new String[]{
                    String.format("%s BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)", "service_id"),
                    String.format("%s VARCHAR (100)", "title"),
                    String.format("%s VARCHAR (100)", "description"),
                    String.format("%s VARCHAR (100)", "service_type"),
                    String.format("%s BIGINT", "requester_id"),
                    String.format("%s BIGINT", "fulfiller_id"),
                    String.format("%s VARCHAR (100)", "location"),
                    String.format("%s TIMESTAMP", "request_time"),
                    String.format("%s TIMESTAMP", "fulfill_time")
            });
        }

        if (!database.doesTableExist("FULFILLERS")) {
            database.createTable("FULFILLERS", new String[]{
                    String.format("%s VARCHAR (100)", "service_type"),
                    String.format("%s BIGINT", "fulfiller_user_id"),
            });
        }

        if (!database.doesTableExist("TYPES")) {
            database.createTable("TYPES", new String[]{
                    String.format("%s VARCHAR (100) PRIMARY KEY", "name"),
                    String.format("%s BOOLEAN", "emergency")
            });

            // generate an initial list of service types for the database
            ServiceType.createInitialServiceTypes();
        }
    }

    /**
     * Storage holder class (singleton).
     */
    private static class StorageHolder {
        private static final Storage instance = new Storage();
    }
}

