package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.SquonksAPI.controller.SquonksAPI;
import edu.wpi.cs3733d18.teamR.RaikouAPI;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamF.api.ServiceRequest;
import edu.wpi.cs3733d18.teamOapi.giftShop.GiftShop;
import edu.wpi.cs3733d18.teamp.api.Exceptions.EmployeeNotFoundException;
import edu.wpi.cs3733d18.teamp.api.TransportationRequest;
import edu.wpi.cs3733d18.teamp.api.Exceptions.ServiceException;
import edu.cmu.sphinx.api.Microphone;
import edu.wpi.cs3733d18.teamQ2.ui.Controller.RequestController2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import edu.wpi.cs3733d18.teamS.database.Storage;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

public class SpecialRequestsController {

    @FXML
    Button voice_recognition_box;

    public void onVoiceRecognitionClick() {
        ServiceRequest f_api = new ServiceRequest();
        f_api.initVoice();
        f_api.run(-1, -1, 1000, 631, null, null, null);
    }

    @FXML
    Button prescription_request;

    public void prescriptionRequestClick() {
        Timeout.stop();
        RaikouAPI raikouAPI = new RaikouAPI();
        try {
            raikouAPI.run(100, 30, 900, 600, null, null, null);
        } catch (edu.wpi.cs3733d18.teamR.ServiceException e) {
            e.printStackTrace();
        }
        Timeout.start();
    }

    @FXML
    Button sanitation_box;

    public void onSanitationRequest() {
        /*
        RequestController2 q_api = new RequestController2();

        try {
            q_api.run(100, 30, 1300, 600, null, null, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ServiceUnavailableException e) {
            e.printStackTrace();
        }
        */
    }

    @FXML
    Button gift_request;

    public void onGiftRequest() {
        GiftShop o_api = new GiftShop();
        o_api.run(100, 30, 1000, 700, null, null, null);
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
        if (finder.pathfinder_path.getAStarNodePath().size() <= 1) {
            return;
        }
        Map.path = finder.pathfinder_path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    @FXML
    Button transportation_request;

    public void onTransportationRequest() {
        Timeout.stop();
        TransportationRequest p_api = new TransportationRequest();
        try {
            p_api.run(100, 30, 1000, 700, null, null, null);
        } catch (ServiceException e) {
            //e.printStackTrace();
        }
        Timeout.start();
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
