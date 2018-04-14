package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RequestSidebarController {

    private UserController parent;
    private AdminPageController adminParent;
    private BorderPane main_pane;

    @FXML
    private Label create_req, my_req, view_log, reports, admin;

    public void setParent(UserController user_controller) {
        parent = user_controller;
    }

    public void setAdminParent(AdminPageController adminPageController) {
        adminParent = adminPageController;
    }

    public void onCreateReqClick() {
        clearColors();
        create_req.setStyle("-fx-background-color:  #91a1c6");
    }

    public void onMyReqClick() {
        clearColors();
        my_req.setStyle("-fx-background-color:  #91a1c6");
    }

    public void onViewLogClick() throws IOException {
        clearColors();
        view_log.setStyle("-fx-background-color:  #91a1c6");
        parent.loadLog();
    }

    public void onReportsClick() throws IOException {
        clearColors();
        reports.setStyle("-fx-background-color:  #91a1c6");
        adminParent.loadReports();
    }

    public void onAdminClick() {
        clearColors();
        admin.setStyle("-fx-background-color:  #91a1c6");
    }

    private void clearColors() {
        create_req.setStyle("-fx-background-color: #4863A0");
        my_req.setStyle("-fx-background-color: #4863A0");
        view_log.setStyle("-fx-background-color: #4863A0");
        reports.setStyle("-fx-background-color: #4863A0");
        admin.setStyle("-fx-background-color: #4863A0");
    }


}
