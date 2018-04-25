package edu.wpi.cs3733d18.teamS.controller;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class CreditsController {

    @FXML
    BorderPane main_pane;

    @FXML
    StackPane stack_pane;

    @FXML
    JFXButton back;

    @FXML
    void onBackClick(ActionEvent event) {    // about screen
        Main.switchScenes("Back", "/HomePage.fxml");
    }

    @FXML
    void onHomeClick(ActionEvent event) {Main.switchScenes("Home", "/HomePage.fxml");}

}
