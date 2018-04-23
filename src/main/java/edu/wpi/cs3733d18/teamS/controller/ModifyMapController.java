package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.matrix.Matrix;
import edu.wpi.cs3733d18.teamS.data.Edge;
import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.data.Node3DPredictor;
import edu.wpi.cs3733d18.teamS.database.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for modifying the map and methods related to it.
 *
 * @author Matthew McMillan
 * @author Mitch Gaines
 * @author Joe Turcotte
 * @author Cormac Lynch-Collier
 * @author Matthew Puentes
 * @author Danny Sullivan
 * @version 1.3, April 13, 2018
 */
public class ModifyMapController {

    /**
     * Stores a double for the zoom factor.
     */
    private double zoom_factor;

    /**
     * A HashMap with Nodes for keys and Circles for values to represent the node list.
     */
    private HashMap<edu.wpi.cs3733d18.teamS.data.Node, Circle> nodes_list;

    /**
     * A HashMap with Edges for keys and Lines for values to represent the edge list.
     */
    private HashMap<edu.wpi.cs3733d18.teamS.data.Edge, Line> edge_list;

    /**
     * The database storage.
     */
    private Storage storage;

    /**
     * Stores a HashMap with Strings as the value and key to represent the locations.
     */
    private HashMap<String, String> locations;

    /**
     * Stores a HashMap with Strings as the value and key to represent the floor map.
     */
    private HashMap<String, String> floor_map;

    private edu.wpi.cs3733d18.teamS.data.Node start_of_edge;

    private edu.wpi.cs3733d18.teamS.data.Node new_node;

    private Node changing_node;

    /**
     * Stores a HashMap with Edges for the Key and Lines for the value to represent the edges to be deleted.
     */
    private HashMap<Edge, Line> edges_to_delete;

    /**
     * Stores a HashMap with Nodes for the key and Circles for the value to represent nodes to move.
     */
    private HashMap<Node, Circle> nodes_to_move;

    /**
     * Stores a HashMap with Nodes for the key and Circles for the value to represent moved nodes.
     */
    private HashMap<Node, Circle> movedNodes;

    @FXML
    JFXToggleButton toggle3D;

    @FXML
    Label add_loc_fail, time;

    @FXML
    TextField building, long_name, short_name, kiosk_location_name, location_one, location_two, location_to_delete, kiosk_location;

    @FXML
    TextField building_change, long_name_change, short_name_change;

    @FXML
    JFXComboBox loc_type, location_or_path, choose_floor, loc_type_change;

    @FXML
    AnchorPane pane;

    @FXML
    Button add_loc_cancel, add_loc, back_btn, confirm_change;

    @FXML
    BorderPane main_pane;

    @FXML
    ScrollPane scroll_pane;
    @FXML
    ImageView map;

    @FXML
    VBox add_node_box, add_edge_box, delete_loc_box, delete_edge_box, modify_loc_box, batch_disable_box, modify_info_box;

    @FXML
    Button confirm_3d;

    @FXML
    Text user_name;

    @FXML
    Circle add_btn, remove_btn;

    /**
     * Stores a color code.
     */
    private Color color = Color.web("#4863A0");

    /**
     * Stores a circle for a temporary pin
     */
    private Circle temp_pin;

    /**
     * Stores a boolean for the first click
     */
    private Boolean first_click;

    /**
     * Stores the node for the first location.
     */
    private edu.wpi.cs3733d18.teamS.data.Node first_loc;

    /**
     * Stores the node for the second location.
     */
    private edu.wpi.cs3733d18.teamS.data.Node second_loc;

    /**
     * Stores a HashMap of Nodes for the Keys and Circles for the Values to represent the entries to delete.
     */
    private HashMap<Node, Circle> entry_to_delete;

    /**
     * Stores a new 3d node predictor.
     */
    private Node3DPredictor predictor = new Node3DPredictor();

    /**
     * Stores a polygon.
     */
    private Polygon geoBlock = new Polygon();

