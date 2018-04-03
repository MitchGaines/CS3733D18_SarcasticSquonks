package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import user.LoginHandler;
import user.User;

public class HomePageController {

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

    public void onPathfindClick(ActionEvent event) throws IOException {
        Parent pathfind_parent = FXMLLoader.load(getClass().getResource("/PathfindPage.fxml"));
        Scene pathfind_scene = new Scene(pathfind_parent);
        Stage pathfind_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pathfind_stage.setTitle("Pathfinder");
        pathfind_stage.setScene(pathfind_scene);
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
