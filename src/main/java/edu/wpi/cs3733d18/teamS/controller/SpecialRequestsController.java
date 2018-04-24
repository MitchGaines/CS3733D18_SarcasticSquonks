package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.SquonksAPI.controller.SquonksAPI;
import edu.wpi.cs3733d18.teamR.RaikouAPI;
import edu.wpi.cs3733d18.teamR.ServiceException;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SpecialRequestsController {

    @FXML
    Button voice_recognition_box;

    public void onVoiceRecognitionClick() {
        // TODO Danny
    }

    @FXML
    Button prescription_request;

    public void prescriptionRequestClick() {
        Timeout.stop();
        RaikouAPI raikouAPI = new RaikouAPI();
        try {
            raikouAPI.run(100, 30, 900, 600, null, null, null);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        Timeout.start();
    }

    @FXML
    Button sanitation_box;

    public void onSanitationRequest() {
        // TODO Danny
    }

    @FXML
    Button gift_request;

    public void onGiftRequest() {
        // TODO Danny
    }

    @FXML
    Button it_request;

    public void onITRequest() {
        Timeout.stop();
        SquonksAPI squonks_api = new SquonksAPI();
        squonks_api.run(e -> onPathFindClick(e));
        Timeout.start();
    }

    public void onPathFindClick(String node_id) {
        SearchAlgorithm alg;
        int select = AdminSpecialOptionsController.getChoosenAlg();
        if (select == 1) {
            alg = new Dijkstras();
        } else if (select == 2) {
            alg = new DepthFirst();
        } else if (select == 3) {
            alg = new BreadthFirst();
        } else {
            alg = new AStar();
        }
        Pathfinder finder = new Pathfinder(alg);
        finder.findShortestPath(Storage.getInstance().getDefaultKioskLocation(), node_id);
        if(finder.pathfinder_path.getAStarNodePath().size() <= 1){
            return;
        }
        Map.path = finder.pathfinder_path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    @FXML
    Button transportation_request;

    public void onTransportationRequest() {
        // TODO Danny
    }

    @FXML
    Button food_request;

    public void onFoodRequest() {
        // TODO Danny
    }

    @FXML
    Button room_service;

    public void onRoomServiceRequest() {
        // TODO Danny
    }

    @FXML
    Button security_request;

    public void onSecurityRequest() {
        // TODO Danny
    }
}
