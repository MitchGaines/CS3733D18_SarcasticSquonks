package edu.wpi.cs3733d18.teamS.controller;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

<<<<<<< HEAD
=======
import javax.swing.*;

>>>>>>> 979eed1225f3e3e2eccb7abfc8151db897718572
public class CreditsController {

    @FXML
    BorderPane main_pane;

    @FXML
    StackPane stack_pane;

    @FXML
    JFXButton back;

    @FXML
    void onBackClick(ActionEvent event) {    // about screen
<<<<<<< HEAD
        Main.switchScenes("Back", "/HomePage.fxml");
    }

=======
        Main.switchScenes("Back", "/AboutPage.fxml");
    }

    @FXML
    void onHomeClick(ActionEvent event) {Main.switchScenes("Home", "/HomePage.fxml");}



>>>>>>> 979eed1225f3e3e2eccb7abfc8151db897718572





}
