package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.service.ServiceLogEntry;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class AdminSpecialOptionsController{

    private static int selected_path_algorithm = 0;

    TableColumn user_name;
    TableColumn type;

    @FXML
    Button modify_map_btn, delete_user_btn, edit_user_btn;
    @FXML
    JFXComboBox<String> path_algorithm_box;
    @FXML
    TableView<User> user_table;

    public static int getChoosenAlg() {
        return selected_path_algorithm;
    }

    public void onModifyMapClick(ActionEvent event) throws IOException {
        Main.switchScenes("Modify Nodes", "/ModifyNodes.fxml");
    }

    public void chooseAlgorithm() {
        selected_path_algorithm = path_algorithm_box.getSelectionModel().getSelectedIndex();
    }

    private void onAddUser() {
        
    }

    private void populateUserTable() {
        user_table.getColumns().removeAll(user_table.getColumns());
        //TableColumn last_name = new TableColumn("Last Name");
        //TableColumn first_name = new TableColumn("First Name");
        user_name = new TableColumn("User Name");
        type = new TableColumn("Type");

        ObservableList<User> users = FXCollections.observableArrayList(Storage.getInstance().getAllUsers());

        user_name.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        type.setCellValueFactory(new PropertyValueFactory<User, String>("type"));

        user_table.setItems(users);
        user_table.getColumns().addAll(user_name, type);
    }

    public void onUserChoose() {
        User selected_user = user_table.getSelectionModel().getSelectedItem();
        System.out.println(selected_user.getUsername());
    }

    public void initialize() {
        ObservableList<String> algorithms = FXCollections.observableArrayList();
        algorithms.add(AllText.get("a_star"));
        algorithms.add(AllText.get("dijkstras"));
        algorithms.add(AllText.get("depth_first"));
        path_algorithm_box.setItems(algorithms);
        populateUserTable();
    }
}
