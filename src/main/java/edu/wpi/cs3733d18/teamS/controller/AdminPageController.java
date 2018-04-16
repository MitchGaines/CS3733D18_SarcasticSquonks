package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminPageController extends UserController {

    //UserController edu.wpi.cs3733d18.teamS.user = new UserController();
    UserController user = new UserController();

    @FXML
    BorderPane main_pane;
    @FXML
    Label time;
    @FXML
    StackPane stack_pane;
    @FXML
    Text user_name;

    FXMLLoader loader;

    public void loadReports() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/Report.fxml"), AllText.getBundle());
        Parent root = loader.load();
        main_pane.setCenter(root);
    }

    public void loadAdmin() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/AdminSpecialOptions.fxml"), AllText.getBundle());
        Parent root = loader.load();
        main_pane.setCenter(root);
    }


    public void initialize() throws IOException {
        requestSidebarController.setParent(this);
        requestSidebarController.setAdminParent(this);
        serviceAreaController.setParent(this);
        MakeRequest();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
    }

}
