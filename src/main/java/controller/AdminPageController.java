package controller;

import internationalization.AllText;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController extends UserController {

    UserController user = new UserController();

    @FXML
    Button modify_map_btn;
    @FXML
    BorderPane main_pane;
    @FXML
    Label time;

    public void onModifyMapClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        Parent modify_nodes_parent = FXMLLoader.load(getClass().getResource("/ModifyNodes.fxml"), AllText.getBundle());
        Scene modify_nodes_scene = new Scene(modify_nodes_parent, window.getWidth(), window.getHeight());
        Stage modify_nodes_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modify_nodes_stage.setTitle("Modify Nodes");
        modify_nodes_stage.setScene(modify_nodes_scene);
        modify_nodes_stage.show();
    }

    public void initialize() {
        serviceAreaController.setParent(this);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
    }
}
