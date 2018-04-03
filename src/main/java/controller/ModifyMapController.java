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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyMapController {

    HashMap<data.Node, ImageView> nodes_list = new HashMap<>();

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

    public ModifyMapController() {

    }

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
            Scene scene = add_loc.getScene();

            data.Node a_node = new data.Node("id", (int)click.getX(), (int)click.getY(), floor.getText(), building.getText(), loc_type.getText(), long_name.getText(), short_name.getText(), "S", 1, 1); //TODO: id function
            ImageView pin = new ImageView("images/nodeIcon.png");
            pin.setX(click.getX());
            pin.setY(click.getY());
            nodes_list.put(a_node, pin);
            pane.getChildren().addAll(pin);
            add_loc_cancel.setVisible(false);
            trash_can.setVisible(true);
            scene.setCursor(Cursor.DEFAULT);
            floor.setText("");
            building.setText("");
            loc_type.setText("");
            long_name.setText("");
            short_name.setText("");
        }
        if (delete_loc_cancel.isVisible()) {
            HashMap<data.Node, ImageView> to_delete = new HashMap<>();
            Point2D pt = new Point2D(click.getX(), click.getY());
            for (Map.Entry<data.Node, ImageView> entry : nodes_list.entrySet()) {
                if (entry.getValue().contains(pt)) {
                    pane.getChildren().remove(entry.getValue());
                    nodes_list.remove(entry); //Might mess with the for loop
                }
            }
        }
    }

    public void onTrashCanClick() {
        delete_loc_cancel.setVisible(true);
        add_loc.setVisible(false);
        floor.setVisible(false);
        building.setVisible(false);
        loc_type.setVisible(false);
        long_name.setVisible(false);
        short_name.setVisible(false);
        add_loc.setVisible(false);
    }

    public void onDeleteLocCancelClick() {
        delete_loc_cancel.setVisible(false);
        add_loc.setVisible(true);
        add_loc.setVisible(true);
        floor.setVisible(true);
        building.setVisible(true);
        loc_type.setVisible(true);
        long_name.setVisible(true);
        short_name.setVisible(true);
        add_loc.setVisible(true);
    }


}
