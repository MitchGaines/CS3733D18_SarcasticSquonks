package controller;

import com.gluonhq.charm.glisten.control.ExpansionPanel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import database.Storage;
import internationalization.AllText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.time.Clock;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Window;
import user.LoginHandler;
import user.User;

public class HomePageController {

    @FXML
    JFXButton pathfind;

    @FXML
    JFXButton login_btn;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Label wrong_credentials;

    @FXML
    Label time;

    @FXML
    BorderPane main_pane;

    @FXML
    JFXComboBox<data.Node> combobox_start;

    @FXML
    JFXComboBox<data.Node> combobox_end;

    @FXML
    MenuButton language_selector;

    /**
     * Performs this function during creation of Controller; sets up the ComboBoxes
     * by pulling all nodes from the database
     * @author Will Lucca
     */
    public void initialize() {
        ObservableList<data.Node> locations = FXCollections.observableArrayList();
        locations.addAll(Storage.getInstance().getAllNodes());

        combobox_start.setItems(locations);
        combobox_end.setItems(locations);
        populateLanguages();
    }

    private void populateLanguages() {
        String[] languages = AllText.getLanguages();
        language_selector.getItems().removeAll(language_selector.getItems());
        for (String language : languages) {
            MenuItem menuItem  = new MenuItem(AllText.get(language));
            menuItem.setOnAction(e -> {
                AllText.changeLanguage(language);
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
                    Stage primary_stage = (Stage)language_selector.getScene().getWindow();
                    primary_stage.setTitle("Brigham and Women's");
                    primary_stage.setScene(new Scene(root, 1200, 800));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            language_selector.getItems().add(menuItem);
        }


    }

    /**
     * Sets start_loc and end_loc to the values selected in the combobox, then switches view to
     * PathfindPage, initializing PathfindController
     * @param event
     * @throws IOException
     */
    @FXML
    void onPathfindClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PathfindPage.fxml"), AllText.getBundle());
        Parent pathfind_parent = (Parent)loader.load();
        PathfindController pathfind_ctrl = loader.getController();
        pathfind_ctrl.doPathfinding(combobox_start.getValue(), combobox_end.getValue());

        Stage pathfind_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pathfind_stage.setTitle("Pathfinder");
        pathfind_stage.setScene(new Scene(pathfind_parent, window.getWidth(), window.getHeight()));
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
        else if (name.equals("regstaff")) {
            openUser(event, "/RegStaffPage.fxml", LoginHandler.getUsers().get(2));
        }
        else {
            wrong_credentials.setText("Wrong username or password");
        }
    }

    //PART OF THE USER TEST
    public void openUser (ActionEvent event, String page, User user) throws IOException{
        Window window = main_pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page), AllText.getBundle());
        Parent user_parent = (Parent)loader.load();
        UserController controller = loader.<UserController>getController();
        controller.setUser(user);
        controller.setPage(page);
        controller.populateBoxes();
        Scene user_scene = new Scene(user_parent, window.getWidth(), window.getHeight());
        Stage user_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user_stage.setTitle("User");
        user_stage.setScene(user_scene);
        user_stage.show();
    } //END OF TEST
}
