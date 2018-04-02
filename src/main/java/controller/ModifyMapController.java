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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyMapController {

    ArrayList<ImageView> nodes = new ArrayList<>();

    Boolean add_node_enabled = false;

    @FXML
    ImageView trash_can;

    @FXML
    Label mode;

    @FXML
    AnchorPane pane;

    @FXML
    ImageView map;

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
        trash_can.setVisible(false);
        add_node_enabled = true;
        mode.setText("Add Location Mode");
        Scene scene = add_loc.getScene();
        Image loc_cursor = new Image("images/nodeIcon.png");
        scene.setCursor(new ImageCursor(loc_cursor));
        add_loc_cancel.setVisible(true);
    }

    public void onAddLocCancelClick() {
        add_node_enabled = false;
        mode.setText("");
        Scene scene = add_loc.getScene();
        scene.setCursor(Cursor.DEFAULT);
        add_loc_cancel.setVisible(false);
        trash_can.setVisible(true);
    }

    public void onMouseClick(MouseEvent click) {
        if (add_node_enabled) {
            ImageView aNode = new ImageView("images/nodeIcon.png");
            aNode.setX(click.getSceneX());
            aNode.setY(click.getSceneY());
            nodes.add(aNode);
            pane.getChildren().addAll(aNode);
        }
    }

    public void onTrashCanClick() {
        mode.setText("Delete Location Mode");
        delete_loc_cancel.setVisible(true);
        add_loc.setVisible(false);
    }

    public void onSelectNodeClick(MouseEvent click) {
        if (delete_loc_cancel.isVisible()) {
            Point2D pt = new Point2D(click.getSceneX(), click.getSceneY());
            List<ImageView> toDelete = new ArrayList<>();
            for (ImageView aNode : nodes) {
                if (aNode.contains(pt)) {
                    System.out.println("asda");
                    pane.getChildren().remove(aNode);
                    toDelete.add(aNode);
                }
            }
            toDelete.forEach(aNode -> {
                nodes.remove(aNode);
            });
        }
    }

    public void onDeleteLocCancelClick() {
        mode.setText("");
        delete_loc_cancel.setVisible(false);
        add_loc.setVisible(true);
    }



}
