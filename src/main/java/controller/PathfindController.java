package controller;

import data.Node;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import pathfind.AStarNode;
import pathfind.Map2D;
import pathfind.Pathfinder;
import pathfind.QRCode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PathfindController {

    private data.Node node1;
    private data.Node node2;

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

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        Scene home_scene = new Scene(root);
        Stage home_stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        home_stage.show();
    }

    public void doPathfinding(data.Node node1, data.Node node2) {
        this.node1 = node1;
        this.node2 = node2;

        Map2D map = new Map2D(map_img, path_polyline, destination_img);
        Pathfinder pathfinder = new Pathfinder();
        pathfinder.findShortestPath(node1.getNodeID(), node2.getNodeID());
        map.drawPath(pathfinder.path.getAStarNodePath());

        QRCode qr = new QRCode(pathfinder.path.getPathDirections());
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }
}
