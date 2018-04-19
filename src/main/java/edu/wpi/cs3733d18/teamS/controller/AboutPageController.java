package edu.wpi.cs3733d18.teamS.controller;

import com.gluonhq.charm.glisten.control.ExpansionPanel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.user.InvalidPasswordException;
import edu.wpi.cs3733d18.teamS.user.InvalidUsernameException;
import edu.wpi.cs3733d18.teamS.user.LoginHandler;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;



public class AboutPageController {

    @FXML
    BorderPane main_pane;

    @FXML
    StackPane stack_pane;

    @FXML
    JFXButton back;

    @FXML
    JFXListView<String> names_list;
    @FXML
    JFXListView<String> positions_list;

    @FXML
    void onBackClick(ActionEvent event) {    // about screen
        Main.switchScenes("Back", "/HomePage.fxml");
    }

    public void initialize() {
        names_list.setMouseTransparent( true );
        names_list.setFocusTraversable( false);
        populateNames();

        positions_list.setMouseTransparent( true );
        positions_list.setFocusTraversable( false);
        populatePositions();
    }

    public void populateNames(){
        names_list.getItems().add("Jonathan Gaines");
        names_list.getItems().add("Noah Hillman");
        names_list.getItems().add("William Lucca");
        names_list.getItems().add("Cormac Lynch-Collier");
        names_list.getItems().add("Matthew McMillan");
        names_list.getItems().add("Matthew Puentes");
        names_list.getItems().add("Mathew Schwartzman");
        names_list.getItems().add("Orion Strickland");
        names_list.getItems().add("Daniel Sullivan");
        names_list.getItems().add("Joseph Turcotte");


    }

    public void populatePositions(){
        positions_list.getItems().add("Lead Developer");
        positions_list.getItems().add("Algorithms Specialist");
        positions_list.getItems().add("Software Engineer");
        positions_list.getItems().add("Product Owner");
        positions_list.getItems().add("Assistant Software Lead");
        positions_list.getItems().add("Assistant Software Lead");
        positions_list.getItems().add("Project Manager");
        positions_list.getItems().add("Documentation Analyst");
        positions_list.getItems().add("Test Engineer");
        positions_list.getItems().add("Scrummaster");

    }



}
