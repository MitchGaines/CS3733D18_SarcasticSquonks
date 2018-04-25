package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.epic.EpicWrapper;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.List;

public class DrawBloodController {

    @FXML
    private JFXTextField patient_name;

    @FXML
    ComboBox amount_selector = new ComboBox<>();

    @FXML
    ComboBox types_selector = new ComboBox<>();

    @FXML
    private JFXButton submit_report_btn;

    public void initialize() {
        populateAmountSelector();
        populateBloodTypesSelector();
    }

    public void populateAmountSelector() {
        List<String> amounts = Arrays.asList("1", "5", "10", ">10");

        amount_selector.valueProperty().set(null);
        amount_selector.getItems().removeAll(amount_selector.getItems());
        amount_selector.getItems().addAll(amounts);
    }

    public void populateBloodTypesSelector() {
        List<String> types = Arrays.asList("A", "B", "AB", "O");

        types_selector.valueProperty().set(null);
        types_selector.getItems().removeAll(types_selector.getItems());
        types_selector.getItems().addAll(types);
    }

    @FXML
    public void submitReport() {
        // TODO EPIC interaction

        EpicWrapper.send2Epic(ServiceRequest.createService("Blood Report", "Patient's blood report.", Storage.getInstance().getServiceTypesByName("Medical").get(0), null, null, null, true));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service Request Created");
        alert.setHeaderText("Service Request Created");
        alert.setContentText("Your service request was created successfully.");
        alert.showAndWait();

        Main.switchScenes("Nurse View", "/NurseHome.fxml");
    }
}
