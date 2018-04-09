package controller;

import com.jfoenix.controls.JFXComboBox;
import data.Edge;
import database.Storage;
import internationalization.AllText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.ImageCursor;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ModifyMapController {

    HashMap<data.Node, ImageView> nodes_list;
    Storage storage;

    HashMap<String, String> locations;

    data.Node start_of_edge;

    @FXML
    Label add_loc_fail;

    @FXML
    TextField floor;

    @FXML
    TextField building;

    @FXML
    JFXComboBox loc_type;

    @FXML
    TextField long_name;

    @FXML
    TextField short_name;

    @FXML
    AnchorPane pane;

    @FXML
    Button add_loc_cancel;

    @FXML
    Button delete_loc_cancel;

    @FXML
    Button add_loc;

    @FXML
    Button back_btn;

    @FXML
    BorderPane main_pane;

    @FXML
    Label time;

    @FXML
    JFXComboBox location_or_path;

    @FXML
    private void initialize() {
        first_click = true;

        nodes_list = new HashMap<>();
        storage = Storage.getInstance();
        makeMap(storage.getAllNodes());

        ObservableList<String> lop = FXCollections.observableArrayList();
        lop.addAll("View Map","Add Location", "Add Path", "Delete Location");
        location_or_path.setItems(lop);
        location_or_path.getSelectionModel().selectFirst();

        ObservableList<String> list_type = FXCollections.observableArrayList();
        list_type.addAll("Conference","Hallway", "Department", "Information", "Laboratory", "Restroom", "Stairs", "Service");
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
        for(int i = 0; i < list_type.size(); i++) {
            locations.put(list_type.get(i), short_name.get(i));
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
    }

    public void onBackClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        Parent admin_parent = FXMLLoader.load(getClass().getResource("/AdminPage.fxml"), AllText.getBundle());
        Scene admin_scene = new Scene(admin_parent, window.getWidth(), window.getHeight());
        Stage admin_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        admin_stage.setTitle("User");
        admin_stage.setScene(admin_scene);
        admin_stage.show();
    }

    public void onAddLocClick() {
        if (!floor.getText().equals("") && !building.getText().equals("") && !loc_type.getSelectionModel().isEmpty() && !long_name.getText().equals("") && !short_name.getText().equals("")) {
            Scene scene = add_loc.getScene();
            Image loc_cursor = new Image("images/nodeIcon.png");
            scene.setCursor(new ImageCursor(loc_cursor));
            add_loc_cancel.setVisible(true);
            add_loc_fail.setText("");
        }
        else {
            add_loc_fail.setText("Please fill all fields");
        }
    }

    public void onAddLocCancelClick() {
        Scene scene = add_loc.getScene();
        scene.setCursor(Cursor.DEFAULT);
        add_loc_cancel.setVisible(false);
    }

    @FXML
    VBox add_node_box;

    public void onChooseAction() {
        if (location_or_path.getValue().toString().equals("Add Path")) {
            add_edge_box.setVisible(true);
            add_node_box.setVisible(false);
            delete_loc_box.setVisible(false);
        }
        else if (location_or_path.getValue().toString().equals("Add Location")) {
            add_node_box.setVisible(true);
            add_edge_box.setVisible(false);
            delete_loc_box.setVisible(false);
        }
        else if (location_or_path.getValue().toString().equals("View Map")) {
            add_edge_box.setVisible(false);
            add_node_box.setVisible(false);
            delete_loc_box.setVisible(false);
        }
        else if (location_or_path.getValue().toString().equals("Delete Location")) {
            add_edge_box.setVisible(false);
            add_node_box.setVisible(false);
            delete_loc_box.setVisible(true);
        }
    }

    @FXML
    TextField location_one, location_two, location_to_delete;

    @FXML
    Button add_edge, cancel_edge;

    @FXML
    VBox add_edge_box, delete_loc_box;

    private Boolean first_click;
    private data.Node first_loc;
    private data.Node second_loc;

    public void onMouseClick(MouseEvent click) {
        if (location_or_path.getValue().toString().equals("Add Location")) {
            Scene scene = add_loc.getScene();
            String loc_type_shortname = locations.get(loc_type.getValue().toString());
            data.Node a_node = new data.Node(generateNodeId(loc_type_shortname), (int)click.getX() + 2100 + 10, (int)click.getY() + 550 + 30, //adding the offset of the image and the offset of the icon
                    floor.getText(), building.getText(), loc_type_shortname, long_name.getText(),
                    short_name.getText(), "S", 1, 1); //TODO: id function
            ImageView pin = new ImageView("images/nodeIcon.png");
            pin.setX(click.getX());
            pin.setY(click.getY());
            nodes_list.put(a_node, pin);
            storage.saveNode(a_node);
            pane.getChildren().add(pin);
            scene.setCursor(Cursor.DEFAULT);
            endAddLoc();
            add_node_box.setVisible(false);
            location_or_path.getSelectionModel().selectFirst();
        }

        else if (location_or_path.getValue().toString().equals("Add Path") && first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    first_loc = entry.getKey();
                    first_click = false;
                    location_one.setText(entry.getKey().getNodeID());
                }
            }
        }

        else if (location_or_path.getValue().toString().equals("Add Path") && !first_click) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    second_loc = entry.getKey();
                    location_two.setText(entry.getKey().getNodeID());
                }
            }
        }

        else if (location_or_path.getValue().toString().equals("Delete Location")) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    entry_to_delete = entry;
                    location_to_delete.setText(entry.getKey().getNodeID());
                }
            }
        }
    }

    private Map.Entry<data.Node, ImageView> entry_to_delete;

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
            storage.saveEdge(new data.Edge(generateEdgeId(first_loc, second_loc), first_loc.getNodeID(), second_loc.getNodeID()));
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

    private void makeMap(List<data.Node> dataNodes) {
        int count = 1; //test
        int coord; //test
        for(data.Node a_node: dataNodes) {
            count++; //test
            ImageView pin = new ImageView("images/nodeIcon.png");
            pin.setX(a_node.getXCoord() - 2100 - 10); // -2100 for the image offset, -10 for the icon offset
            pin.setY(a_node.getYCoord() - 550 - 30); // -550 for the image offset, -30 for the icon offset
            nodes_list.put(a_node, pin);
            pane.getChildren().add(pin);
        }
    }

    private void endAddLoc() {
        add_loc_cancel.setVisible(false);
        floor.setText("");
        building.setText("");
        loc_type.getSelectionModel().clearSelection();
        long_name.setText("");
        short_name.setText("");
    }

    private void deleteEdge(data.Node a_node) {
        List<data.Edge> to_delete = new ArrayList<>();
        List<data.Edge> edges = storage.getAllEdges();
        for (data.Edge a_edge : edges) {
            if (a_edge.getEndNode().equals(a_node.getNodeID()) || a_edge.getStartNode().equals(a_node.getNodeID())) {
                to_delete.add(a_edge);
            }
        }
        for (data.Edge a_edge : to_delete) {
            storage.deleteEdge(a_edge);
        }
    }

    private String generateNodeId(String ntype){
        List<data.Node> nodes = storage.getAllNodes();  //get list of all nodes in database

        int currmax = 0;
        for (data.Node n : nodes){  //count number of nodes of that type that already exist
            if (n.getNodeType().equals(ntype)){
                String fullID = n.getNodeID();
                fullID = fullID.substring(5,8);
                int id = Integer.parseInt(fullID);
                if(id > currmax) {
                    currmax = id;
                }
            }
        }
        return ("B" + ntype + String.format("%03d", currmax+1) + "02");
    }

    private String generateEdgeId(data.Node first_node, data.Node second_node) {
        return (first_node.getNodeID() + "_" + second_node.getNodeID());
    }

}
