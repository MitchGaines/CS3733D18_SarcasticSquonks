package controller;

import database.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import user.LoginHandler;
import user.User;

public class HomePageController implements Initializable {

    @FXML
    Button pathfind;

    @FXML
    Button login_btn;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Label wrong_credentials;

    @FXML
    ComboBox<data.Node> combobox_start;
    @FXML
    ComboBox<data.Node> combobox_end;

    /**
     * Performs this function during creation of Controller; sets up the ComboBoxes
     * by pulling all nodes from the database
     * @param location
     * @param resources
     * @author Will Lucca
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<data.Node> locations = FXCollections.observableArrayList();
        locations.addAll(Storage.getInstance().getAllNodes());

        combobox_start.setItems(locations);
        combobox_end.setItems(locations);
    }

    /**
     * Sets start_loc and end_loc to the values selected in the combobox, then switches view to
     * PathfindPage, initializing PathfindController
     * @param event
     * @throws IOException
     */
    @FXML
    void onPathfindClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PathfindPage.fxml"));
        Parent pathfind_parent = (Parent)loader.load();
        PathfindController pathfind_ctrl = loader.getController();
        pathfind_ctrl.doPathfinding(combobox_start.getValue(), combobox_end.getValue());

        Stage pathfind_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pathfind_stage.setTitle("Pathfinder");
        pathfind_stage.setScene(new Scene(pathfind_parent));
        pathfind_stage.show();
    }

    //THIS IS A TEST TO TRY OUT DIFFERENT USERS
    public void onLoginClick(ActionEvent event) throws IOException {
        String name = username.getText();
        if (name.equals("doctor")) {
            openUser(event, "/DoctorPage.fxml", LoginHandler.getUsers().get(0));
        }
        else if (name.equals("admin")) {
            openUser(event, "/AdminPage.fxml", LoginHandler.getUsers().get(1));
        }
        if (name.equals("regstaff")) {
            openUser(event, "/RegStaffPage.fxml", LoginHandler.getUsers().get(2));
        }
        else {
            wrong_credentials.setText("Wrong username or password");
        }
    }

    //PART OF THE USER TEST
    public void openUser (ActionEvent event, String page, User user) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
//             Parent user_parent = FXMLLoader.load(getClass().getResource(page));
        Parent user_parent = (Parent)loader.load();
        UserController controller = loader.<UserController>getController();
        controller.setUser(user);
        controller.populateBoxes();
        Scene user_scene = new Scene(user_parent);
        Stage user_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user_stage.setTitle("User");
        user_stage.setScene(user_scene);
        user_stage.show();
    } //END OF TEST
}
