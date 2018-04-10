package controller;

import database.Storage;
import internationalization.AllText;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    private int current_floor;
    private Map map;

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
    Polyline path_polyline_2;

    @FXML
    BorderPane main_pane;

    @FXML
    Label time;

    public void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        this.db_storage = Storage.getInstance();
        map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);
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
        Pathfinder pathfinder = new Pathfinder(new AStar());
        pathfinder.findShortestPath(this.node1.getNodeID(), this.node2.getNodeID());

        for(int i = 0; i < Map.floor_ids.length; i++){
            if(Map.floor_ids[i].equals(this.node1.getNodeFloor()))
                current_floor = i;
        }
        map.setFloor(current_floor);
        updateMap();

        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);
        else
            map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);

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
        data.Node node1 = db_storage.getNodeByID(start_id);
        for(int i = 0; i < Map.floor_ids.length; i++){
            if(Map.floor_ids[i].equals(node1.getNodeFloor()))
                current_floor = i;
        }
        map.setFloor(current_floor);
        updateMap();

        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);
        else
            map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);

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

    public void updateMap(){
        Image m = getFloorImage(current_floor, mode);
        map_img.setImage(m);
        map_anchor_pane.setPrefSize(m.getWidth(), m.getHeight());
    }

    public void enable3DMapping(){
        mode = mappingMode.MAP3D;
        ArrayList<AStarNode> path = map.getPath();
        map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);
        map.setPath(path);
        updateMap();
        if(node1 != null && node2 != null)
            map.drawPath();
    }

    public void enable2DMapping(){
        mode = mappingMode.MAP2D;
        ArrayList<AStarNode> path = map.getPath();
        map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, current_floor);
        map.setPath(path);
        updateMap();
        if(node1 != null && node2 != null)
            map.drawPath();
    }

    @FXML
    Button toggle_map_btn;

    public void toggleMappingType(){
        if(mode == mappingMode.MAP2D){
            enable3DMapping();
            toggle_map_btn.setText("2D Map"); //TODO language stuff
        }
        else if(mode == mappingMode.MAP3D) {
            enable2DMapping();
            toggle_map_btn.setText("3D Map"); //TODO language stuff
        }
    }

    @FXML
    ImageView expanded_qr;

    public void onQRClick() {
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        ObservableList<Node> list = main_pane.getChildren();
        for (Node a_node : list) {
            a_node.setEffect(adj);
        }
        Parent a_parent = expanded_qr.getParent();
        a_parent.setEffect(null);
        a_parent.getChildrenUnmodifiable().get(0).setEffect(adj);
        expanded_qr.setImage(qr_img.getImage());
        expanded_qr.isPreserveRatio();
        expanded_qr.setVisible(true);
    }

    @FXML
    BorderPane header_pane;

    public void onBigQRClick(){
        expanded_qr.setVisible(false);
        ObservableList<Node> list = main_pane.getChildren();
        for (Node a_node : list) {
            a_node.setStyle("-fx-background-color: null");
            a_node.setEffect(null);
        }
        header_pane.setStyle("-fx-background-color:  #4863A0");
        Parent a_parent = expanded_qr.getParent();
        a_parent.setEffect(null);
        a_parent.getChildrenUnmodifiable().get(0).setEffect(null);
    }

    public void onMapUp() {
        if(current_floor < 4){
            current_floor++;
            map.setFloor(current_floor);
            map.drawPath();
            updateMap();
        }
    }

    public void onMapDown() {
        if(current_floor > 0){
            current_floor--;
            map.setFloor(current_floor);
            map.drawPath();
            updateMap();
        }
    }

    private static Image getFloorImage(int floor, mappingMode mode){
        String[] images2d = {"images/00_thelowerlevel2.png",
                "images/00_thelowerlevel1.png",
                "images/01_thefirstfloor.png",
                "images/02_thesecondfloor.png",
                "images/03_thethirdfloor.png"};
        String[] images3d = {"images/L2-NO-ICONS.png",
                "images/L1-NO-ICONS.png",
                "images/1-NO-ICONS.png",
                "images/2-NO-ICONS.png",
                "images/3-NO-ICONS.png"};
        if(mode == mappingMode.MAP2D)
            return new Image(images2d[floor]);
        else
            return new Image(images3d[floor]);
    }

}
