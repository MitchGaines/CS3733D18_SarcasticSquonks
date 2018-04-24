package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d18.SquonksAPI.controller.SquonksAPI;
import edu.wpi.cs3733d18.teamS.arduino.MotionSensor;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamS.service.ServiceLogEntry;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.derby.iapi.types.UserType;

import java.io.IOException;
import java.sql.Time;

/**
 * This controller deals with special options for the admin users.
 * @author Cormac Lynch-Collier
 * @author Noah Hillman
 * @version 1.3, April 13, 2018
 */
public class AdminSpecialOptionsController{

    /**
     * The default (AStar) path algorithm.
     */
    private static int selected_path_algorithm = 0;

    /**
     * Stores the table columns for username, type, lastname, and firstname.
     */
    TableColumn user_name, type, last_name, first_name;

    /**
     * Stores the selected user.
     */
    User selected_user;

    /**
     * The boxes for modifying user data.
     */
    @FXML
    VBox add_user_box, delete_user_box, modify_user_box;

    /**
     * The text fields for username, password, firstname, lastname, user to delete, modify firstname and modify lastname.
     */
    @FXML
    JFXTextField username_field, password_field, first_name_field, last_name_field, user_to_delete, modify_firstname, modify_lastname;

    /**
     * The text fields for modifying the username and password.
     */
    @FXML
    JFXTextField modify_username, modify_password, timeout_field;

    /**
     * Buttons for the modify map, delete user, add user, confirm delete and cancel delete.
     */
    @FXML
    Button modify_map_btn, delete_user_btn, modify_user, add_user_btn, confirm_delete_btn, cancel_delete_btn, set_timeout_btn;

    /**
     * The combo box for choosing the pathfind algorithm.
     */
    @FXML
    JFXComboBox<String> path_algorithm_box;

    /**
     * The combo box for modifying users.
     */
    @FXML
    JFXComboBox<User.user_type> type_user, type_user_modify;

    /**
     * The User table.
     */
    @FXML
    TableView<User> user_table;

    private AdminPageController parent;

