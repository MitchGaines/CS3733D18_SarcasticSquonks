package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.SquonksAPI.data.User;
import edu.wpi.cs3733d18.SquonksAPI.database.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientDataController {
    /**
     * Stores the table columns for username, type, lastname, and firstname.
     */
    TableColumn patient_name, type, last_name, first_name;

    /**
     * The User table.
     */
    @FXML
    TableView<User> patient_table;

    /**
     * Populates the patient table.
     */
    private void populatePatientTable() {
        patient_table.getColumns().removeAll(patient_table.getColumns());
        last_name = new TableColumn("Last Name");
        first_name = new TableColumn("First Name");
        patient_name = new TableColumn("Patient Name");
        type = new TableColumn("Type");

        // TODO add patients from EPIC
        ObservableList<User> users = FXCollections.observableArrayList(/* TODO */);

        last_name.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        first_name.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        patient_name.setCellValueFactory(new PropertyValueFactory<User, String>("PatientName"));
        type.setCellValueFactory(new PropertyValueFactory<User, String>("type"));

        patient_table.setItems(users);
        patient_table.getColumns().addAll(last_name, first_name, patient_name, type);
    }

    /**
     * Initializes the scene.
     */
    public void initialize() {
        populatePatientTable();
    }

    @FXML
    private JFXButton back_btn;

    @FXML
    public void onBackClick() {
        Main.switchScenes("Nurse Home", "/NurseHome.fxml");
    }
}
