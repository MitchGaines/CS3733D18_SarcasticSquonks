/**
 * StorageTestSuite.java
 * A test suite for storage operations
 * Author: Joseph Turcotte
 * Date: March 30, 2018
 */

import data.Edge;
import data.Node;
import database.Storage;
import database.IDatabase;
import database.ApacheDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.ServiceRequest;
import service.ServiceType;
import user.LoginHandler;
import user.User;

import java.util.List;

public class StorageTestSuite {

    private IDatabase data;
    private Storage storage;

    // ------------------- STORAGE TESTS -------------------- //

    /**
     * Set up tests for the database
     */
    @Before
    public void setUp() {
        data = new ApacheDatabase("apacheDB");
        storage = Storage.getInstance();
        storage.setDatabase(data);

        // generate dummy users for database TODO perhaps apply to Nodes and Edges
//        LoginHandler lh = new LoginHandler();
//        lh.__generateDummyUsers();
    }

    // ------------------- NODE TESTS -------------------- //

    /**
     * Test adding a node to the database
     */
    @Test
    public void testAddNode() {
        Node new_node = new Node(
                "node1",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node);

        // check the length of the list of nodes returned
        List<Node> nodes = storage.getAllNodes();
        Assert.assertEquals(nodes.size(), 1);
    }

    /**
     * Test deleting a node from the database
     */
    @Test
    public void testDeleteNode() {
        Node new_node = new Node(
                "node1",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node);

        Node new_node_2 = new Node(
                "node2",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node_2);

        // delete a node from the database
        storage.deleteNode(new_node);

        // check the length and the id of the remaining node
        List<Node> nodes = storage.getAllNodes();
        Assert.assertEquals(nodes.size(), 1);
        Assert.assertEquals(nodes.get(0).getNodeID(), "node2");
    }

    /**
     * Test updating a node in the database
     */
    @Test
    public void testUpdateNode() {
        Node old_node = new Node(
                "node1",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(old_node);

        // change a field of the node
        old_node.setNodeBuilding("46 Francis");

        // update database with new node
        storage.updateNode(old_node);

        // check the field of the new node
        Node n = storage.getNodeByID(old_node.getNodeID());
        Assert.assertEquals(n.getNodeBuilding(), "46 Francis");
    }

    /**
     * Test getting a specific node by id
     */
    @Test
    public void testGetNodeByID() {
        Node new_node = new Node(
                "node1",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node);

        Node new_node_2 = new Node(
                "node2",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 39 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node_2);

        // get one of the nodes by id and check its long name
        Node n = storage.getNodeByID(new_node_2.getNodeID());
        Assert.assertEquals(n.getLongName(), "Hallway Intersection 39 Level 2");
    }

    /**
     * Test getting all nodes
     */
    @Test
    public void testGetAllNodes() {
        Node new_node = new Node(
                "node1",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 38 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node);

        Node new_node_2 = new Node(
                "node2",
                0,
                0,
                "2",
                "45 Francis",
                "HALL",
                "Hallway Intersection 39 Level 2",
                "Hallway B3802",
                "Team S",
                0,
                0
        );
        storage.saveNode(new_node_2);

        // get all nodes and check the length of the list
        List<Node> nodes = storage.getAllNodes();
        Assert.assertEquals(nodes.size(), 2);

        // also check the node ids of each entry
        Assert.assertEquals(nodes.get(0).getNodeID(), "node1");
        Assert.assertEquals(nodes.get(1).getNodeID(), "node2");
    }

    // ------------------- EDGE TESTS -------------------- //

    /**
     * Test adding an edge to the database
     */
    @Test
    public void testAddEdge() {
        Edge new_edge = new Edge(
                "edge1",
                "node1",
                "node2"
        );
        storage.saveEdge(new_edge);

        // check length of list returned by database
        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 1);

        // also check the fields of the edge added
        Assert.assertEquals(edges.get(0).getEdgeID(), "edge1");
        Assert.assertEquals(edges.get(0).getStartNode(), "node1");
        Assert.assertEquals(edges.get(0).getEndNode(), "node2");
    }

