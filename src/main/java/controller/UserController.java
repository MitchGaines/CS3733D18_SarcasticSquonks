package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
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

}
