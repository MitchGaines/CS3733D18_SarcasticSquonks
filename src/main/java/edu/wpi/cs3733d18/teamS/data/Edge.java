package edu.wpi.cs3733d18.teamS.data;

/**
 * Edge is an Entity class to hold information on Edges
 * which connects the map Nodes. One Edge object has both a
 * start node and an end node
 * Date: March 24, 2018
 *
 * @author Joseph Turcotte
 * @version 1.0
 */

public class Edge {

    /**
     * Stores the edge id.
     */
    String edge_id; // unique id of edge
    /**
     * Stores the start node.
     */
    String start_node; // start node of edge
    /**
     * Stores the end node.
     */
    String end_node; // end node of edge

    /**
     * Tells whether an edge is disabled or not.
     */
    private boolean disabled; // tells whether the edge is mutable

    /**
     * Constructs and Edge with an id, start node and end node.
     * @param edge_id    the edge id.
     * @param start_node the start node of the edge.
     * @param end_node   the end node of the edge.
     * @param disabled a boolean for whether or not the Edge is disabled.
     */
    public Edge(String edge_id, String start_node, String end_node, boolean disabled) {
        this.edge_id = edge_id;
        this.start_node = start_node;
        this.end_node = end_node;
        this.disabled = disabled;
    }

    /**
     * Retrieves the edge id.
     *
     * @return the edge id.
     */
    public String getEdgeID() {
        return this.edge_id;
    }

    /**
     * Sets the edge id.
     *
     * @param edge_id the edge id.
     */
    public void setEdgeID(String edge_id) {
        this.edge_id = edge_id;
    }

    /**
     * Retrieves the start node.
     *
     * @return the start node.
     */
    public String getStartNode() {
        return this.start_node;
    }

    /**
     * Sets the start node.
     *
     * @param start_node the start node.
     */
    public void setStartNode(String start_node) {
        this.start_node = start_node;
    }

    /**
     * Retrieves the end node.
     *
     * @return the end node.
     */
    public String getEndNode() {
        return this.end_node;
    }

    /**
     * Sets the end node.
     *
     * @param end_node the end node.
     */
    public void setEndNode(String end_node) {
        this.end_node = end_node;
    }

    public boolean isDisabled(){
        return disabled;
    }

    public void setDisabled(boolean disabled){
        this.disabled = disabled;
    }
}