    /**
     * Initializes the scene.
     */
    @FXML
    private void initialize() {
        first_click = true;

        zoom_factor = 1;

        scroll_pane.setVvalue(0.5);
        scroll_pane.setHvalue(0.25);

        Image add_img = new Image("images/add.jpg");
        Image remove_img = new Image("images/remove.png");

        add_btn.setFill(new ImagePattern(add_img));
        remove_btn.setFill(new ImagePattern(remove_img));

        edges_to_delete = new HashMap<>();
        entry_to_delete = new HashMap<>();
        nodes_to_move = new HashMap<>();
        movedNodes = new HashMap<>();

        ObservableList<String> list_type = FXCollections.observableArrayList();
        list_type.addAll("Conference", "Hallway", "Department", "Information", "Laboratory", "Restroom", "Stairs", "Service");
        loc_type.setItems(list_type);
        loc_type_change.setItems(list_type);

        //Hashmap for node constructor
        List<String> short_name = new ArrayList<>();
        short_name.add("CONF");
        short_name.add("HALL");
        short_name.add("DEPT");
        short_name.add("INFO");
        short_name.add("LABS");
        short_name.add("REST");
        short_name.add("STAI");
        short_name.add("SERV");

        locations = new HashMap<>();
        for (int i = 0; i < list_type.size(); i++) {
            locations.put(list_type.get(i), short_name.get(i));
        }

        ObservableList<String> floors = FXCollections.observableArrayList();
        floors.addAll("Lower Level Two", "Lower Level One", "Ground Floor", "First Floor", "Second Floor", "Third Floor");
        choose_floor.setItems(floors);

        List<String> short_floor = new ArrayList<>();
        short_floor.add("L2");
        short_floor.add("L1");
        short_floor.add("G");
        short_floor.add("1");
        short_floor.add("2");
        short_floor.add("3");

        floor_map = new HashMap<>();
        for (int i = 0; i < floors.size(); i++) {
            floor_map.put(floors.get(i), short_floor.get(i));
        }

        choose_floor.getSelectionModel().select(3);

        pane.getChildren().clear();
        pane.getChildren().add(map);
        nodes_list = new HashMap<>();
        edge_list = new HashMap<>();
        storage = Storage.getInstance();
        makeMap(storage.getAllNodes(), storage.getAllEdges());


        ObservableList<String> lop = FXCollections.observableArrayList();
        lop.addAll("View Map", "Add Location", "Add Path", "Delete Location", "Delete Path", "Modify Locations", "Set Kiosk Location", "Batch Disable Locations");
        location_or_path.setItems(lop);
        location_or_path.getSelectionModel().selectFirst();

        list_type = FXCollections.observableArrayList();
        list_type.addAll("Conference", "Hallway", "Department", "Information", "Laboratory", "Restroom", "Stairs", "Service");
        loc_type.setItems(list_type);

        //Hashmap for node constructor
        short_name = new ArrayList<>();
        short_name.add("CONF");
        short_name.add("HALL");
        short_name.add("DEPT");
        short_name.add("INFO");
        short_name.add("LABS");
        short_name.add("REST");
        short_name.add("STAI");
        short_name.add("SERV");

        locations = new HashMap<>();
        for (int i = 0; i < list_type.size(); i++) {
            locations.put(list_type.get(i), short_name.get(i));
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));

