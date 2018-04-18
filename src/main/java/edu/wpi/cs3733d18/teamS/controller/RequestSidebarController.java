package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class RequestSidebarController {

    private UserController parent;
    private AdminPageController adminParent;
    private User user;

    @FXML
    private Label create_req, my_req, view_log, reports, user_type_label, user_reports;

    @FXML
    GridPane sidebar;

    public void setUser(User user) {
        this.user = user;
    }

    public void initialize() {
        sidebar.setStyle("-fx-background-color: #4863A0");
    }

    public GridPane getSidebar() {
        return sidebar;
    }

    public void setParent(UserController user_controller) {
        parent = user_controller;
    }

    public void setAdminParent(AdminPageController adminPageController) {
        adminParent = adminPageController;
    }

    public void setEmergency() {
        my_req.setStyle("-fx-background-color: #ff0000");
    }

    public void fulfillEmerg() {
        clearColors();
    }

    public void onCreateReqClick() throws IOException {
        clearColors();
        create_req.setStyle("-fx-background-color:  #91a1c6");
        parent.MakeRequest();
        parent.populateBoxes();
    }

    public void onMyReqClick() throws IOException {
        clearColors();
        my_req.setStyle("-fx-background-color:  #91a1c6");
        parent.completeRequest();
        parent.populateBoxes();
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

    public void onUserReportsClick() throws IOException {
        clearColors();
        user_reports.setStyle("-fx-background-color:  #91a1c6");
        adminParent.loadUserReports();
    }

    public void onAdminClick() throws IOException {
        clearColors();
        user_type_label.setStyle("-fx-background-color:  #91a1c6");
        adminParent.loadAdmin();
    }

    private void clearColors() {
        create_req.setStyle("-fx-background-color: #4863A0");
        my_req.setStyle("-fx-background-color: #4863A0");
        view_log.setStyle("-fx-background-color: #4863A0");
        reports.setStyle("-fx-background-color: #4863A0");
        user_reports.setStyle("-fx-background-color: #4863A0");
        user_type_label.setStyle("-fx-background-color: #4863A0");
    }


}
