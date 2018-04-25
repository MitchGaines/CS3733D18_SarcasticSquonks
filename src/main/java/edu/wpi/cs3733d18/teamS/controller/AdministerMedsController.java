package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.epic.EpicWrapper;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.List;

public class AdministerMedsController {

    @FXML
    private JFXTextField patient_name;

    @FXML
    ComboBox med_selector = new ComboBox<>();

    @FXML
    ComboBox reaction_selector = new ComboBox<>();

    @FXML
    private JFXButton submit_report_btn;

    public void initialize() {
        populateMedSelector();
        populateReactionSelector();
    }

    public void populateMedSelector() {
        List<String> take_med = Arrays.asList("Yes", "No");

        med_selector.valueProperty().set(null);
        med_selector.getItems().removeAll(med_selector.getItems());
        med_selector.getItems().addAll(take_med);
    }

    public void populateReactionSelector() {
        List<String> reaction = Arrays.asList("Yes", "No");

        reaction_selector.valueProperty().set(null);
        reaction_selector.getItems().removeAll(reaction_selector.getItems());
        reaction_selector.getItems().addAll(reaction);
    }

    @FXML
    public void submitReport() {
        // TODO EPIC interaction

        EpicWrapper.send2Epic(ServiceRequest.createService("Medication Report", "Patient's medication report.", Storage.getInstance().getServiceTypesByName("Medical").get(0), null, null, null, true));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service Request Created");
        alert.setHeaderText("Service Request Created");
        alert.setContentText("Your service request was created successfully.");
        alert.showAndWait();

        Main.switchScenes("Nurse View", "/NurseHome.fxml");
    }
}
