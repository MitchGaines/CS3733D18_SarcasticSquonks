package controller;

import internationalization.AllText;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private enum mappingMode{MAP3D, MAP2D}
    private mappingMode mode = mappingMode.MAP2D; //The pathfinder defaults to being in 2D mode.

    @FXML
    Button back_button;

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

    public void doPathfinding(data.Node node1, data.Node node2) {
        this.node1 = node1;
        this.node2 = node2;

        Map map;
        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, destination_img);
        else
            map = new Map3D(map_img, path_polyline, destination_img);

        Pathfinder pathfinder = new Pathfinder();
        pathfinder.findShortestPath(node1.getNodeID(), node2.getNodeID());
        map.drawPath(pathfinder.path.getAStarNodePath());

        QRCode qr = null;
        try {
            qr = new QRCode(pathfinder.path.getPathDirections().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void enable3DMapping(){
        mode = mappingMode.MAP3D;
        Image m = new Image("images/2-ICONS.png");
        map_img.setImage(m);

        if(node1 != null && node2 != null){
            doPathfinding(node1, node2);
        }
    }

    public void enable2DMapping(){
        mode = mappingMode.MAP2D;
        Image m = new Image("images/02_thesecondfloor.png");
        map_img.setImage(m);

        if(node1 != null && node2 != null){
            doPathfinding(node1, node2);
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
