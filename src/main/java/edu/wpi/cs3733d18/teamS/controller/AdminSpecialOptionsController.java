package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminSpecialOptionsController{

    private static int selected_path_algorithm = 0;

    @FXML
    Button modify_map_btn;
    @FXML
    JFXComboBox<String> path_algorithm_box;

    public static int getChoosenAlg() {
        return selected_path_algorithm;
    }

    public void onModifyMapClick(ActionEvent event) throws IOException {
        Main.switchScenes("Modify Nodes", "/ModifyNodes.fxml");
    }

    public void chooseAlgorithm() {
        selected_path_algorithm = path_algorithm_box.getSelectionModel().getSelectedIndex();
    }

    public void initialize() {
        ObservableList<String> algorithms = FXCollections.observableArrayList();
        algorithms.add(AllText.get("a_star"));
        algorithms.add(AllText.get("dijkstras"));
        algorithms.add(AllText.get("depth_first"));
        path_algorithm_box.setItems(algorithms);
    }
}
