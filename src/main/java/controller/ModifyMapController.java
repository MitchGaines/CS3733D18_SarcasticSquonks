package controller;

import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.lann.activation.Softmax;
import com.kylecorry.matrix.Matrix;
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
import javafx.scene.control.*;
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
    HashMap<String, String> floor_map;

    data.Node start_of_edge;

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
    private void initialize() {
        first_click = true;

        scroll_pane.setVvalue(0.25);
        scroll_pane.setHvalue(0.25);

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

        ObservableList<String> floors = FXCollections.observableArrayList();
        floors.addAll("Ground Floor", "Lower Level One", "Lower Level Two", "First Floor", "Second Floor", "Third Floor");
        choose_floor.setItems(floors);

        List<String> short_floor = new ArrayList<>();
        short_floor.add("G");
        short_floor.add("L1");
        short_floor.add("L2");
        short_floor.add("1");
        short_floor.add("2");
        short_floor.add("3");

        floor_map = new HashMap<>();
        for(int i = 0; i < floors.size(); i++) {
            floor_map.put(floors.get(i), short_floor.get(i));
        }

        choose_floor.getSelectionModel().select(3);

        pane.getChildren().clear();
        pane.getChildren().add(map);
        nodes_list = new HashMap<>();
        storage = Storage.getInstance();
        makeMap(storage.getAllNodes());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
    }

    @FXML
    ImageView map;

    public void onChooseFloorChange() {
        pane.getChildren().clear();
        nodes_list.clear();
        pane.getChildren().add(map);
        if (choose_floor.getValue().toString().equals("Ground Floor")) {
            map.setImage(new Image("images/00_thegroundfloor.png"));
        }
        else if (choose_floor.getValue().toString().equals("Lower Level One")) {
            map.setImage(new Image("images/00_thelowerlevel1.png"));
        }
        else if (choose_floor.getValue().toString().equals("Lower Level Two")) {
            map.setImage(new Image("images/00_thelowerlevel2.png"));
        }
        else if (choose_floor.getValue().toString().equals("First Floor")) {
            map.setImage(new Image("images/01_thefirstfloor.png"));
        }
        else if (choose_floor.getValue().toString().equals("Second Floor")) {
            map.setImage(new Image("images/02_thesecondfloor.png"));
        }
        else if (choose_floor.getValue().toString().equals("Third Floor")) {
            map.setImage(new Image("images/03_thethirdfloor.png"));
        }
        makeMap(storage.getAllNodes());
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
        if (!building.getText().equals("") && !loc_type.getSelectionModel().isEmpty() && !long_name.getText().equals("") && !short_name.getText().equals("")) {
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

            List<data.Node> nn_nodes_list  = storage.getAllNodes();

            PersistentMachineLearningAlgorithm nn = new NN.Builder()
                    .addLayer(2,8, new Softmax())
                    .addLayer(8, 2, new Linear())
                    .build();

            Matrix[] input_data = new Matrix[nn_nodes_list.size()];

            Matrix[] output_data = new Matrix[nn_nodes_list.size()];

            for(int i = 0; i < nn_nodes_list.size(); i++) {
                input_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord() / 5000.0, (double) nn_nodes_list.get(i).getYCoord() / 3400.0); //2D
                output_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord3D(), (double) nn_nodes_list.get(i).getYCoord3D()); //3D
            }

            nn.fit(input_data, output_data, 0.0001, 500);


            Matrix predict_3d = nn.predict((click.getX() + 10)/5000.0, (click.getY() + 30)/3400.0);
          
            String loc_type_shortname = locations.get(loc_type.getValue().toString());
            data.Node a_node = new data.Node(generateNodeId(loc_type_shortname), (int)click.getX() + 10, (int)click.getY() + 30, //adding the offset of the icon
                    floor_map.get(choose_floor.getValue().toString()), building.getText(), loc_type_shortname, long_name.getText(),
                    short_name.getText(), "S", (int)predict_3d.get(0,0), (int)predict_3d.get(1, 0));
          
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
        String floor = choose_floor.getValue().toString();
        for(data.Node a_node: dataNodes) {
            if (a_node.getNodeFloor().equals(floor_map.get(floor))) {
                ImageView pin = new ImageView("images/nodeIcon.png");
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
        String floor_track = floor_map.get(choose_floor.getValue().toString());
        String small_ft = ("00" + floor_track).substring(floor_track.length());
        return ("B" + ntype + String.format("%03d", currmax+1) + small_ft);
    }

    private String generateEdgeId(data.Node first_node, data.Node second_node) {
        return (first_node.getNodeID() + "_" + second_node.getNodeID());
    }

}
