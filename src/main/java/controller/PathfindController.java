package controller;

import database.Storage;
import internationalization.AllText;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.stage.Window;
import pathfind.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class PathfindController {

    private data.Node node1;
    private data.Node node2;
    private Storage db_storage;

    private enum mappingMode{MAP3D, MAP2D}
    private mappingMode mode = mappingMode.MAP2D; //The pathfinder defaults to being in 2D mode.

    @FXML
    Button back_button;

    @FXML
    AnchorPane map_anchor_pane;

    @FXML
    ScrollPane map_scroll_pane;

    @FXML
    ImageView qr_img;
    @FXML
    ImageView map_img;
    @FXML
    ImageView destination_img;

    @FXML
    Polyline path_polyline;

    @FXML
    BorderPane main_pane;

    @FXML
    Label time;

    public void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        this.db_storage = Storage.getInstance();
        enable2DMapping();
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        Scene home_scene = new Scene(root, window.getWidth(), window.getHeight());
        Stage home_stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        home_stage.show();
    }

    public void doPathfinding(String node1, String node2) {
        this.node1 = db_storage.getNodeByID(node1);
        this.node2 = db_storage.getNodeByID(node2);

        Map map;
        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, destination_img, map_scroll_pane);
        else
            map = new Map3D(map_img, path_polyline, destination_img, map_scroll_pane);

        Pathfinder pathfinder = new Pathfinder(new AStar());
        pathfinder.findShortestPath(this.node1.getNodeID(), this.node2.getNodeID());
        map.drawPath(pathfinder.pathfinder_path.getAStarNodePath());

        QRCode qr = null;
        try {
            qr = new QRCode(pathfinder.pathfinder_path.getPathDirections().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void quickLocationFinding(String start_id, String goal_id){
        Map map;
        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, destination_img, map_scroll_pane);
        else
            map = new Map3D(map_img, path_polyline, destination_img, map_scroll_pane);
        Pathfinder quickFinder = new Pathfinder(new BreadthFirst());
        quickFinder.findShortestPath(start_id, goal_id);
        map.drawPath(quickFinder.pathfinder_path.getAStarNodePath());
        this.node1 = db_storage.getNodeByID(quickFinder.pathfinder_path.getAStarNodePath().get(0).getID());
        this.node2 = db_storage.getNodeByID(quickFinder.pathfinder_path.getAStarNodePath().get(quickFinder.pathfinder_path.getAStarNodePath().size()-1).getID());

        // Building QR code and placing in scene
        QRCode qr = null;
        try {
            qr = new QRCode(quickFinder.pathfinder_path.getPathDirections().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void enable3DMapping(){
        mode = mappingMode.MAP3D;
        Image m = new Image("images/2-ICONS.png");
        map_img.setImage(m);
        map_anchor_pane.setPrefSize(m.getWidth(), m.getHeight());

        if(node1 != null && node2 != null){
            doPathfinding(node1.getNodeID(), node2.getNodeID());
        }
    }

    public void enable2DMapping(){
        mode = mappingMode.MAP2D;
        Image m = new Image("images/02_thesecondfloor.png");
        map_img.setImage(m);
        map_anchor_pane.setPrefSize(m.getWidth(), m.getHeight());

        if(node1 != null && node2 != null){
            doPathfinding(node1.getNodeID(), node2.getNodeID());
        }
    }

    public void toggleMappingType(){
        if(mode == mappingMode.MAP2D){
            enable3DMapping();
        }
        else if(mode == mappingMode.MAP3D) {
            enable2DMapping();
        }
    }

}
