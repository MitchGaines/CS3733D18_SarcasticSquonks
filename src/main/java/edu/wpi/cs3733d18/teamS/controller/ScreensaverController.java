package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.arduino.MotionSensor;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javax.swing.text.html.ImageView;

/**
 * Controller for the homepage.
 * @author Mitch Gaines
 * @author Orion Strickland
 * @version 1.3, April 13, 2018
 */
public class ScreensaverController {

    @FXML
    javafx.scene.image.ImageView logo;

    @FXML
    BorderPane border;


    public void initialize() {
        border.widthProperty().addListener((observable, oldValue, newValue) -> {
            logo.setFitWidth(border.getWidth()*.45);
            logo.setFitHeight(border.getWidth()*.45);
        });


        System.out.println("Initialized");


        try{

            MotionSensor.getInstance().connect();
        }catch (ArrayIndexOutOfBoundsException e) {
            //Platform.runLater(() -> Main.switchScenes("Screen Saver", "/ScreenSaver.fxml"));
            System.out.println("SOmehthigaid iaha pppenign");
        }
    }

}

