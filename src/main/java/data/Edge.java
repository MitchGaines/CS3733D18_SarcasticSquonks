package data;

/**
 * Edge.java
 * Entity class to hold Edge information
 * Author: Joseph Turcotte
 * Date: March 24, 2018
 */

public class Edge {

    // fields of edge
    String edge_id; // unique id of edge
    String start_node; // start node of edge TODO: change to Node class?
    String end_node; // end node of edge

    // constructor
    public Edge(String edge_id, String start_node, String end_node) {
        this.edge_id = edge_id;
        this.start_node = start_node;
        this.end_node = end_node;
    }

    // getters and setters
    public String getEdgeID() {
        return this.edge_id;
    }

    public void setEdgeID(String edge_id) {
        this.edge_id = edge_id;
    }

    public String getStartNode() {
        return this.start_node;
    }

    public void setStartNode(String start_node) {
        this.start_node = start_node;
    }

    public String getEndNode() {
        return this.end_node;
    }

    public void setEndNode(String end_node) {
        this.end_node = end_node;
    }
}
