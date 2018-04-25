package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.List;

public class CheckVitalsController {

    @FXML
    private JFXTextField patient_name;

    @FXML
    private ComboBox vital_selector = new ComboBox<>();

    @FXML
    private ComboBox abnormality_selector = new ComboBox<>();

    @FXML
    private JFXButton submit_report_btn;

    public void initialize() {
        populateVitalSelector();
        populateAbnormalitiesSelector();
    }

    public void populateVitalSelector() {
        List<String> vitals = Arrays.asList("Blood Pressure", "Pulse Rate", "Respiration Rate", "Body Temperature");

        vital_selector.valueProperty().set(null);
        vital_selector.getItems().removeAll(vital_selector.getItems());
        vital_selector.getItems().addAll(vitals);
    }

    public void populateAbnormalitiesSelector() {
        List<String> abnormalities = Arrays.asList("Yes", "No");

        abnormality_selector.valueProperty().set(null);
        abnormality_selector.getItems().removeAll(abnormality_selector.getItems());
        abnormality_selector.getItems().addAll(abnormalities);
    }

    @FXML
    public void submitReport() {
        // TODO EPIC interaction

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service Request Created");
        alert.setHeaderText("Service Request Created");
        alert.setContentText("Your service request was created successfully.");
        alert.showAndWait();

        Main.switchScenes("Nurse View", "/NurseHome.fxml");
    }
}
