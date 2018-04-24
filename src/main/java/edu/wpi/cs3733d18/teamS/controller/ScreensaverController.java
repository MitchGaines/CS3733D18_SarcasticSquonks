package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.arduino.MotionSensor;

/**
 * Controller for the homepage.
 * @author Matthew McMillan
 * @author Orion Strickland
 * @version 1.3, April 13, 2018
 */
public class ScreensaverController {

    public void initialize() {
        System.out.println("Initialized");
        try{

            MotionSensor.getInstance().connect();
        }catch (ArrayIndexOutOfBoundsException e) {
            //Platform.runLater(() -> Main.switchScenes("Screen Saver", "/ScreenSaver.fxml"));
            System.out.println("SOmehthigaid iaha pppenign");
        }
    }

}

