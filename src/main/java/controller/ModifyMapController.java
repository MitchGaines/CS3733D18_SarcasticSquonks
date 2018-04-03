package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.ImageCursor;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

public class ModifyMapController {

    List<data.Node> nodes = new ArrayList<>();
    List<ImageView> pins = new ArrayList<>();

    @FXML
    Label add_loc_fail;

    @FXML
    ImageView trash_can;

    @FXML
    TextField floor;

    @FXML
    TextField building;

    @FXML
    TextField loc_type;

    @FXML
    TextField long_name;

    @FXML
    TextField short_name;

    @FXML
    AnchorPane pane;

    @FXML
    Button add_loc_cancel;

    @FXML
    Button delete_loc_cancel;

    @FXML
    Button add_loc;

    @FXML
    Button back_btn;

    public void onBackClick(ActionEvent event) throws IOException {
        Parent admin_parent = FXMLLoader.load(getClass().getResource("/AdminPage.fxml"));
        Scene admin_scene = new Scene(admin_parent);
        Stage admin_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        admin_stage.setTitle("User");
        admin_stage.setScene(admin_scene);
        admin_stage.show();
    }

    public void onAddLocClick() {
        if (!floor.getText().equals("") && !building.getText().equals("") && !loc_type.getText().equals("") && !long_name.getText().equals("") && !short_name.getText().equals("")) {
            trash_can.setVisible(false);
            Scene scene = add_loc.getScene();
            Image loc_cursor = new Image("images/nodeIcon.png");
            scene.setCursor(new ImageCursor(loc_cursor));
            add_loc_cancel.setVisible(true);
            add_loc_fail.setText("");
        }
        else {
            add_loc_fail.setText("Please fill all fields");
        }
    }

    public void onAddLocCancelClick() {
        Scene scene = add_loc.getScene();
        scene.setCursor(Cursor.DEFAULT);
        add_loc_cancel.setVisible(false);
        trash_can.setVisible(true);
    }

    public void onMouseClick(MouseEvent click) {
        if (add_loc_cancel.isVisible()) {
            data.Node a_node = new data.Node("id", (int)click.getX(), (int)click.getY(), floor.getText(), building.getText(), loc_type.getText(), long_name.getText(), short_name.getText(), "S", 1, 1); //TODO: id function
            ImageView pin = new ImageView("images/nodeIcon.png");
            pin.setX(click.getX());
            pin.setY(click.getY());
            nodes.add(a_node);
            pins.add(pin);
            pane.getChildren().addAll(pin);
        }
        if (delete_loc_cancel.isVisible()) {
            List<ImageView> toDelete = new ArrayList<>();
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (ImageView pin : pins) {
                if (pin.contains(pt)) {
                    pane.getChildren().remove(pin);
                    toDelete.add(pin);
                }
            }
            toDelete.forEach(aNode -> {
                nodes.remove(aNode);
            });
        }
    }

    public void onTrashCanClick() {
        delete_loc_cancel.setVisible(true);
        add_loc.setVisible(false);
    }

    public void onDeleteLocCancelClick() {
        delete_loc_cancel.setVisible(false);
        add_loc.setVisible(true);
    }


}
