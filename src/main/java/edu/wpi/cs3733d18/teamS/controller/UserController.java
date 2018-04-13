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

    public void onLogoutClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        Scene home_scene = new Scene(root, window.getWidth(), window.getHeight());
        Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");

        Timeout.addListenersToScene(home_scene);

        home_stage.setScene(home_scene);
        home_stage.show();
    }

    private void setUser(User user) {
        this.user = user;
        serviceAreaController.setUser(user);
        populateBoxes();
    }

    public void setUp(User user, String page) {
        setUser(user);
        setPage(page);
        populateBoxes();
    }

    private void populateBoxes() {
        serviceAreaController.populateRequestTypes();
        serviceAreaController.populateRequestsBox();
    }

    public void openLog(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Log.fxml"), AllText.getBundle());
        Parent root = loader.load();
        LogController log_controller = loader.getController();
        Scene home_scene = new Scene(root, window.getWidth(), window.getHeight());
        Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");

        Timeout.addListenersToScene(home_scene);

        home_stage.setScene(home_scene);
        log_controller.setUser(user);
        log_controller.setReturnPage(page);
        log_controller.populateTable();

        home_stage.show();
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

    public void initialize() {
        serviceAreaController.setParent(this);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        //dismissEmergency();
    }

}
