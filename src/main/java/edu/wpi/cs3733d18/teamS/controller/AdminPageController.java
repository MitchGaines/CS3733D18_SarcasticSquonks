package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AdminPageController extends UserController {

    private static int selected_path_algorithm = 0;
    //UserController edu.wpi.cs3733d18.teamS.user = new UserController();
    UserController user = new UserController();

    @FXML
    Button modify_map_btn;
    @FXML
    BorderPane main_pane;
    @FXML
    Label time;
    @FXML
    JFXComboBox<String> path_algorithm_box;

    public static int getChoosenAlg() {
        return selected_path_algorithm;
    }

    public void loadReportScreen(ActionEvent event) throws IOException {
        Main.switchScenes("View Reports", "/Report.fxml");
    }

    public void onModifyMapClick(ActionEvent event) throws IOException {
        Main.switchScenes("Modify Nodes", "/ModifyNodes.fxml");
    }

    public void initialize() {
        serviceAreaController.setParent(this);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        ObservableList<String> algorithms = FXCollections.observableArrayList();
        algorithms.add(AllText.get("a_star"));
        algorithms.add(AllText.get("dijkstras"));
        algorithms.add(AllText.get("depth_first"));
        path_algorithm_box.setItems(algorithms);

    }

    public void chooseAlgorithm() {
        this.selected_path_algorithm = path_algorithm_box.getSelectionModel().getSelectedIndex();

    }
}