    public void setParent(AdminPageController parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the chosen algorithm.
     * @return the value for the selected path algorithm.
     */
    public static int getChoosenAlg() {
        return selected_path_algorithm;
    }

    /**
     * Switches scene to the modify map screen.
     * @param event The click on the button.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void onModifyMapClick(ActionEvent event) throws IOException {
        ModifyMapController mmc = (ModifyMapController) Main.switchScenes("Modify Nodes", "/ModifyNodes.fxml");
        mmc.setUp(user, page);

    }

    private User user;
    private String page;

    private void setUser(User user) {
        this.user = user;
    }

    private void setPage(String page) {
        this.page = page;
    }

    public void setUp(User user, String page) {
        setUser(user);
        setPage(page);
    }

    /**
     * Sets the selected path algorithm value to the corresponding choice from the menu.
     */
    public void chooseAlgorithm() {
        selected_path_algorithm = path_algorithm_box.getSelectionModel().getSelectedIndex();
    }

    /**
     * Adds a user to the database.
     */
    public void onAddUser() {
        if(!first_name_field.getText().equals("") && !last_name_field.getText().equals("") && !username_field.getText().equals("")
                && !password_field.getText().equals("") && !type_user.getSelectionModel().isEmpty()) {
            boolean can_mod = false;
            if (type_user.getValue() == User.user_type.ADMIN_STAFF) {
                can_mod = true;
            }
            User new_user = new User(username_field.getText(), password_field.getText(), first_name_field.getText(), last_name_field.getText(), type_user.getValue(), can_mod);
            Storage.getInstance().saveUser(new_user);
            populateUserTable();
            first_name_field.setText("");
            last_name_field.setText("");
            username_field.setText("");
            password_field.setText("");
            type_user.getSelectionModel().clearSelection();
        }
    }

    /**
     * Populates the user table.
     */
    private void populateUserTable() {
        user_table.getColumns().removeAll(user_table.getColumns());
        last_name = new TableColumn("Last Name");
        first_name = new TableColumn("First Name");
        user_name = new TableColumn("User Name");
        type = new TableColumn("Type");

        ObservableList<User> users = FXCollections.observableArrayList(Storage.getInstance().getAllUsers());

        last_name.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        first_name.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        user_name.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        type.setCellValueFactory(new PropertyValueFactory<User, String>("type"));

        user_table.setItems(users);
        user_table.getColumns().addAll(last_name, first_name, user_name, type);
    }

    /**
     * Deletes a user.
     */
    public void onDeleteUser() {
        if(selected_user != null) {
            add_user_box.setVisible(false);
            delete_user_box.setVisible(true);
            user_to_delete.setText(selected_user.getFirstName() + " " + selected_user.getLastName());
        }
    }

    /**
     * Confirms a user to be deleted.
     */
    public void onConfirmDelete() {
        Storage.getInstance().deleteUser(selected_user);
        populateUserTable();
        user_to_delete.setText("");
        add_user_box.setVisible(true);
        delete_user_box.setVisible(false);
    }

    /**
     * Cancels a user being deleted.
     */
    public void onCancelDelete() {
        add_user_box.setVisible(true);
        delete_user_box.setVisible(false);
        user_to_delete.setText("");
    }

    /**
     * Modifies a users data.
     */
    public void onModifyUser() {
        if(selected_user != null) {
            add_user_box.setVisible(false);
            modify_user_box.setVisible(true);
            modify_firstname.setText(selected_user.getFirstName());
            modify_lastname.setText(selected_user.getLastName());
            modify_username.setText(selected_user.getUsername());
            type_user_modify.getSelectionModel().select(selected_user.getType());
            ObservableList<User.user_type> types_list = FXCollections.observableArrayList();
            types_list.addAll(User.user_type.ADMIN_STAFF, User.user_type.DOCTOR, User.user_type.REGULAR_STAFF);
            type_user_modify.setItems(types_list);
        }
    }

    /**
     * Confirms the changes made to the user.
     */
    public void onConfirmModify() {
        if(!type_user_modify.getSelectionModel().isEmpty() && !modify_firstname.getText().equals("") && !modify_lastname.getText().equals("")
                && !modify_password.getText().equals("") && !modify_username.getText().equals("")) {
            selected_user.setType(type_user_modify.getValue());
            selected_user.setLastName(modify_lastname.getText());
            selected_user.setFirstName(modify_firstname.getText());
            selected_user.setPassword(modify_password.getText());
            selected_user.setUsername(modify_username.getText());
            Storage.getInstance().updateUser(selected_user);
            populateUserTable();
            modify_user_box.setVisible(false);
            add_user_box.setVisible(true);
        }
    }

    public void onSetTimeoutClick(){
        Timeout.sleep_time = Integer.parseInt(timeout_field.getText().toString().replaceAll(",", "")) * 1000;
        Timeout.stop();
        Timeout.start();
    }

    public void onEnableSetButton(){
        set_timeout_btn.setVisible(true);
    }


    /**
     * cancels changes made to the user.
     */
    public void onCancelModify() {
        modify_user_box.setVisible(false);
        add_user_box.setVisible(true);
    }

    /**
     * sets the selected user.
     */
    public void onUserChoose() {
        selected_user = user_table.getSelectionModel().getSelectedItem();
    }

    @FXML
    Button it_request;

    public void onITRequest() {
        Timeout.stop();
        SquonksAPI squonks_api = new SquonksAPI();
        squonks_api.run(100, 30, 900, 600, null, null, new Stage());
        Timeout.start();
    }

    /**
     * Initializes the scene.
     */
    public void initialize() {
        ObservableList<String> algorithms = FXCollections.observableArrayList();
        algorithms.add(AllText.get("a_star"));
        algorithms.add(AllText.get("dijkstras"));
        algorithms.add(AllText.get("depth_first"));
        algorithms.add(AllText.get("breadth_first"));
        path_algorithm_box.setItems(algorithms);
        populateUserTable();
        ObservableList<User.user_type> types_list = FXCollections.observableArrayList();
        types_list.addAll(User.user_type.ADMIN_STAFF, User.user_type.DOCTOR, User.user_type.REGULAR_STAFF);
        type_user.setItems(types_list);

        add_user_box.setVisible(true);
        delete_user_box.setVisible(false);
        modify_user_box.setVisible(false);
        timeout_field.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    }

    @FXML
    private Button special_request;

    @FXML
    public void onSpecialRequest() throws IOException {
        parent.loadSpecialRequests();
    }
}
