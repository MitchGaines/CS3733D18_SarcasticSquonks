package controller;

import internationalization.AllText;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import service.ServiceType;
import user.User;

import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController{

    @FXML
    Button logout_btn;

    public void onLogoutClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        Scene home_scene = new Scene(root);
        Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        home_stage.show();
    }

    private User user;

    public void setUser(User user) {
        this.user = user;
        serviceAreaController.setUser(user);
    }

    @FXML
    // this cannot adhere to the style guide, the name must be like that or the JavaFX won't properly link. Sorry :(
    private ServiceAreaController serviceAreaController;


    public void populateBoxes() {
        serviceAreaController.populateRequestsBox();
        serviceAreaController.populateRequestTypes();
    }

    public void openLog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Log.fxml"), AllText.getBundle());
        Parent root = loader.load();
        LogController log_controller = loader.getController();
        Scene home_scene = new Scene(root);
        Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        log_controller.setUser(user);
        log_controller.setReturnPage(page);
        log_controller.populateTable();

        home_stage.show();
    }

    private String page;

    public void setPage(String page) {
        this.page = page;
    }

    @FXML
    private Label emergency_title, emergency_details, emergency_label;

    public void declareEmergency(String title, String location, String description) {
        if (emergency_title != null && emergency_details != null && emergency_label != null) {
            emergency_title.setText(title + ", " + location);
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
    }

}
