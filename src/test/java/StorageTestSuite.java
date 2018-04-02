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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        storage = new Storage();
    }

    /**
     * Test adding a node to the database
     */
    @Test
    public void testAddNode() {

        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("NODES");
    }

    /**
     * Test deleting a node from the database
     */
    @Test
    public void testDeleteNode() {

        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("NODES");
    }

    /**
     * Test updating a node in the database
     */
    @Test
    public void testUpdateNode() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("NODES");
    }

    /**
     * Test getting a specific node by id
     */
    @Test
    public void testGetNodeByID() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("NODES");
    }

    /**
     * Test getting all nodes
     */
    @Test
    public void testGetAllNodes() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("NODES");
    }

    /**
     * Test adding an edge to the database
     */
    @Test
    public void testAddEdge() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("EDGES");
    }

    /**
     * Test deleting an edge from the database
     */
    @Test
    public void testDeleteEdge() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("EDGES");
    }

    /**
     * Test updating an edge in the database
     */
    @Test
    public void testUpdateEdge() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("EDGES");
    }

    /**
     * Test getting a specific edge by id
     */
    @Test
    public void testGetEdgeByID() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("EDGES");
    }

    /**
     * Test getting all edges
     */
    @Test
    public void testGetAllEdges() {
        storage.setDatabase(data);

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

        // drop table at the end
        storage.getDatabase().dropTable("EDGES");
    }
}

