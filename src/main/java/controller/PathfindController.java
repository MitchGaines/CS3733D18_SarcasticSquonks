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
import javafx.stage.Stage;
import pathfind.QRCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PathfindController implements Initializable {

    @FXML
    Button back_button;

    @FXML
    ImageView qr_img;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        /*
        Perform all pathfinding operations here and then convert to
        a readable string (exactly how it will be viewed after scanning)
        to pass into QRCode creator.
         */

        QRCode qr = new QRCode("pathfind text here...");
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
