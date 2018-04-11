package controller;

import com.gluonhq.charm.glisten.control.ExpansionPanel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import database.Storage;
import internationalization.AllText;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Window;
import user.InvalidPasswordException;
import user.InvalidUsernameException;
import pathfind.AStar;
import pathfind.BreadthFirst;
import user.LoginHandler;
import user.User;

public class HomePageController {

    private static boolean include_stairs = true;

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
    Label time, time2;

    @FXML
    BorderPane main_pane;

    @FXML
    JFXComboBox<data.Node> combobox_start;

    @FXML
    JFXComboBox<data.Node> combobox_end;

    @FXML
    JFXComboBox language_selector;

    @FXML
    StackPane stack_pane;

    @FXML
    ExpansionPanel exp_panel;

    private LoginHandler loginHandler;

    @FXML
    JFXButton REST;

    @FXML
    JFXButton DEPT;

    @FXML
    JFXButton INFO;

    @FXML
    JFXToggleButton stairs_toggle;


    /**
     * Performs this function during creation of Controller; sets up the ComboBoxes
     * by pulling all nodes from the database
     * @author Will Lucca
     */
    public void initialize() {
        ObservableList<data.Node> locations = FXCollections.observableArrayList();
        Storage storage = Storage.getInstance();
        locations.addAll(storage.getAllNodes());

        ArrayList<data.Node> to_remove = new ArrayList<data.Node>();
        for(data.Node location : locations){
            if(location.getNodeType().equals("HALL") ||
                    location.getNodeType().equals("ELEV") ||
                    location.getNodeType().equals("STAI") ||
                    location.getShortName().equals("CRN")){

                to_remove.add(location);
            }
        }
        locations.removeAll(to_remove);
        Collections.sort(locations, Comparator.comparing(data.Node::getLongName));

        combobox_start.setItems(locations);
        combobox_start.setValue(locations.iterator().next());
        combobox_end.setItems(locations);

        loginHandler = new LoginHandler();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        time2.setText(dtf.format(now));
        language_selector.getItems().removeAll(language_selector.getItems());
        for (String language : AllText.getLanguages()) {
            language_selector.getItems().add(AllText.get(language));
        }

        login_btn.defaultButtonProperty().bind(Bindings.or(username.focusedProperty(), password.focusedProperty()));
    }

    public void onLanguageChange() {
        if (language_selector.getSelectionModel().isEmpty()) {
            return;
        }
        try {
            AllText.changeLanguage(AllText.getLanguages()[language_selector.getSelectionModel().getSelectedIndex()]);
            Parent root   = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
            Stage primary_stage = (Stage) language_selector.getScene().getWindow();
            primary_stage.setTitle("Brigham and Women's");
            Scene primary_scene = new Scene(root, 1200, 800);

            Timeout.addListenersToScene(primary_scene);

            primary_stage.setScene(primary_scene);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (combobox_start.getSelectionModel().isEmpty() || combobox_end.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Please select a starting and ending location");
            alert.setHeaderText("Please select a starting and ending location");
            alert.setContentText("You must select both a starting and ending location to get directions.");
            alert.showAndWait();
        } else if (combobox_start.getValue().equals(combobox_end.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Your starting and ending locations can't be the same");
            alert.setHeaderText("Your starting and ending locations can't be the same");
            alert.setContentText("You must select a starting and ending location that are different from each other.");
            alert.showAndWait();
        }
        include_stairs = stairs_toggle.isSelected();
        Window window = main_pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PathfindPage.fxml"), AllText.getBundle());
        Parent pathfind_parent = (Parent)loader.load();
        PathfindController pathfind_ctrl = loader.getController();
        pathfind_ctrl.doPathfinding(combobox_start.getValue().getNodeID(), combobox_end.getValue().getNodeID());
        if(PathfindController.isPathfindReady()){
            Stage pathfind_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            pathfind_stage.setTitle("Pathfinder");

            Scene pathfind_parent_scene = new Scene(pathfind_parent, window.getWidth(), window.getHeight());

            Timeout.addListenersToScene(pathfind_parent_scene);

            pathfind_stage.setScene(pathfind_parent_scene);
            pathfind_stage.show();
        }
    }

    @FXML
    void onQuickClick(ActionEvent event) throws IOException{
        Button button = (Button) event.getSource();
        if(combobox_start.getValue().getNodeID().contains(button.getId())){
            return;
        }
        Window window = main_pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PathfindPage.fxml"), AllText.getBundle());
        Parent pathfind_parent = (Parent)loader.load();
        PathfindController pathfind_ctrl = loader.getController();
        pathfind_ctrl.quickLocationFinding(combobox_start.getValue().getNodeID(), button.getId());

        Stage pathfind_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pathfind_stage.setTitle("Pathfinder");

        Scene pathfind_parent_scene = new Scene(pathfind_parent, window.getWidth(), window.getHeight());

        Timeout.addListenersToScene(pathfind_parent_scene);

        pathfind_stage.setScene(pathfind_parent_scene);
        pathfind_stage.show();
    }

    //THIS IS A TEST TO TRY OUT DIFFERENT USERS
    public void onLoginClick(ActionEvent event) throws IOException {
        try {
            User user = loginHandler.login(username.getText(), password.getText());
            System.out.println(user);
            if (user.getType() == User.user_type.DOCTOR) {
                openUser(event, "/DoctorPage.fxml", user);
            }
            else if (user.getType() == User.user_type.ADMIN_STAFF) {
                openUser(event, "/AdminPage.fxml", user);
            }
            else if (user.getType() == User.user_type.REGULAR_STAFF) {
                openUser(event, "/RegStaffPage.fxml", user);
            }
        } catch (InvalidPasswordException e) {
            wrong_credentials.setText("Wrong username or password");
        } catch (InvalidUsernameException e) {
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

        Timeout.addListenersToScene(user_scene);

        user_stage.setScene(user_scene);
        user_stage.show();
    } //END OF TEST

    public static boolean includeStairs(){
        return include_stairs;
    }
}
