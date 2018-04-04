package controller;

import data.Edge;
import database.Storage;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyMapController {

    HashMap<data.Node, ImageView> nodes_list;
    Storage storage;

    boolean add_edge = false;
    data.Node start_of_edge;

    @FXML
    Label add_loc_fail;

    @FXML
    ImageView trash_can;

    @FXML
    TextField floor;

    @FXML
    TextField building;

    @FXML
    TextField loc_type;

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
    CheckBox add_edge_check;

    @FXML
    private void initialize() {
        nodes_list = new HashMap<>();
        storage = Storage.getInstance();
        makeMap(storage.getAllNodes());
    }

    public void onBackClick(ActionEvent event) throws IOException {
        Parent admin_parent = FXMLLoader.load(getClass().getResource("/AdminPage.fxml"));
        Scene admin_scene = new Scene(admin_parent);
        Stage admin_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        admin_stage.setTitle("User");
        admin_stage.setScene(admin_scene);
        admin_stage.show();
    }

    public void onAddLocClick() {
        if (!floor.getText().equals("") && !building.getText().equals("") && !loc_type.getText().equals("") && !long_name.getText().equals("") && !short_name.getText().equals("")) {
            trash_can.setVisible(false);
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
        trash_can.setVisible(true);
    }

    public void onMouseClick(MouseEvent click) {
        if (add_edge) {
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    data.Edge a_edge = new Edge(generateEdgeId(start_of_edge, entry.getKey()), start_of_edge.getNodeID(), entry.getKey().getNodeID()); //TODO edge id function
                    storage.saveEdge(a_edge);
                }
            }
            add_edge_check.setSelected(false);
            endAddLoc();
        }

        else if (add_loc_cancel.isVisible()) {
            Scene scene = add_loc.getScene();
            data.Node a_node = new data.Node("id", (int)click.getX() + 2100 + 10, (int)click.getY() + 550 + 30, //adding the offset of the image and the offset of the icon
                    floor.getText(), building.getText(), loc_type.getText(), long_name.getText(),
                    short_name.getText(), "S", 1, 1); //TODO: id function
            ImageView pin = new ImageView("images/nodeIcon.png");
            pin.setX(click.getX());
            pin.setY(click.getY());
            nodes_list.put(a_node, pin);
            storage.saveNode(a_node);
            pane.getChildren().add(pin);
            scene.setCursor(Cursor.DEFAULT);
            if (add_edge_check.isSelected()) {
                add_edge = true;
                start_of_edge = a_node;
            }
            else {
                endAddLoc();
            }
        }

        else if (delete_loc_cancel.isVisible()) {
            System.out.println(storage.getAllEdges().size());
            HashMap<data.Node, ImageView> to_delete = new HashMap<>();
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    pane.getChildren().remove(entry.getValue());
                    storage.deleteNode(entry.getKey());
                    nodes_list.remove(entry);
                    deleteEdge(entry.getKey());
                }
            }
            System.out.println(storage.getAllEdges().size());
        }
    }

    public void onTrashCanClick() {
        delete_loc_cancel.setVisible(true);
        add_loc.setVisible(false);
        floor.setVisible(false);
        building.setVisible(false);
        loc_type.setVisible(false);
        long_name.setVisible(false);
        short_name.setVisible(false);
        add_loc.setVisible(false);
    }

    public void onDeleteLocCancelClick() {
        delete_loc_cancel.setVisible(false);
        add_loc.setVisible(true);
        add_loc.setVisible(true);
        floor.setVisible(true);
        building.setVisible(true);
        loc_type.setVisible(true);
        long_name.setVisible(true);
        short_name.setVisible(true);
        add_loc.setVisible(true);
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
        trash_can.setVisible(true);
        floor.setText("");
        building.setText("");
        loc_type.setText("");
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

        int nodeinc = 0;        //initialize a counter

        for (data.Node n : nodes){  //count number of nodes of that type that already exist
            if (n.getNodeType().equals(ntype)){
                nodeinc++;
            }
        }
        nodeinc++;
        return ("S" + ntype + Integer.toString(nodeinc) + "02");
    }



    private String generateEdgeId(data.Node first_node, data.Node second_node) {
        return (first_node.getNodeID() + "_" + second_node.getNodeID());
    }

}
