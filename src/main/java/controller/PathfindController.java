package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import pathfind.QRCode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PathfindController implements Initializable {

    private final int MAP_WIDTH = 5000;
    private final int MAP_HEIGHT= 3400;

    @FXML
    Button back_button;

    @FXML
    ImageView qr_img;
    @FXML
    ImageView map_img;

    @FXML
    Polyline path_polyline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<data.Node> path = new ArrayList<>();

        // Test path
        //path.add(new data.Node("",0,0,"", "", "", "", "", "", 0, 0));
        //path.add(new data.Node("",1000,0,"", "", "", "", "", "", 0, 0));
        //path.add(new data.Node("",1000,2000,"", "", "", "", "", "", 0, 0));
        //path.add(new data.Node("",5000,3400,"", "", "", "", "", "", 0, 0));

        /* TODO
        Perform all pathfinding operations here and then convert to
        a readable string (exactly how it will be viewed after scanning)
        to pass into QRCode creator.
         */

        Bounds img_bounds = map_img.getBoundsInParent();
        path_polyline.getPoints().clear();
        for (data.Node node : path) {
            path_polyline.getPoints().addAll(((double)node.getXCoord() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX(),
                    ((double)node.getYCoord() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY());
        }

        QRCode qr = new QRCode("pathfind text here...");
        qr_img.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        Scene home_scene = new Scene(root);
        Stage home_stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");
        home_stage.setScene(home_scene);
        home_stage.show();
    }
}
