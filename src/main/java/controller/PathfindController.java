package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class PathfindController implements Initializable {

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

    /**
     * Performs this function during creation of Controller; everything done here
     * is the logic that should go into manipulating what is shown
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map2D map = new Map2D(map_img, path_polyline, destination_img);
        Pathfinder pathfinder = new Pathfinder();
        //pathfinder.findShortestPath();
        //map.drawPath(pathfinder.path.getAStarNodePath());

        // Test path
        /*
        ArrayList<AStarNode> test_path = new ArrayList<>();
        test_path.add(new AStarNode("", 2623, 1018, ""));
        test_path.add(new AStarNode("", 2610, 953, ""));
        test_path.add(new AStarNode("", 2605, 902, ""));
        test_path.add(new AStarNode("", 2605, 841, ""));
        test_path.add(new AStarNode("", 2605, 783, ""));
        test_path.add(new AStarNode("", 2554, 784, ""));
        map.drawPath(test_path);
        */

        QRCode qr = new QRCode(/*pathfinder.path.getPathDirections()*/"Pathfinding directions here");
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        Scene home_scene = new Scene(root);
        Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        home_stage.show();
    }
}
