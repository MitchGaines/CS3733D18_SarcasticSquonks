package edu.wpi.cs3733d18.teamS.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RequestSidebarController {
    @FXML
    Label create_req, my_req, view_log, reports, admin;

    public void onCreateReqClick() {
        clearColors();
        create_req.setStyle("-fx-background-color:  #91a1c6");
    }

    public void onMyReqClick() {
        clearColors();
        my_req.setStyle("-fx-background-color:  #91a1c6");
    }

    public void onViewLogClick() {
        clearColors();
        view_log.setStyle("-fx-background-color:  #91a1c6");
    }

    public void onReportsClick() {
        clearColors();
        reports.setStyle("-fx-background-color:  #91a1c6");
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
