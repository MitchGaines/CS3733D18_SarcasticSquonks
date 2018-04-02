package data;

/*
 * Node.java
 * Entity class to hold Node information
 * Author: Joseph Turcotte
 * Date: March 24, 2018
 */

public class Node { // TODO: add 3d coordinate fields

    // fields of Node
    private String node_id; // unique id of node
    private int x_coord; // x coordinate of node
    private int y_coord; // y coordinate of node
    private String floor; // floor of node
    private String building; // building of node
    private String node_type; // type of node
    private String long_name; // unabbreviated name of node
    private String short_name; // abbreviated name of node
    private String team_assigned; // team assigned to this node
    private int x_coord_3d; // 3d x coordinate of node
    private int y_coord_3d; // 3d y coordinate of node

    // node constructor
    public Node(String id, int x, int y, String floor, String bldg, String type,
                String lName, String sName, String team, int x3d, int y3d) {
        this.node_id = id;
        this.x_coord = x;
        this.y_coord = y;
        this.floor = floor;
        this.building = bldg;
        this.node_type = type;
        this.long_name = lName;
        this.short_name = sName;
        this.team_assigned = team;
        this.x_coord_3d = x3d;
        this.y_coord_3d = y3d;
    }

    // getters and setters
    public String getNodeID() {
        return this.node_id;
    }

    public void setNodeID(String node_id) {
        this.node_id = node_id;
    }

    public int getXCoord() {
        return this.x_coord;
    }

    public void setXCoord(int x_coord) {
        this.x_coord = x_coord;
    }

    public int getYCoord() {
        return this.y_coord;
    }

    public void setYCoord(int y_coord) {
        this.y_coord = y_coord;
    }

    public String getNodeFloor() {
        return this.floor;
    }

    public void setNodeFloor(String floor) {
        this.floor = floor;
    }

    public String getNodeBuilding() {
        return this.building;
    }

    public void setNodeBuilding(String building) {
        this.building = building;
    }

    public String getNodeType() {
        return this.node_type;
    }

    public void setNodeType(String node_type) {
        this.node_type = node_type;
    }

    public String getLongName() {
        return this.long_name;
    }

    public void setLongName(String long_name) {
        this.long_name = long_name;
    }

    public String getShortName() {
        return this.short_name;
    }

    public void setShortName(String short_name) {
        this.node_type = short_name;
    }

    public String getTeamAssigned() {
        return this.team_assigned;
    }

    public void setTeamAssigned(String team_assigned) {
        this.team_assigned = team_assigned;
    }

    public int getXCoord3D() {
        return this.x_coord_3d;
    }

    public void setXCoord3D(int x_coord_3d) {
        this.x_coord_3d = x_coord_3d;
    }

    public int getYCoord3D() {
        return this.y_coord_3d;
    }

    public void setYCoord3D(int y_coord_3d) {
        this.y_coord_3d = y_coord_3d;
    }

}