    /**
     * Test deleting an edge from the database
     */
    @Test
    public void testDeleteEdge() {
        Edge edge1 = new Edge(
                "edge1",
                "node1",
                "node2"
        );
        storage.saveEdge(edge1);

        Edge edge2 = new Edge (
                "edge2",
                "node2",
                "node3"
        );

        // delete an edge from the database
        storage.deleteEdge(edge2);

        // check length and fields of remaining node
        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 1);
        Assert.assertEquals(edges.get(0).getEdgeID(), "edge1");
    }

    /**
     * Test updating an edge in the database
     */
    @Test
    public void testUpdateEdge() {
        Edge old_edge = new Edge(
                "edge1",
                "node1",
                "node2"
        );
        storage.saveEdge(old_edge);

        // change a field of the Edge
        old_edge.setStartNode("node2");
        old_edge.setEndNode("node3");

        // update database with new fields
        storage.updateEdge(old_edge);

        // check updated fields of new Edge, and that size didn't change
        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 1);
        Assert.assertEquals(edges.get(0).getEdgeID(), "edge1");
        Assert.assertEquals(edges.get(0).getStartNode(), "node2");
        Assert.assertEquals(edges.get(0).getEndNode(), "node3");
    }

    /**
     * Test getting a specific edge by id
     */
    @Test
    public void testGetEdgeByID() {
        Edge new_edge = new Edge(
                "edge1",
                "node1",
                "node2"
        );
        storage.saveEdge(new_edge);

        Edge new_edge_2 = new Edge(
                "edge2",
                "node2",
                "node3"
        );
        storage.saveEdge(new_edge_2);

        // get one of the nodes by id and check its start node
        Edge e = storage.getEdgeByID(new_edge_2.getEdgeID());
        Assert.assertEquals(e.getStartNode(), "node2");
    }

    /**
     * Test getting all edges
     */
    @Test
    public void testGetAllEdges() {
        Edge new_edge = new Edge(
                "edge1",
                "node1",
                "node2"
        );
        storage.saveEdge(new_edge);

        Edge new_edge_2 = new Edge(
                "edge2",
                "node2",
                "node3"
        );
        storage.saveEdge(new_edge_2);

        // get all nodes and check the length of the list
        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 2);

        // also check the node ids of each entry
        Assert.assertEquals(edges.get(0).getEdgeID(), "edge1");
        Assert.assertEquals(edges.get(1).getEdgeID(), "edge2");
    }

    // ------------------- USER TESTS -------------------- //

    /**
     * Test adding a user to the database
     */
    @Test
    public void testAddUser() {
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);

        storage.saveUser(new_user);

        // check the length of the list of users returned
        List<User> users = storage.getAllUsers();
        Assert.assertEquals(users.size(), 1);
    }

    /**
     * Test deleting a user from the database
     */
    @Test
    public void testDeleteUser() {
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);

        storage.saveUser(new_user);

        User new_user2 = new User("amanda", "amanda", User.user_type.ADMIN_STAFF, true);
        new_user2.setUserID(2);

        storage.saveUser(new_user2);

        // delete a user from the database
        storage.deleteUser(new_user);

        // check the length and the id of the remaining user
        List<User> users = storage.getAllUsers();
        Assert.assertEquals(users.size(), 1);
        Assert.assertEquals(users.get(0).getUserID(), 2);
    }

    /**
     * Test updating a node in the database
     */
    @Test
    public void testUpdateUser() {
        User old_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        old_user.setUserID(1);

        storage.saveUser(old_user);

        // change a field of the user
        old_user.setCanModMap(false);

        // update database with new user
        storage.updateUser(old_user);

        // check the field of the new user
        User u = storage.getUserByID(old_user.getUserID());
        Assert.assertFalse(u.canModMap());
    }

    /**
     * Test getting a specific user by id
     */
    @Test
    public void testGetUserByID() {
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);

        storage.saveUser(new_user);

        User new_user2 = new User("amanda", "amanda", User.user_type.ADMIN_STAFF, true);
        new_user2.setUserID(2);

        storage.saveUser(new_user2);

        // get one of the users by id and check its username
        User u = storage.getUserByID(new_user2.getUserID());
        Assert.assertEquals(u.getUsername(), "amanda");
    }

    /**
     * Test getting a specific user by username
     */
    @Test
    public void testGetUserByName() {
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);

        storage.saveUser(new_user);

        User new_user2 = new User("amanda", "amanda", User.user_type.ADMIN_STAFF, true);
        new_user2.setUserID(2);

        storage.saveUser(new_user2);

        // get one of the users by username and check its modifying privileges
        User u = storage.getUserByName(new_user.getUsername());
        Assert.assertFalse(u.canModMap());
    }

    /**
     * Test getting a specific user by credentials
     */
    @Test
    public void testGetUserByCredentials() { // TODO this method uses the plain password as a check because of "'" character
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);
        storage.saveUser(new_user);

        User new_user2 = new User("amanda", "amanda", User.user_type.ADMIN_STAFF, true);
        new_user2.setUserID(2);
        storage.saveUser(new_user2);

        // get one of the users by id and check its username
        User u = storage.getUserByCredentials(new_user2.getUsername(), new_user2.getPlainPassword());
        Assert.assertEquals(u.getUsername(), "amanda");
    }

    /**
     * Test getting all users from the users table
     */
    @Test
    public void testGetAllUsers() {
        User new_user = new User("joe", "joe", User.user_type.DOCTOR, false);
        new_user.setUserID(1);

        storage.saveUser(new_user);

        User new_user2 = new User("amanda", "amanda", User.user_type.ADMIN_STAFF, true);
        new_user2.setUserID(2);

        storage.saveUser(new_user2);

        // get all of the users and check the length of the list returned
        List<User> users = storage.getAllUsers();
        Assert.assertEquals(users.size(), 2);

        // also check the username of each entry
        Assert.assertEquals(users.get(0).getUsername(), "joe");
        Assert.assertEquals(users.get(1).getUsername(), "amanda");
    }

