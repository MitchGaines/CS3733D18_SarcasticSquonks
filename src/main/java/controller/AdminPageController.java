package controller;

import internationalization.AllText;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController extends UserController {

    //UserController user = new UserController();

    @FXML
    Button modify_map_btn;

    public void onModifyMapClick(ActionEvent event) throws IOException {
        Parent modify_nodes_parent = FXMLLoader.load(getClass().getResource("/ModifyNodes.fxml"), AllText.getBundle());
        Scene modify_nodes_scene = new Scene(modify_nodes_parent);
        Stage modify_nodes_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modify_nodes_stage.setTitle("Modify Nodes");
        modify_nodes_stage.setScene(modify_nodes_scene);
        modify_nodes_stage.show();
    }
}