        predictor.start(storage);
    }

    /**
     * Sets up factors for when zoom out is clicked.
     */
    public void onZoomOut() {
        if (zoom_factor > 0.5) {
            zoom_factor -= 0.2;
            scroll_pane.getContent().setScaleX(zoom_factor);
            scroll_pane.setLayoutX(zoom_factor);
            scroll_pane.getContent().setScaleY(zoom_factor);
            scroll_pane.setLayoutY(zoom_factor);

        }
    }

    /**
     * Sets up factors for when zoom in is clicked.
     */
    public void onZoomIn() {
        if (zoom_factor < 1.8) {
            zoom_factor += 0.2;
            scroll_pane.getContent().setScaleX(zoom_factor);
            scroll_pane.setLayoutX(zoom_factor);
            scroll_pane.getContent().setScaleY(zoom_factor);
            scroll_pane.setLayoutY(zoom_factor);
        }
    }

    /**
     * Changes the 2d map image when the floor is selected.
     */
    public void onChooseFloorChange() {
        pane.getChildren().clear();
        nodes_list.clear();
        pane.getChildren().add(map);
        switch (choose_floor.getValue().toString()) {
            case "Ground Floor":
                map.setImage(new Image("images/2dMaps/00_thegroundfloor.png"));
                break;
            case "Lower Level One":
                map.setImage(new Image("images/2dMaps/00_thelowerlevel1.png"));
                break;
            case "Lower Level Two":
                map.setImage(new Image("images/2dMaps/00_thelowerlevel2.png"));
                break;
            case "First Floor":
                map.setImage(new Image("images/2dMaps/01_thefirstfloor.png"));
                break;
            case "Second Floor":
                map.setImage(new Image("images/2dMaps/02_thesecondfloor.png"));
                break;
            case "Third Floor":
                map.setImage(new Image("images/2dMaps/03_thethirdfloor.png"));
                break;
        }
        toggle3D.setSelected(false);
        makeMap(storage.getAllNodes(), storage.getAllEdges());
    }

    /**
     * Switches scenes when the back button is clicked.
     *
     * @param event the click.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void onBackClick(ActionEvent event) throws IOException {
        Main.switchScenes("User", "/AdminPage.fxml");
    }

    /**
     * Adds a location to the map.
     */
    public void onAddLocClick() {
        if (!building.getText().equals("") && !loc_type.getSelectionModel().isEmpty() && !long_name.getText().equals("") && !short_name.getText().equals("")) {
            Scene scene = add_loc.getScene();
            Circle circ_cursor = new Circle(7, color);
            circ_cursor.setStroke(Color.BLACK);
            circ_cursor.setStrokeWidth(3);

            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            Image image = circ_cursor.snapshot(sp, null);

            scene.setCursor(new ImageCursor(image, 17, 17));

            add_loc_cancel.setVisible(true);
            add_loc_fail.setText("");
        } else {
            add_loc_fail.setText("Please fill all fields");
        }
    }

    /**
     * Cancels adding a location to the map.
     */
    public void onAddLocCancelClick() {
        Scene scene = add_loc.getScene();
        scene.setCursor(Cursor.DEFAULT);
        add_loc_cancel.setVisible(false);
    }

    /**
     * Allows the user to choose an action, either add path, add, location, view map, delete location, delete path,
     * modify location, set the kiosk's default location, or batch disabled locations.
     */
    public void onChooseAction() {
        clearOptions();
        switch (location_or_path.getValue().toString()) {
            case "Add Path":
                add_edge_box.setVisible(true);
                break;
            case "Add Location":
                add_node_box.setVisible(true);
                break;
            case "View Map":
                break;
            case "Delete Location":
                delete_loc_box.setVisible(true);
                break;
            case "Delete Path":
                delete_edge_box.setVisible(true);
                break;
            case "Modify Locations":
                modify_loc_box.setVisible(true);
                scroll_pane.setPannable(false);
                break;
            case "Set Kiosk Location":
                kiosk_location_name.setVisible(true);
                break;
            case "Batch Disable Locations":
                batch_disable_box.setVisible(true);
                geoBlock = new Polygon();
                geoBlock.setId("polygon");
                geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
                geoBlock.setStroke(Color.GREEN);
                break;
        }
    }

    public void clearOptions() {
        scroll_pane.setPannable(true);
        add_edge_box.setVisible(false);
        add_node_box.setVisible(false);
        delete_loc_box.setVisible(false);
        kiosk_location_name.setVisible(false);
        delete_edge_box.setVisible(false);
        modify_loc_box.setVisible(false);
        batch_disable_box.setVisible(false);
        modify_info_box.setVisible(false);
        pane.setOnMouseMoved(null);
        add_loc.setVisible(true);
        removePaneChild();
        for(Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
            entry.getValue().setFill(color);
        }
    }

    /**
     * Confirms switching to the 3d map.
     */
    public void onConfirm3dClick() {
        pane.getChildren().clear();
        pane.getChildren().add(map);
        map.setImage(new Image(choose2DMap()));
        makeMap(storage.getAllNodes(), storage.getAllEdges());

        Circle pin = new Circle(new_node.getXCoord(), new_node.getYCoord(), 7, color);
        pin.setStroke(Color.BLACK);
        pin.setStrokeWidth(3);

        nodes_list.put(new_node, pin);
        storage.saveNode(new_node);
        addClickProperty(new_node, pin);

        pane.getChildren().add(pin);

        endAddLoc();

        confirm_3d.setVisible(false);
        choose_floor.setVisible(true);
    }

    /**
     * Returns the file name for the 2d map image.
     *
     * @return the file name.
     */
    private String choose2DMap() {
        String toReturn = "";
        switch (choose_floor.getValue().toString()) {
            case "Ground Floor":
                toReturn = "images/2dMaps/00_thegroundfloor.png";
                break;
            case "Lower Level One":
                toReturn = "images/2dMaps/00_thelowerlevel1.png";
                break;
            case "Lower Level Two":
                toReturn = "images/2dMaps/00_thelowerlevel2.png";
                break;
            case "First Floor":
                toReturn = "images/2dMaps/01_thefirstfloor.png";
                break;
            case "Second Floor":
                toReturn = "images/2dMaps/02_thesecondfloor.png";
                break;
            case "Third Floor":
                toReturn = "images/2dMaps/03_thethirdfloor.png";
                break;
        }
        return toReturn;
    }

    /**
     * Adds edges, nodes, etc to the map given certain parameters and where the mouse is clicked.
     * @param click the mouse is clicked.
     */

    /**
     * Disables the nodes in the highlighted area.
     */
    public void disableNodesInPolygon() {
        ArrayList<Node> intersecting_nodes = getIntersectingNodes();
        for (Node n : intersecting_nodes) {
            n.setDisabled(true);
            storage.updateNode(n);
        }
        checkEdges();
        removePaneChild();
        geoBlock = new Polygon();
        geoBlock.setId("polygon");
        geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
        geoBlock.setStroke(Color.GRAY);
        pane.setOnMouseMoved(null);

        makeMap(new ArrayList<>(nodes_list.keySet()), new ArrayList<>(edge_list.keySet()));

        for (Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
            if (!entry.getValue().getFill().equals(Color.YELLOW)) {
                if (entry.getKey().isDisabled()) {
                    entry.getValue().setFill(Color.GRAY);
                }
            }
        }
    }

    /**
     * Enables the nodes in the highlighted area.
     */
    public void enableNodesInPolygon() {
        ArrayList<Node> intersecting_nodes = getIntersectingNodes();
        for (Node n : intersecting_nodes) {
            n.setDisabled(false);
            storage.updateNode(n);
        }
        checkEdges();
        removePaneChild();
        geoBlock = new Polygon();
        geoBlock.setId("polygon");
        geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
        geoBlock.setStroke(Color.GREEN);
        pane.setOnMouseMoved(null);

        makeMap(new ArrayList<>(nodes_list.keySet()), new ArrayList<>(edge_list.keySet()));

        for (Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
            if (!entry.getValue().getFill().equals(Color.YELLOW)) {
                if (entry.getKey().isDisabled()) {
                    entry.getValue().setFill(Color.GRAY);
                }
            }
        }
    }

    /**
     * Checks to see if the edges are disabled or not.
     */
    public void checkEdges() {
        for (Edge e : edge_list.keySet()) {
            if (storage.getNodeByID(e.getStartNode()).isDisabled()
                    || storage.getNodeByID(e.getEndNode()).isDisabled()) {
                e.setDisabled(true);
                storage.updateEdge(e);
            } else {
                e.setDisabled(false);
                storage.updateEdge(e);
            }
        }
    }

    /**
     * Retrieves the nodes that intersect an area.
     *
     * @return the intersecting nodes.
     */
    private ArrayList<Node> getIntersectingNodes() {
        ArrayList<Node> intersecting_nodes = new ArrayList<>();
        if (geoBlock.getPoints().size() == 0) {
            return intersecting_nodes;
        }
        for (Node node : nodes_list.keySet()) {
            if (!Shape.intersect(nodes_list.get(node), geoBlock).getBoundsInParent().isEmpty()) {
                intersecting_nodes.add(node);
            }
        }
        return intersecting_nodes;
    }

    /**
     * Removes anything in the pane.
     */
    private void removePaneChild() {
        if (pane.getChildren().size() > 0) {
            removePaneChild("previewLine");
            removePaneChild("polygon");
        }
    }

    /**
     * Removes anything in the pane.
     *
     * @param id the id of the a specific object in the pane.
     */
    private void removePaneChild(String id) {
        int index = -1;
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i).getId() == id) {
                index = i;
            }
        }
        if (index != -1) {
            pane.getChildren().remove(index);
        }
    }

    /**
     * Creates a preview line by taking in the starting 2d coordinates.
     *
     * @param start_x the initial x coordinate.
     * @param start_y the initial y coordinate.
     */
    private void createPreviewLine(final double start_x, final double start_y) {
        pane.setOnMouseMoved(event -> {
            removePaneChild("previewLine");
            Line l = new Line(start_x, start_y, event.getX(), event.getY());
            l.setId("previewLine");
            pane.getChildren().add(l);
        });

        pane.setOnMouseExited(event -> removePaneChild("previewLine"));
    }

    /**
     * Selects the 3d map.
     *
     * @return the 3d map file image.
     */
    private String select3DMap() {
        String toReturn = "";
        switch (choose_floor.getValue().toString()) {
            case "Ground Floor":
                toReturn = "images/2dMaps/00_thegroundfloor.png";
                break;
            case "Lower Level One":
                toReturn = "images/3dMaps/L1-NO-ICONS.png";
                break;
            case "Lower Level Two":
                toReturn = "images/3dMaps/L2-NO-ICONS.png";
                break;
            case "First Floor":
                toReturn = "images/3dMaps/1-NO-ICONS.png";
                break;
            case "Second Floor":
                toReturn = "images/3dMaps/2-NO-ICONS.png";
                break;
            case "Third Floor":
                toReturn = "images/3dMaps/3-NO-ICONS.png";
                break;
        }
        return toReturn;
    }

    /**
     * Stores a Circle for moving nodes.
     */
    Circle to_move = new Circle();

    /**
     * relocates the nodes when the mouse is released.
     *
     * @param event the mouse is depressed.
     */
    private void released(MouseEvent event) {
        movedNodes.putAll(nodes_to_move);
    }

    /**
     * Drags objects when the mouse is clicked and held.
     *
     * @param event the click and hold of the mouse.
     */
    public void drag(MouseEvent event) {

        if (location_or_path.getValue().toString().equals("Modify Locations")) {
            to_move = (Circle) event.getSource();
            double org_x = to_move.getCenterX();
            double org_y = to_move.getCenterY();
            to_move.setCenterX(event.getX());
            to_move.setCenterY(event.getY());
            double x_change = event.getX() - org_x;
            double y_change = event.getY() - org_y;
            moveAll(to_move, x_change, y_change);
        }

    }

    /**
     * Moves the selected objects.
     *
     * @param to_move the highlighted area to move.
     * @param x       the x translation.
     * @param y       the y translation.
     */
    private void moveAll(Circle to_move, double x, double y) {
        for (Map.Entry<Node, Circle> entry : nodes_to_move.entrySet()) {
            if (!entry.getValue().equals(to_move)) {
                entry.getValue().setCenterX(entry.getValue().getCenterX() + x);
                entry.getValue().setCenterY(entry.getValue().getCenterY() + y);
            }
        }
    }

    /**
     * Confirms moving the objects.
     */
    public void onConfirmMove() {
        for (Map.Entry<Node, Circle> entry : movedNodes.entrySet()) {
            Node node = storage.getNodeByID(entry.getKey().getNodeID());
            if (toggle3D.isSelected()) {
                node.setXCoord3D((int) entry.getValue().getCenterX());
                node.setYCoord3D((int) entry.getValue().getCenterY());
            } else {
                node.setXCoord((int) entry.getValue().getCenterX());
                node.setYCoord((int) entry.getValue().getCenterY());
            }
            storage.updateNode(node);
            entry.getValue().setFill(color);
        }
        makeMap(storage.getAllNodes(), storage.getAllEdges());

    }

    /**
     * Cancels moving the objects.
     */
    public void onCancelMove() {
        for (Map.Entry<Node, Circle> entry : nodes_to_move.entrySet()) {
            entry.getValue().setFill(color);
        }
        nodes_to_move.clear();
        makeMap(storage.getAllNodes(), storage.getAllEdges());

    }

    /**
     * Deletes an edge.
     */
    public void onDeleteEdge() {
        for (Map.Entry<Edge, Line> line : edges_to_delete.entrySet()) {
            pane.getChildren().remove(line.getValue());
            storage.deleteEdge(line.getKey());
        }
    }

    /**
     * Cancels deleting an edge.
     */
    public void onCancelDeleteEdge() {
        for (Map.Entry<Edge, Line> line : edges_to_delete.entrySet()) {
            edge_list.put(line.getKey(), line.getValue());
            line.getValue().setStroke(Color.BLACK);
        }
        location_or_path.getSelectionModel().selectFirst();
        delete_edge_box.setVisible(false);
    }

    /**
     * Deletes an object on the click of a location.
     */
    public void onDeleteLocClick() {
        if (!location_to_delete.getText().trim().isEmpty()) {
            for (Map.Entry<Node, Circle> entry : entry_to_delete.entrySet()) {
                pane.getChildren().remove(entry.getValue());
                storage.deleteNode(entry.getKey());
                nodes_list.remove(entry);
                deleteEdge(entry.getKey());
            }
        }
        location_to_delete.setText("");
    }

    /**
     * Cancels deleting a object on the click of a location.
     */
    public void onDeleteLocCancelClick() {
        for (Map.Entry<Node, Circle> entry : entry_to_delete.entrySet()) {
            nodes_list.put(entry.getKey(), entry.getValue());
            entry.getValue().setFill(color);
        }
        location_to_delete.setText("");
    }

    /**
     * Adds an edge when clicked.
     */
    public void onAddEdgeClick() {
        if (!location_one.getText().trim().isEmpty() && !location_two.getText().trim().isEmpty()) {
            Edge new_edge = new edu.wpi.cs3733d18.teamS.data.Edge(generateEdgeId(first_loc, second_loc), first_loc.getNodeID(), second_loc.getNodeID(), false);
            storage.saveEdge(new_edge);

            double startX = storage.getNodeByID(new_edge.getStartNode()).getXCoord();
            double startY = storage.getNodeByID(new_edge.getStartNode()).getYCoord();
            double endX = storage.getNodeByID(new_edge.getEndNode()).getXCoord();
            double endY = storage.getNodeByID(new_edge.getEndNode()).getYCoord();

            if (toggle3D.isSelected()) {
                startX = storage.getNodeByID(new_edge.getStartNode()).getXCoord();
                startY = storage.getNodeByID(new_edge.getStartNode()).getYCoord();
                endX = storage.getNodeByID(new_edge.getEndNode()).getXCoord();
                endY = storage.getNodeByID(new_edge.getEndNode()).getYCoord();
            }

            Line path = new Line(startX, startY, endX, endY);
            path.setStrokeWidth(3);
            pane.getChildren().add(path);
            edge_list.put(new_edge, path);
            onCancelEdgeClick();
        }
    }

    /**
     * Cancels adding an edge on click.
     */
    public void onCancelEdgeClick() {
        first_click = true;
        location_one.setText("");
        location_two.setText("");
        nodes_list.get(first_loc).setFill(color);
        nodes_list.get(second_loc).setFill(color);
    }

    /**
     * makes the map.
     *
     * @param dataNodes the nodes on the map.
     * @param dataEdges the edges on the map.
     */
    private void makeMap(List<edu.wpi.cs3733d18.teamS.data.Node> dataNodes, List<edu.wpi.cs3733d18.teamS.data.Edge> dataEdges) {
        pane.getChildren().removeAll(nodes_list.values());
        pane.getChildren().removeAll(edge_list.values());
        nodes_list.clear();
        edge_list.clear();
        nodes_to_move.clear();
        movedNodes.clear();
        String floor = choose_floor.getValue().toString();
        String short_floor = floor_map.get(choose_floor.getValue().toString());
        String small_ft = ("00" + short_floor).substring(short_floor.length());
        for (edu.wpi.cs3733d18.teamS.data.Edge a_edge : dataEdges) {
            String start = a_edge.getStartNode().substring(8);
            String end = a_edge.getEndNode().substring(8);
            if (start.equals(small_ft) && end.equals(small_ft)) {
                try {
                    double startX, startY, endX, endY;
                    if (toggle3D.isSelected()) {
                        startX = storage.getNodeByID(a_edge.getStartNode()).getXCoord3D();
                        startY = storage.getNodeByID(a_edge.getStartNode()).getYCoord3D();
                        endX = storage.getNodeByID(a_edge.getEndNode()).getXCoord3D();
                        endY = storage.getNodeByID(a_edge.getEndNode()).getYCoord3D();
                    } else {
                        startX = storage.getNodeByID(a_edge.getStartNode()).getXCoord();
                        startY = storage.getNodeByID(a_edge.getStartNode()).getYCoord();
                        endX = storage.getNodeByID(a_edge.getEndNode()).getXCoord();
                        endY = storage.getNodeByID(a_edge.getEndNode()).getYCoord();
                    }
                    Line path = new Line(startX, startY, endX, endY);
                    path.setStrokeWidth(3);
                    if (a_edge.isDisabled()) {
                        path.setStroke(Color.GRAY);
                    }
                    pane.getChildren().add(path);
                    edge_list.put(a_edge, path);
                } catch (NullPointerException e) {

                }
            }
        }
        for (edu.wpi.cs3733d18.teamS.data.Node a_node : dataNodes) {
            if (!a_node.getShortName().equals("TRAIN")) {
                if (a_node.getNodeFloor().equals(floor_map.get(floor))) {
                    Circle pin;
                    if (toggle3D.isSelected()) {
                        pin = new Circle(a_node.getXCoord3D(), a_node.getYCoord3D(), 8, color);
                    } else {
                        pin = new Circle(a_node.getXCoord(), a_node.getYCoord(), 8, color);
                    }
                    pin.setStroke(Color.BLACK);
                    pin.setStrokeWidth(3);
                    addClickProperty(a_node, pin);
                    if (a_node.isDisabled()) {
                        pin.setFill(Color.GRAY);
                    }
                    nodes_list.put(a_node, pin);
                    pane.getChildren().add(pin);
                }
            }
        }
    }

    private void addClickProperty(Node node, Circle pin) {
        pin.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getButton() == MouseButton.SECONDARY) {
                    rightClick(node, pin);
                } else {
                    switch (location_or_path.getValue().toString()) {
                        case "Add Path":
                            clickOptionAddPath(node, pin);
                            break;
                        case "Delete Location":
                            clickOptionDeleteLoc(node, pin);
                            break;
                        case "Set Kiosk Location":
                            clickOptionKioskLoc(node, pin);
                            break;
                        case "Modify Locations":
                            clickOptionModLoc(node, pin);
                            break;
                    }
                }
                for (Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
                    if (!entry.getValue().getFill().equals(Color.YELLOW)) {
                        if (entry.getKey().isDisabled()) {
                            entry.getValue().setFill(Color.GRAY);
                        }
                    }
                }
            }
        });
    }

    private void rightClick(Node node, Circle pin) {
        location_or_path.getSelectionModel().selectFirst();
        clearOptions();
        modify_info_box.setVisible(true);
        pin.setFill(Color.YELLOW);
        building_change.setText(node.getNodeBuilding());
        loc_type_change.getSelectionModel().select((Object) node.getNodeType());
        long_name_change.setText(node.getLongName());
        short_name_change.setText(node.getShortName());
        changing_node = node;
    }

    private void clickOptionAddPath(Node node, Circle pin) {
        if (first_click) {
            first_loc = node;
            first_click = false;
            location_one.setText(node.getNodeID());
            pin.setFill(Color.YELLOW);
        } else {
            second_loc = node;
            location_two.setText(node.getNodeID());
            pin.setFill(Color.YELLOW);
        }
    }

    private void clickOptionDeleteLoc(Node node, Circle pin) {
        if (pin.getFill().equals(Color.YELLOW)) {
            pin.setFill(color);
            entry_to_delete.remove(node, pin);
        } else {
            entry_to_delete.put(node, pin);
            location_to_delete.setText(node.getNodeID());
            pin.setFill(Color.YELLOW);
        }
    }

    private void clickOptionKioskLoc(Node node, Circle pin) {
        Storage.getInstance().updateDefaultKioskLocation(node.getNodeID());
        HomePageController.setKioskDefaultLocation(Storage.getInstance().getDefaultKioskLocation());
        kiosk_location_name.setText(node.getNodeID());
    }

    private void clickOptionModLoc(Node node, Circle pin) {
        if (pin.getFill().equals(Color.YELLOW) && movedNodes.containsKey(node)) {
            pin.setFill(color);
            nodes_to_move.remove(node, pin);
        } else {
            nodes_to_move.put(node, pin);
            pin.setFill(Color.YELLOW);
            pin.setOnMouseDragged(event -> drag(event));
            pin.setOnMouseReleased(event -> released(event));
        }
    }

    public void onMouseClick(MouseEvent click) {
        if (confirm_3d.isVisible()) {
            new_node.setXCoord3D((int) click.getX());
            new_node.setYCoord3D((int) click.getY());
            pane.getChildren().remove(temp_pin);
            temp_pin = new Circle(new_node.getXCoord3D(), new_node.getYCoord3D(), 7, color);
            temp_pin.setStroke(Color.BLACK);
            temp_pin.setStrokeWidth(3);
            pane.getChildren().add(temp_pin);
        } else if (location_or_path.getValue().toString().equals("Add Location")) {
            clickOptionAddLocation(click);
        } else if (location_or_path.getValue().toString().equals("Delete Path")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<Edge, Line> line : edge_list.entrySet()) {
                if (line.getValue().contains(pt)) {
                    if (line.getValue().getStroke().equals(Color.YELLOW)) {
                        edge_list.put(line.getKey(), line.getValue());
                        line.getValue().setStroke(Color.BLACK);
                        edges_to_delete.remove(line);
                    } else {
                        edges_to_delete.put(line.getKey(), line.getValue());
                        line.getValue().setStroke(Color.YELLOW);
                        edge_list.remove(line);
                    }
                }
            }
        } else if (location_or_path.getValue().toString().equals("Batch Disable Locations")) {
            removePaneChild("polygon");
            createPreviewLine(click.getX(), click.getY());
            geoBlock.getPoints().addAll(click.getX(), click.getY());
            pane.getChildren().add(geoBlock);
        }
    }

    private void clickOptionAddLocation(MouseEvent click) {
        Scene scene = add_loc.getScene();

        Matrix predict_3d = predictor.getPrediction((int) click.getX(), (int) click.getY());

        String loc_type_shortname = locations.get(loc_type.getValue().toString());
        new_node = new edu.wpi.cs3733d18.teamS.data.Node(generateNodeId(loc_type_shortname), (int) click.getX(), (int) click.getY(),
                floor_map.get(choose_floor.getValue().toString()), building.getText(), loc_type_shortname, long_name.getText(),
                short_name.getText(), "S", (int) predict_3d.get(0, 0), (int) predict_3d.get(1, 0), false); //TODO: id function
        pane.getChildren().clear();
        pane.getChildren().add(map);
        map.setImage(new Image(select3DMap()));

        temp_pin = new Circle(new_node.getXCoord3D(), new_node.getYCoord3D(), 7, color);
        temp_pin.setStroke(Color.BLACK);
        temp_pin.setStrokeWidth(3);
        pane.getChildren().add(temp_pin);
        confirm_3d.setVisible(true);
        scene.setCursor(Cursor.DEFAULT);
        add_node_box.setVisible(false);
        choose_floor.setVisible(false);
    }

    public void onChangeInfoConfirm() {
        changing_node.setNodeBuilding(building_change.getText());
        String loc_type_shortname = locations.get(loc_type_change.getValue().toString());
        changing_node.setNodeType(loc_type_shortname);
        changing_node.setLongName(long_name_change.getText());
        changing_node.setShortName(short_name_change.getText());
        storage.updateNode(changing_node);
        location_or_path.getSelectionModel().selectFirst();
        clearOptions();
    }

    /**
     * Stops adding a location.
     */
    private void endAddLoc() {
        building.setText("");
        long_name.setText("");
        short_name.setText("");
    }

    /**
     * Deletes an edge.
     *
     * @param a_node the node connected.
     */
    private void deleteEdge(edu.wpi.cs3733d18.teamS.data.Node a_node) {
        List<Edge> to_delete = new ArrayList<>();
        List<edu.wpi.cs3733d18.teamS.data.Edge> edges = storage.getAllEdges();
        for (Map.Entry<Edge, Line> an_entry : edge_list.entrySet()) {
            if (an_entry.getKey().getEndNode().equals(a_node.getNodeID()) || an_entry.getKey().getStartNode().equals(a_node.getNodeID())) {
                to_delete.add(an_entry.getKey());
                edge_list.remove(an_entry);
                pane.getChildren().remove(edge_list.get(an_entry.getKey()));
            }
        }
        for (Edge a_edge : to_delete) {
            storage.deleteEdge(a_edge);
        }
    }

    /**
     * Creates a node Id for the nodes created.
     *
     * @param ntype the type of the node.
     * @return the String of the new node ID.
     */
    private String generateNodeId(String ntype) {
        List<edu.wpi.cs3733d18.teamS.data.Node> nodes = storage.getAllNodes();  //get list of all nodes in edu.wpi.cs3733d18.teamS.database

        int currmax = 0;
        for (edu.wpi.cs3733d18.teamS.data.Node n : nodes) {  //count number of nodes of that type that already exist
            if (n.getNodeType().equals(ntype)) {
                String fullID = n.getNodeID();
                fullID = fullID.substring(5, 8);
                int id = Integer.parseInt(fullID);
                if (id > currmax) {
                    currmax = id;
                }
            }
        }
        String floor_track = floor_map.get(choose_floor.getValue().toString());
        String small_ft = ("00" + floor_track).substring(floor_track.length());
        return ("B" + ntype + String.format("%03d", currmax + 1) + small_ft);
    }

    /**
     * Generates an edge ID.
     *
     * @param first_node  the first node connected to the edge.
     * @param second_node the second node connected to the edge.
     * @return the edge ID.
     */
    private String generateEdgeId(edu.wpi.cs3733d18.teamS.data.Node first_node, edu.wpi.cs3733d18.teamS.data.Node second_node) {
        return (first_node.getNodeID() + "_" + second_node.getNodeID());
    }

    /**
     * Toggles the 3d mode.
     */
    public void on3DToggle() {
        if (toggle3D.isSelected()) {
            choose3DFloor();
        } else {
            onChooseFloorChange();
        }
        makeMap(storage.getAllNodes(), storage.getAllEdges());
        for (Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
            if (!entry.getValue().getFill().equals(Color.YELLOW)) {
                if (entry.getKey().isDisabled()) {
                    entry.getValue().setFill(Color.GRAY);
                }
            }
        }
        removePaneChild();
    }

    /**
     * chooses the image for the 3d map floor.
     */
    private void choose3DFloor() {
        switch (choose_floor.getValue().toString()) {
            case "Ground Floor":
                map.setImage(new Image("images/2dMaps/00_thegroundfloor.png"));
                break;
            case "Lower Level One":
                map.setImage(new Image("images/3dMaps/L1-NO-ICONS.png"));
                break;
            case "Lower Level Two":
                map.setImage(new Image("images/3dMaps/L2-NO-ICONS.png"));
                break;
            case "First Floor":
                map.setImage(new Image("images/3dMaps/1-NO-ICONS.png"));
                break;
            case "Second Floor":
                map.setImage(new Image("images/3dMaps/2-NO-ICONS.png"));
                break;
            case "Third Floor":
                map.setImage(new Image("images/3dMaps/3-NO-ICONS.png"));
                break;
        }
    }

}
