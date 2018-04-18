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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModifyMapController {

    double zoom_factor;

    HashMap<edu.wpi.cs3733d18.teamS.data.Node, Circle> nodes_list;
    HashMap<edu.wpi.cs3733d18.teamS.data.Edge, Line> edge_list;

    Storage storage;

    HashMap<String, String> locations;
    HashMap<String, String> floor_map;

    edu.wpi.cs3733d18.teamS.data.Node start_of_edge;

    edu.wpi.cs3733d18.teamS.data.Node new_node;

    HashMap<Edge, Line> edges_to_delete;
    HashMap<Node, Circle> nodes_to_move;
    HashMap<Node, Circle> movedNodes;

    @FXML
    JFXToggleButton toggle3D;

    @FXML
    Label add_loc_fail, time;

    @FXML
    TextField building, long_name, short_name, kiosk_location_name, location_one, location_two, location_to_delete, kiosk_location;

    @FXML
    JFXComboBox loc_type, location_or_path, choose_floor;

    @FXML
    AnchorPane pane;

    @FXML
    Button add_loc_cancel, add_loc, back_btn;

    @FXML
    BorderPane main_pane;

    @FXML
    ScrollPane scroll_pane;
    @FXML
    ImageView map;

    @FXML
    VBox add_node_box, add_edge_box, delete_loc_box, delete_edge_box, modify_loc_box, batch_disable_box;

    @FXML
    Button confirm_3d;

    @FXML
    Text user_name;

    Color color = Color.web("#4863A0");

    Circle temp_pin;
    private Boolean first_click;
    private edu.wpi.cs3733d18.teamS.data.Node first_loc;
    private edu.wpi.cs3733d18.teamS.data.Node second_loc;
    private HashMap<Node, Circle> entry_to_delete;
    private Node3DPredictor predictor = new Node3DPredictor();
    private Polygon geoBlock = new Polygon();

    @FXML
    private void initialize() {
        first_click = true;

        zoom_factor = 1;

        scroll_pane.setVvalue(0.5);
        scroll_pane.setHvalue(0.25);

        edges_to_delete = new HashMap<>();
        entry_to_delete = new HashMap<>();
        nodes_to_move = new HashMap<>();
        movedNodes = new HashMap<>();

        ObservableList<String> list_type = FXCollections.observableArrayList();
        list_type.addAll("Conference", "Hallway", "Department", "Information", "Laboratory", "Restroom", "Stairs", "Service");
        loc_type.setItems(list_type);

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

    public void onZoomOut() {
        if (zoom_factor > 0.5) {
            zoom_factor -= 0.2;
            scroll_pane.getContent().setScaleX(zoom_factor);
            scroll_pane.setLayoutX(zoom_factor);
            scroll_pane.getContent().setScaleY(zoom_factor);
            scroll_pane.setLayoutY(zoom_factor);

        }
    }

    public void onZoomIn() {
        if (zoom_factor < 1.8) {
            zoom_factor += 0.2;
            scroll_pane.getContent().setScaleX(zoom_factor);
            scroll_pane.setLayoutX(zoom_factor);
            scroll_pane.getContent().setScaleY(zoom_factor);
            scroll_pane.setLayoutY(zoom_factor);
        }
    }

    public void onChooseFloorChange() {
        pane.getChildren().clear();
        nodes_list.clear();
        pane.getChildren().add(map);
        switch (choose_floor.getValue().toString()) {
            case "Ground Floor":
                map.setImage(new Image("images/2dMaps/00_thegroundfloor.png"));
                toggle3D.setSelected(false);
                break;
            case "Lower Level One":
                map.setImage(new Image("images/2dMaps/00_thelowerlevel1.png"));
                toggle3D.setSelected(false);
                break;
            case "Lower Level Two":
                map.setImage(new Image("images/2dMaps/00_thelowerlevel2.png"));
                toggle3D.setSelected(false);
                break;
            case "First Floor":
                map.setImage(new Image("images/2dMaps/01_thefirstfloor.png"));
                toggle3D.setSelected(false);
                break;
            case "Second Floor":
                map.setImage(new Image("images/2dMaps/02_thesecondfloor.png"));
                toggle3D.setSelected(false);
                break;
            case "Third Floor":
                map.setImage(new Image("images/2dMaps/03_thethirdfloor.png"));
                toggle3D.setSelected(false);
                break;
        }
        makeMap(storage.getAllNodes(), storage.getAllEdges());
    }

    public void onBackClick(ActionEvent event) throws IOException {
        Main.switchScenes("User", "/AdminPage.fxml");
    }

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

    public void onAddLocCancelClick() {
        Scene scene = add_loc.getScene();
        scene.setCursor(Cursor.DEFAULT);
        add_loc_cancel.setVisible(false);
    }

    public void onChooseAction() {
        scroll_pane.setPannable(true);
        switch (location_or_path.getValue().toString()) {
            case "Add Path":
                add_edge_box.setVisible(true);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "Add Location":
                add_node_box.setVisible(true);
                add_edge_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "View Map":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "Delete Location":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(true);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "Delete Path":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(true);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "Modify Locations":
                modify_loc_box.setVisible(true);
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                scroll_pane.setPannable(false);
                break;
            case "Set Kiosk Location":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(true);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(false);
                pane.setOnMouseMoved(null);
                removePaneChild();
                break;
            case "Batch Disable Locations":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                delete_edge_box.setVisible(false);
                modify_loc_box.setVisible(false);
                batch_disable_box.setVisible(true);
                geoBlock = new Polygon();
                geoBlock.setId("polygon");
                geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
                geoBlock.setStroke(Color.GREEN);
                break;
        }
    }

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
        pane.getChildren().add(pin);

        endAddLoc();

        confirm_3d.setVisible(false);
        choose_floor.setVisible(true);
    }

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
        } else if (location_or_path.getValue().toString().equals("Add Path") && first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, Circle> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    first_loc = entry.getKey();
                    first_click = false;
                    location_one.setText(entry.getKey().getNodeID());
                    entry.getValue().setFill(Color.YELLOW);
                }
            }
        } else if (location_or_path.getValue().toString().equals("Add Path") && !first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, Circle> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    second_loc = entry.getKey();
                    location_two.setText(entry.getKey().getNodeID());
                    entry.getValue().setFill(Color.YELLOW);
                }
            }
        } else if (location_or_path.getValue().toString().equals("Delete Location")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, Circle> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    if(entry.getValue().getFill().equals(Color.YELLOW)) {
                        entry.getValue().setFill(color);
                        edges_to_delete.remove(entry);
                    }
                    entry_to_delete.put(entry.getKey(), entry.getValue());
                    location_to_delete.setText(entry.getKey().getNodeID());
                    entry.getValue().setFill(Color.YELLOW);
                }
            }
        } else if (location_or_path.getValue().toString().equals("Set Kiosk Location")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, Circle> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    Storage.getInstance().updateDefaultKioskLocation(entry.getKey().getNodeID());
                    HomePageController.setKioskDefaultLocation(Storage.getInstance().getDefaultKioskLocation());
                    kiosk_location_name.setText(entry.getKey().getNodeID());
                }
            }
        } else if (location_or_path.getValue().toString().equals("Delete Path")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for(Map.Entry<Edge, Line> line : edge_list.entrySet()) {
                if(line.getValue().contains(pt)) {
                    if(line.getValue().getStroke().equals(Color.YELLOW)) {
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
        } else if (location_or_path.getValue().toString().equals("Modify Locations")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for(Map.Entry<Node, Circle> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    //circle_selected = entry.getValue();
                    if(entry.getValue().getFill().equals(Color.YELLOW) && !movedNodes.containsKey(entry.getKey())) {
                        entry.getValue().setFill(color);
                        nodes_to_move.remove(entry.getKey(), entry.getValue());
                    } else {
                        nodes_to_move.put(entry.getKey(), entry.getValue());
                        entry.getValue().setFill(Color.YELLOW);
                        entry.getValue().setOnMouseDragged(event -> drag(event));
                        entry.getValue().setOnMouseReleased(event -> released(event));
                    }
                }
            }
        }else if(location_or_path.getValue().toString().equals("Batch Disable Locations")){
            removePaneChild("polygon");
            createPreviewLine(click.getX(),click.getY());
            geoBlock.getPoints().addAll(click.getX(), click.getY());
            pane.getChildren().add(geoBlock);
        }
        for(Map.Entry<Node, Circle> entry : nodes_list.entrySet()){
            if(! entry.getValue().getFill().equals(Color.YELLOW)) {
                if(entry.getKey().isDisabled()){
                    entry.getValue().setFill(Color.GREEN);
                }
            }
        }
    }

    public void disableNodesInPolygon(){
        ArrayList<Node> intersecting_nodes = getIntersectingNodes();
        for(Node n : intersecting_nodes) {
            n.setDisabled(true);
            storage.updateNode(n);
        }
        checkEdges();
        makeMap(new ArrayList<>(nodes_list.keySet()), new ArrayList<>(edge_list.keySet()));
        removePaneChild();
        geoBlock = new Polygon();
        geoBlock.setId("polygon");
        geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
        geoBlock.setStroke(Color.GREEN);
        pane.setOnMouseMoved(null);
    }
    public void enableNodesInPolygon(){
        ArrayList<Node> intersecting_nodes = getIntersectingNodes();
        for(Node n : intersecting_nodes){
            n.setDisabled(false);
            storage.updateNode(n);
        }
        checkEdges();
        makeMap(new ArrayList<>(nodes_list.keySet()), new ArrayList<>(edge_list.keySet()));
        removePaneChild();
        geoBlock = new Polygon();
        geoBlock.setId("polygon");
        geoBlock.setStyle("-fx-fill: rgba(49,248,0,0.3);");
        geoBlock.setStroke(Color.GREEN);
        pane.setOnMouseMoved(null);
    }

    public void checkEdges(){
        for(Edge e : edge_list.keySet()){
            if (storage.getNodeByID(e.getStartNode()).isDisabled()
                    || storage.getNodeByID(e.getEndNode()).isDisabled()){
                e.setDisabled(true);
                storage.updateEdge(e);
            }else{
                e.setDisabled(false);
                storage.updateEdge(e);
            }
        }
    }

    private ArrayList<Node> getIntersectingNodes(){
        ArrayList<Node> intersecting_nodes = new ArrayList<>();
        if(geoBlock.getPoints().size() == 0){
            return intersecting_nodes;
        }
        for(Node node : nodes_list.keySet()){
            if(!Shape.intersect(nodes_list.get(node), geoBlock).getBoundsInParent().isEmpty()){
                intersecting_nodes.add(node);
            }
        }
        return intersecting_nodes;
    }

    private void removePaneChild(){
        if(pane.getChildren().size() > 0){
            removePaneChild("previewLine");
            removePaneChild("polygon");
        }
    }

    private void removePaneChild(String id){
        int index = -1;
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i).getId() == id){
                index = i;
            }
        }
        if(index != -1){
            pane.getChildren().remove(index);
        }
    }

    private void createPreviewLine(final double start_x, final double start_y){
        pane.setOnMouseMoved(event -> {
            removePaneChild("previewLine");
            Line l = new Line(start_x, start_y, event.getX(), event.getY());
            l.setId("previewLine");
            pane.getChildren().add(l);
        });

        pane.setOnMouseExited(event -> removePaneChild("previewLine"));
    }

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

    Circle to_move = new Circle();

    private void released(MouseEvent event) {
        movedNodes.putAll(nodes_to_move);
    }

    public void drag(MouseEvent event) {

        if(location_or_path.getValue().toString().equals("Modify Locations")) {
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

    private void moveAll(Circle to_move, double x, double y) {
        for(Map.Entry<Node, Circle> entry : nodes_to_move.entrySet()) {
            if(!entry.getValue().equals(to_move)) {
                entry.getValue().setCenterX(entry.getValue().getCenterX() + x);
                entry.getValue().setCenterY(entry.getValue().getCenterY() + y);
            }
        }
    }

    public void onConfirmMove() {
        for(Map.Entry<Node, Circle> entry : movedNodes.entrySet()) {
            Node node = storage.getNodeByID(entry.getKey().getNodeID());
            if(toggle3D.isSelected()) {
                node.setXCoord3D((int)entry.getValue().getCenterX());
                node.setYCoord3D((int)entry.getValue().getCenterY());
            } else {
                node.setXCoord((int) entry.getValue().getCenterX());
                node.setYCoord((int) entry.getValue().getCenterY());
            }
            storage.updateNode(node);
            entry.getValue().setFill(color);
        }
        if (toggle3D.isSelected()) {
            make3DMap(storage.getAllNodes(), storage.getAllEdges());
        } else {
            makeMap(storage.getAllNodes(), storage.getAllEdges());
        }

    }

    public void onCancelMove() {
        for(Map.Entry<Node, Circle> entry : nodes_to_move.entrySet()) {
            entry.getValue().setFill(color);
        }
        nodes_to_move.clear();
        if(toggle3D.isSelected()) {
            make3DMap(storage.getAllNodes(), storage.getAllEdges());
        } else {
            makeMap(storage.getAllNodes(), storage.getAllEdges());
        }

    }

    public void onDeleteEdge() {
        for(Map.Entry<Edge, Line> line : edges_to_delete.entrySet()) {
            pane.getChildren().remove(line.getValue());
            storage.deleteEdge(line.getKey());
        }
    }

    public void onCancelDeleteEdge() {
        for(Map.Entry<Edge, Line> line : edges_to_delete.entrySet()) {
            edge_list.put(line.getKey(), line.getValue());
            line.getValue().setStroke(Color.BLACK);
        }
        location_or_path.getSelectionModel().selectFirst();
        delete_edge_box.setVisible(false);
    }

    public void onDeleteLocClick() {
        if (!location_to_delete.getText().trim().isEmpty()) {
            for(Map.Entry<Node, Circle> entry : entry_to_delete.entrySet()) {
                pane.getChildren().remove(entry.getValue());
                storage.deleteNode(entry.getKey());
                nodes_list.remove(entry);
                deleteEdge(entry.getKey());
            }
        }
        location_to_delete.setText("");
    }

    public void onDeleteLocCancelClick() {
        for(Map.Entry<Node, Circle> entry : entry_to_delete.entrySet()) {
            nodes_list.put(entry.getKey(), entry.getValue());
            entry.getValue().setFill(color);
        }
        location_to_delete.setText("");
    }

    public void onAddEdgeClick() {
        if (!location_one.getText().trim().isEmpty() && !location_two.getText().trim().isEmpty()) {
            Edge new_edge = new edu.wpi.cs3733d18.teamS.data.Edge(generateEdgeId(first_loc, second_loc), first_loc.getNodeID(), second_loc.getNodeID(), false);
            storage.saveEdge(new_edge);

            double startX = storage.getNodeByID(new_edge.getStartNode()).getXCoord();
            double startY = storage.getNodeByID(new_edge.getStartNode()).getYCoord();
            double endX = storage.getNodeByID(new_edge.getEndNode()).getXCoord();
            double endY = storage.getNodeByID(new_edge.getEndNode()).getYCoord();

            if(toggle3D.isSelected()) {
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

    public void onCancelEdgeClick() {
        first_click = true;
        location_one.setText("");
        location_two.setText("");
        nodes_list.get(first_loc).setFill(color);
        nodes_list.get(second_loc).setFill(color);
    }

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
                    double startX = storage.getNodeByID(a_edge.getStartNode()).getXCoord();
                    double startY = storage.getNodeByID(a_edge.getStartNode()).getYCoord();
                    double endX = storage.getNodeByID(a_edge.getEndNode()).getXCoord();
                    double endY= storage.getNodeByID(a_edge.getEndNode()).getYCoord();
                    Line path = new Line(startX, startY, endX, endY);
                    path.setStrokeWidth(3);
                    if(a_edge.isDisabled()){
                        path.setStroke(Color.GREEN);
                    }
                    pane.getChildren().add(path);
                    edge_list.put(a_edge, path);
                } catch (NullPointerException e) {

                }
            }
        }
        for (edu.wpi.cs3733d18.teamS.data.Node a_node : dataNodes) {
            if(!a_node.getShortName().equals("TRAIN")) {
                if (a_node.getNodeFloor().equals(floor_map.get(floor))) {

                    Circle pin = new Circle(a_node.getXCoord(), a_node.getYCoord(), 8, color);
                    pin.setStroke(Color.BLACK);
                    pin.setStrokeWidth(3);
                    if (a_node.isDisabled()) {
                        pin.setFill(Color.GREEN);
                    }
                    nodes_list.put(a_node, pin);
                    pane.getChildren().add(pin);
                }
            }
        }
    }

    private void endAddLoc() {
        building.setText("");
        long_name.setText("");
        short_name.setText("");
    }

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

    private String generateEdgeId(edu.wpi.cs3733d18.teamS.data.Node first_node, edu.wpi.cs3733d18.teamS.data.Node second_node) {
        return (first_node.getNodeID() + "_" + second_node.getNodeID());
    }

    public void on3DToggle() {
        if(toggle3D.isSelected()) {
            choose3DFloor();
            make3DMap(storage.getAllNodes(), storage.getAllEdges());
        }
        else {
            onChooseFloorChange();
            makeMap(storage.getAllNodes(), storage.getAllEdges());
        }
    }

    private void make3DMap(List<Node> dataNodes, List<Edge> dataEdges) {
        pane.getChildren().removeAll(nodes_list.values());
        pane.getChildren().removeAll(edge_list.values());
        nodes_list.clear();
        edge_list.clear();
        nodes_to_move.clear();
        movedNodes.clear();
        String floor = choose_floor.getValue().toString();
        for (edu.wpi.cs3733d18.teamS.data.Node a_node : dataNodes) {
            if (a_node.getNodeFloor().equals(floor_map.get(floor))) {

                Circle pin = new Circle(a_node.getXCoord3D(), a_node.getYCoord3D(), 8, color);
                pin.setStroke(Color.BLACK);
                pin.setStrokeWidth(3);

                nodes_list.put(a_node, pin);
                pane.getChildren().add(pin);
            }
        }
        String short_floor = floor_map.get(choose_floor.getValue().toString());
        String small_ft = ("00" + short_floor).substring(short_floor.length());
        for (edu.wpi.cs3733d18.teamS.data.Edge a_edge : dataEdges) {
            String start = a_edge.getStartNode().substring(8);
            String end = a_edge.getEndNode().substring(8);
            if (start.equals(small_ft) && end.equals(small_ft)) {
                try {
                    double startX = storage.getNodeByID(a_edge.getStartNode()).getXCoord3D();
                    double startY = storage.getNodeByID(a_edge.getStartNode()).getYCoord3D();
                    double endX = storage.getNodeByID(a_edge.getEndNode()).getXCoord3D();
                    double endY= storage.getNodeByID(a_edge.getEndNode()).getYCoord3D();
                    Line path = new Line(startX, startY, endX, endY);
                    path.setStrokeWidth(3);
                    pane.getChildren().add(path);
                    edge_list.put(a_edge, path);
                } catch (NullPointerException e) {

                }
            }
        }
    }

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
