package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.matrix.Matrix;
import edu.wpi.cs3733d18.teamS.database.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
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
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModifyMapController {

    double zoom_factor;

    HashMap<edu.wpi.cs3733d18.teamS.data.Node, ImageView> nodes_list;
    Storage storage;

    HashMap<String, String> locations;
    HashMap<String, String> floor_map;

    edu.wpi.cs3733d18.teamS.data.Node start_of_edge;

    @FXML
    Label add_loc_fail;

    @FXML
    TextField building;

    @FXML
    JFXComboBox loc_type;

    @FXML
    TextField long_name;

    @FXML
    TextField short_name;

    @FXML
    TextField kiosk_location_name;

    @FXML
    AnchorPane pane;

    @FXML
    Button add_loc_cancel;

    @FXML
    Button add_loc;

    @FXML
    Button back_btn;

    @FXML
    BorderPane main_pane;

    @FXML
    Label time;

    @FXML
    JFXComboBox location_or_path, choose_floor;

    @FXML
    ScrollPane scroll_pane;
    @FXML
    ImageView map;
    @FXML
    VBox add_node_box;
    @FXML
    TextField location_one, location_two, location_to_delete, kiosk_location;
    @FXML
    VBox add_edge_box, delete_loc_box;
    edu.wpi.cs3733d18.teamS.data.Node new_node;
    @FXML
    Button confirm_3d;

    @FXML
    Text user_name;

    ImageView pin;
    private Boolean first_click;
    private edu.wpi.cs3733d18.teamS.data.Node first_loc;
    private edu.wpi.cs3733d18.teamS.data.Node second_loc;
    private Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, ImageView> entry_to_delete;

    @FXML
    private void initialize() {
        first_click = true;

        zoom_factor = 1;

        scroll_pane.setVvalue(0.5);
        scroll_pane.setHvalue(0.25);

        ObservableList<String> lop = FXCollections.observableArrayList();
        lop.addAll("View Map", "Add Location", "Add Path", "Delete Location");
        location_or_path.setItems(lop);
        location_or_path.getSelectionModel().selectFirst();

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
        storage = Storage.getInstance();
        makeMap(storage.getAllNodes());

        lop = FXCollections.observableArrayList();
        lop.addAll("View Map", "Add Location", "Add Path", "Delete Location", "Set Kiosk Location");
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
        makeMap(storage.getAllNodes());
    }

    public void onBackClick(ActionEvent event) throws IOException {
        Main.switchScenes("User", "/AdminPage.fxml");
    }

    public void onAddLocClick() {
        if (!building.getText().equals("") && !loc_type.getSelectionModel().isEmpty() && !long_name.getText().equals("") && !short_name.getText().equals("")) {
            Scene scene = add_loc.getScene();
            Image loc_cursor = new Image("images/mapIcons/nodeIcon.png");
            scene.setCursor(new ImageCursor(loc_cursor));
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
        switch (location_or_path.getValue().toString()) {
            case "Add Path":
                add_edge_box.setVisible(true);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                break;
            case "Add Location":
                add_node_box.setVisible(true);
                add_edge_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                break;
            case "View Map":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(false);
                break;
            case "Delete Location":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(true);
                kiosk_location_name.setVisible(false);
                break;
            case "Set Kiosk Location":
                add_edge_box.setVisible(false);
                add_node_box.setVisible(false);
                delete_loc_box.setVisible(false);
                kiosk_location_name.setVisible(true);
                break;
        }
    }

    public void onConfirm3dClick() {
        pane.getChildren().clear();
        pane.getChildren().add(map);
        map.setImage(new Image(choose2DMap()));
        makeMap(storage.getAllNodes());

        ImageView pin = new ImageView("images/mapIcons/nodeIcon.png");
        pin.setX(new_node.getXCoord());
        pin.setY(new_node.getYCoord());
        nodes_list.put(new_node, pin);
        storage.saveNode(new_node);
        pane.getChildren().add(pin);

        endAddLoc();
        add_node_box.setVisible(false);
        location_or_path.getSelectionModel().selectFirst();
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
            pane.getChildren().remove(pin);
            pin.setX(new_node.getXCoord3D());
            pin.setY(new_node.getYCoord3D());
            pane.getChildren().add(pin);
        } else if (location_or_path.getValue().toString().equals("Add Location")) {
            Scene scene = add_loc.getScene();

            List<edu.wpi.cs3733d18.teamS.data.Node> nn_nodes_list = storage.getAllNodes();

            PersistentMachineLearningAlgorithm nn = new NN.Builder()
                    .addLayer(2, 8, new ReLU())
                    .addLayer(8, 2, new Linear())
                    .build();

            Matrix[] input_data = new Matrix[nn_nodes_list.size()];

            Matrix[] output_data = new Matrix[nn_nodes_list.size()];

            for (int i = 0; i < nn_nodes_list.size(); i++) {
                input_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord() / 5000.0, (double) nn_nodes_list.get(i).getYCoord() / 3400.0); //2D
                output_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord3D(), (double) nn_nodes_list.get(i).getYCoord3D()); //3D
            }

            nn.fit(input_data, output_data, 0.0001, 500);


            Matrix predict_3d = nn.predict((click.getX() + 10) / 5000.0, (click.getY() + 30) / 3400.0);

            String loc_type_shortname = locations.get(loc_type.getValue().toString());
            new_node = new edu.wpi.cs3733d18.teamS.data.Node(generateNodeId(loc_type_shortname), (int) click.getX() + 10, (int) click.getY() + 30, //adding the offset of the icon
                    floor_map.get(choose_floor.getValue().toString()), building.getText(), loc_type_shortname, long_name.getText(),
                    short_name.getText(), "S", (int) predict_3d.get(0, 0), (int) predict_3d.get(1, 0)); //TODO: id function
            pane.getChildren().clear();
            pane.getChildren().add(map);
            map.setImage(new Image(select3DMap()));
            pin = new ImageView("images/mapIcons/nodeIcon.png");
            pin.setX(new_node.getXCoord3D());
            pin.setY(new_node.getYCoord3D());
            pane.getChildren().add(pin);
            confirm_3d.setVisible(true);
            scene.setCursor(Cursor.DEFAULT);
            add_node_box.setVisible(false);
            choose_floor.setVisible(false);
        } else if (location_or_path.getValue().toString().equals("Add Path") && first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    first_loc = entry.getKey();
                    first_click = false;
                    location_one.setText(entry.getKey().getNodeID());
                }
            }
        } else if (location_or_path.getValue().toString().equals("Add Path") && !first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    second_loc = entry.getKey();
                    location_two.setText(entry.getKey().getNodeID());
                }
            }
        } else if (location_or_path.getValue().toString().equals("Delete Location")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    entry_to_delete = entry;
                    location_to_delete.setText(entry.getKey().getNodeID());
                }
            }
        } else if (location_or_path.getValue().toString().equals("Set Kiosk Location")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<edu.wpi.cs3733d18.teamS.data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    HomePageController.setKioskDefaultLocation(entry.getKey().getNodeID());
                    kiosk_location_name.setText(entry.getKey().getShortName());
                }
            }
        }
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

    public void onDeleteLocClick() {
        if (!location_to_delete.getText().trim().isEmpty()) {
            pane.getChildren().remove(entry_to_delete.getValue());
            storage.deleteNode(entry_to_delete.getKey());
            nodes_list.remove(entry_to_delete);
            deleteEdge(entry_to_delete.getKey());
            onDeleteLocCancelClick();
        }
    }

    public void onDeleteLocCancelClick() {
        location_to_delete.setText("");
        delete_loc_box.setVisible(false);
        location_or_path.getSelectionModel().selectFirst();
    }

    public void onAddEdgeClick() {
        if (!location_one.getText().trim().isEmpty() && !location_two.getText().trim().isEmpty()) {
            storage.saveEdge(new edu.wpi.cs3733d18.teamS.data.Edge(generateEdgeId(first_loc, second_loc), first_loc.getNodeID(), second_loc.getNodeID()));
            onCancelEdgeClick();
        }
    }

    public void onCancelEdgeClick() {
        first_click = true;
        add_edge_box.setVisible(false);
        location_or_path.getSelectionModel().selectFirst();
        location_one.setText("");
        location_two.setText("");
    }

    private void makeMap(List<edu.wpi.cs3733d18.teamS.data.Node> dataNodes) {
        String floor = choose_floor.getValue().toString();
        for (edu.wpi.cs3733d18.teamS.data.Node a_node : dataNodes) {
            if (a_node.getNodeFloor().equals(floor_map.get(floor))) {
                ImageView pin = new ImageView("images/mapIcons/nodeIcon.png");
                pin.setX(a_node.getXCoord() - 10); //  -10 for the icon offset
                pin.setY(a_node.getYCoord() - 30); //  -30 for the icon offset
                nodes_list.put(a_node, pin);
                pane.getChildren().add(pin);
            }
        }
    }

    private void endAddLoc() {
        add_loc_cancel.setVisible(false);
        building.setText("");
        loc_type.getSelectionModel().clearSelection();
        long_name.setText("");
        short_name.setText("");
    }

    private void deleteEdge(edu.wpi.cs3733d18.teamS.data.Node a_node) {
        List<edu.wpi.cs3733d18.teamS.data.Edge> to_delete = new ArrayList<>();
        List<edu.wpi.cs3733d18.teamS.data.Edge> edges = storage.getAllEdges();
        for (edu.wpi.cs3733d18.teamS.data.Edge a_edge : edges) {
            if (a_edge.getEndNode().equals(a_node.getNodeID()) || a_edge.getStartNode().equals(a_node.getNodeID())) {
                to_delete.add(a_edge);
            }
        }
        for (edu.wpi.cs3733d18.teamS.data.Edge a_edge : to_delete) {
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

}
