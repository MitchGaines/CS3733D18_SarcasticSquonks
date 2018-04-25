package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.SquonksAPI.controller.SquonksAPI;
import edu.wpi.cs3733d18.teamR.RaikouAPI;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamF.api.ServiceRequest;
import edu.wpi.cs3733d18.teamOapi.giftShop.GiftShop;
import edu.wpi.cs3733d18.teamp.api.TransportationRequest;
import edu.wpi.cs3733d18.teamp.api.Exceptions.ServiceException;
import com.nearnagas.api.Security;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The controller for the special requests page
 * @author Joseph Turcotte
 * @author Danny Sullivan
 * @version 1.3, April 23, 2018
 */

public class SpecialRequestsController {

    @FXML
    Button voice_recognition_box;

    public void onVoiceRecognitionClick() {
        Timeout.stop();
        ServiceRequest f_api = new ServiceRequest();
        //f_api.initVoice();
        f_api.run(-1, -1, 1000, 631, null, null, null);
        Timeout.start();
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
    Button gift_request;

    public void onGiftRequest() {
        Timeout.stop();
        GiftShop o_api = new GiftShop();
        o_api.run(100, 30, 1000, 700, null, null, null);
        Timeout.start();
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
            e.printStackTrace();
        }
        Timeout.start();
    }

    @FXML
    Button security_request;

    public void onSecurityRequest() {
        Timeout.stop();
        Security n_api = new Security();
        try {
            n_api.run(100, 30, 1280, 720, null, null, null);
        } catch (com.nearnagas.api.ServiceException e) {
            e.printStackTrace();
        }
        Timeout.start();
    }

    @FXML
    private Button nurse_btn;

    public void onNurseClick() {
        Main.switchScenes("Nurse Home", "/NurseHome.fxml");
    }
}