//    // ------------------- SERVICE TESTS -------------------- //
//
//    /**
//     * Test adding a service request to the database
//     */
//    @Test
//    public void testAddRequest() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                0,
//                "Room 223"
//        );
//
//        storage.saveRequest(request);
//
//        // check the length of the list of requests returned
//        List<ServiceRequest> requests = storage.getAllServiceRequests();
//        Assert.assertEquals(requests.size(), 1);
//    }
//
//    /**
//     * Test deleting a request from the database
//     */
//    @Test
//    public void testDeleteRequest() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        User new_user2 = new User("amanda", "amanda", User.user_type.REGULAR_STAFF);
//        new_user2.setUserID(2);
//        new_user2.setCanModMap(true);
//        storage.saveUser(new_user2);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        ServiceRequest request2 = ServiceRequest.createService(
//                2,
//                "Request assistance",
//                "Patient in Room 223 needs help",
//                cleanUp,
//                new_user2.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request2);
//
//        // delete a request from the database
//        storage.deleteRequest(request);
//
//        // check the length of the list of requests returned
//        List<ServiceRequest> requests = storage.getAllServiceRequests();
//        Assert.assertEquals(requests.size(), 1);
//    }
//
//    /**
//     * Test updating a request in the database
//     */
//    @Test
//    public void testUpdateRequest() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        // now update a field of the request and update the database
//        request.setLocation("Room 224");
//        storage.updateRequest(request);
//
//        // check that the field was updated
//        List<ServiceRequest> requests = storage.getAllServiceRequests();
//        Assert.assertEquals(requests.size(), 1);
//        Assert.assertEquals(requests.get(0).getLocation(), "Room 224");
//    }
//
//    /**
//     * Test getting a service request by id
//     */
//    @Test
//    public void testGetRequestByID() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        User new_user2 = new User("amanda", "amanda", User.user_type.REGULAR_STAFF);
//        new_user2.setUserID(2);
//        new_user2.setCanModMap(true);
//        storage.saveUser(new_user2);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        ServiceRequest request2 = ServiceRequest.createService(
//                2,
//                "Request assistance",
//                "Patient in Room 223 needs help",
//                cleanUp,
//                new_user2.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request2);
//
//        // get the request by id
//        ServiceRequest gottenRequest = storage.getRequestByID(new_user.getUserID());
//
//        // check the description of the request returned
//        Assert.assertEquals(gottenRequest.getDescription(), "Cleanup required near Room 223");
//    }
//
//    /**
//     * Test getting service requests by type
//     */
//    @Test
//    public void testGetRequestsByType() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        User new_user2 = new User("amanda", "amanda", User.user_type.REGULAR_STAFF);
//        new_user2.setUserID(2);
//        new_user2.setCanModMap(true);
//        storage.saveUser(new_user2);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//        ServiceType emergency = ServiceType.createServiceType("emergency", true, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        ServiceRequest request2 = ServiceRequest.createService(
//                2,
//                "Request assistance",
//                "Patient in Room 223 needs help",
//                cleanUp,
//                new_user2.getUserID(),
//                0,
//                "Room 223"
//        );
//        storage.saveRequest(request2);
//
//        ServiceRequest request3 = ServiceRequest.createService(
//                3,
//                "Request assistance",
//                "Patient fell in hallway",
//                emergency,
//                new_user2.getUserID(),
//                0,
//                "Room 204"
//        );
//        storage.saveRequest(request3);
//
//        // get all requests of type cleanUp and check its length
//        List<ServiceRequest> requests = storage.getRequestsByType(cleanUp);
//        Assert.assertEquals(requests.size(), 2);
//    }
//
//    /**
//     * Test getting all service requests assigned to a given user
//     */
//    @Test
//    public void testGetRequestsForUser() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        User new_user2 = new User("amanda", "amanda", User.user_type.REGULAR_STAFF);
//        new_user2.setUserID(2);
//        new_user2.setCanModMap(true);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//        ServiceType emergency = ServiceType.createServiceType("emergency", true, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                new_user2.getUserID(),
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        ServiceRequest request2 = ServiceRequest.createService(
//                2,
//                "Request assistance",
//                "Patient in Room 223 needs help",
//                cleanUp,
//                new_user2.getUserID(),
//                new_user.getUserID(),
//                "Room 223"
//        );
//        storage.saveRequest(request2);
//
//        ServiceRequest request3 = ServiceRequest.createService(
//                3,
//                "Request assistance",
//                "Patient fell in hallway",
//                emergency,
//                new_user2.getUserID(),
//                new_user.getUserID(),
//                "Room 204"
//        );
//        storage.saveRequest(request3);
//
//        // get all requests assigned to a user and check its length
//        List<ServiceRequest> requests = storage.getAllRequestsAssignedToUser(new_user);
//        Assert.assertEquals(requests.size(), 2);
//    }
//
//    /**
//     * Test getting all service requests by a given user
//     */
//    @Test
//    public void testGetRequestsByUser() {
//        User new_user = new User("joe", "joe", User.user_type.DOCTOR);
//        new_user.setUserID(1);
//        new_user.setCanModMap(true);
//        storage.saveUser(new_user);
//
//        User new_user2 = new User("amanda", "amanda", User.user_type.REGULAR_STAFF);
//        new_user2.setUserID(2);
//        new_user2.setCanModMap(true);
//        storage.saveUser(new_user2);
//
//        ServiceType cleanUp = ServiceType.createServiceType("cleanup", false, null);
//        ServiceType emergency = ServiceType.createServiceType("emergency", true, null);
//
//        ServiceRequest request = ServiceRequest.createService(
//                1,
//                "Request cleanup",
//                "Cleanup required near Room 223",
//                cleanUp,
//                new_user.getUserID(),
//                new_user2.getUserID(),
//                "Room 223"
//        );
//        storage.saveRequest(request);
//
//        ServiceRequest request2 = ServiceRequest.createService(
//                2,
//                "Request assistance",
//                "Patient in Room 223 needs help",
//                cleanUp,
//                new_user2.getUserID(),
//                new_user.getUserID(),
//                "Room 223"
//        );
//        storage.saveRequest(request2);
//
//        ServiceRequest request3 = ServiceRequest.createService(
//                3,
//                "Request assistance",
//                "Patient fell in hallway",
//                emergency,
//                new_user2.getUserID(),
//                new_user.getUserID(),
//                "Room 204"
//        );
//        storage.saveRequest(request3);
//
//        // get all requests assigned to a user and check its length
//        List<ServiceRequest> requests = storage.getAllRequestsByUser(new_user2);
//        Assert.assertEquals(requests.size(), 2);
//
//        // TODO this test fails because of ServiceType issues
//        //Assert.assertTrue(requests.get(1).getServiceType().isEmergency());
//    }

    @After
    public void breakDown() {
        // drop tables at the end
        storage.getDatabase().dropTable("NODES");
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("USERS");
//        storage.getDatabase().dropTable("SERVICES");
    }
}

