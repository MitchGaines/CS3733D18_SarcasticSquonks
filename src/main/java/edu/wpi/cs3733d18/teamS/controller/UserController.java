package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserController {

    @FXML
    // this cannot adhere to the style guide, the name must be like that or the JavaFX won't properly link. Sorry :(
    protected ServiceAreaController serviceAreaController;
    @FXML
    Button logout_btn;
    @FXML
    BorderPane main_pane;
    @FXML
    Label time;
    private User user;
    private String page;
    @FXML
    private Label emergency_title, emergency_details, emergency_label;
    @FXML
    private Text user_name;

    @FXML
    protected RequestSidebarController requestSidebarController;

    public void onLogoutClick(ActionEvent event) throws IOException {
        Main.switchScenes("Brigham and Women's", "/HomePage.fxml");
    }

    private void setUser(User user) {
        this.user = user;
        serviceAreaController.setUser(user);
        requestSidebarController.setUser(user);
        user_name.setText(user.getFirstName() + " " + user.getLastName());
    }

    public void setUp(User user, String page) {
        setUser(user);
        setPage(page);
        populateBoxes();
        setSidebar();
    }

    protected void populateBoxes() {
        serviceAreaController.setParent(this);
        serviceAreaController.populateRequestTypes();
        serviceAreaController.populateRequestsBox();
    }

    private void setPage(String page) {
        this.page = page;
    }

    public void declareEmergency(String title, edu.wpi.cs3733d18.teamS.data.Node location, String description) {
        if (emergency_title != null && emergency_details != null && emergency_label != null) {
            emergency_title.setText(title + ", " + location.getShortName());
            emergency_details.setText(description);
            emergency_label.setVisible(true);
            emergency_title.setVisible(true);
            emergency_details.setVisible(true);
        }
    }

    public void dismissEmergency() {
        if (emergency_title != null && emergency_details != null && emergency_label != null) {
            emergency_label.setVisible(false);
            emergency_title.setVisible(false);
            emergency_details.setVisible(false);
        }

    }

    public void loadLog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Log.fxml"), AllText.getBundle());
        Parent root = loader.load();
        LogController log_controller = loader.getController();
        log_controller.setUser(user);
        //log_controller.setReturnPage(page);
        log_controller.populateTable();
        main_pane.setCenter(root);
    }

    public void setSidebar() {
        if (user.getType() != User.user_type.ADMIN_STAFF) {
            requestSidebarController.getSidebar().getChildren().get(3).setVisible(false);
            requestSidebarController.getSidebar().getChildren().get(4).setVisible(false);
        }
    }

    public void MakeRequest() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MakeRequest.fxml"), AllText.getBundle());
        Parent root = loader.load();
        serviceAreaController = loader.getController();
        serviceAreaController.setUpToMake(user);
        main_pane.setCenter(root);
    }

    public void completeRequest() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CompleteRequest.fxml"), AllText.getBundle());
        Parent root = loader.load();
        serviceAreaController = loader.getController();
        serviceAreaController.setUpToComplete(user);
        main_pane.setCenter(root);
    }

    public void initialize() throws IOException {
        serviceAreaController.setParent(this);
        requestSidebarController.setParent(this);
        MakeRequest();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        //dismissEmergency();
    }

}
