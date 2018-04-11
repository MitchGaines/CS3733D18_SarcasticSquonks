package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.stage.Window;
import edu.wpi.cs3733d18.teamS.pathfind.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.net.MalformedURLException;

public class PathfindController {

    private edu.wpi.cs3733d18.teamS.data.Node node1;
    private edu.wpi.cs3733d18.teamS.data.Node node2;
    private Storage db_storage;

    private enum mappingMode{MAP3D, MAP2D}
    private mappingMode mode = mappingMode.MAP2D; //The pathfinder defaults to being in 2D mode.

    private int current_floor;
    private Map map;


    double zoom_factor;

    @FXML
    ImageView zoom_in, zoom_out;

    private static boolean PATHFIND_READY = false;

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
    Label time, floor_indicator;

    public void initialize() {
        zoom_factor = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        this.db_storage = Storage.getInstance();
        map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);
        enable2DMapping();
    }

    public void onZoomOut() {
        if(zoom_factor > 0.5) {
            zoom_factor -= 0.2;
            map_scroll_pane.getContent().setScaleX(zoom_factor);
            map_scroll_pane.setLayoutX(zoom_factor);
            map_scroll_pane.getContent().setScaleY(zoom_factor);
            map_scroll_pane.setLayoutY(zoom_factor);
        }
    }

    public void onZoomIn()  {
        if (zoom_factor < 1.8) {
            zoom_factor += 0.2;
            map_scroll_pane.getContent().setScaleX(zoom_factor);
            map_scroll_pane.setLayoutX(zoom_factor);
            map_scroll_pane.getContent().setScaleY(zoom_factor);
            map_scroll_pane.setLayoutY(zoom_factor);
        }
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Window window = main_pane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        Scene home_scene = new Scene(root, window.getWidth(), window.getHeight());
        Stage home_stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        home_stage.setTitle("Brigham and Women's");

        Timeout.addListenersToScene(home_scene);

        home_stage.setScene(home_scene);
        home_stage.show();
    }

    public void doPathfinding(String node1, String node2) {
        PATHFIND_READY = false;
        this.node1 = db_storage.getNodeByID(node1);
        this.node2 = db_storage.getNodeByID(node2);
        map.clearIcons();

        for(int i = 0; i < Map.floor_ids.size(); i++){
            if(Map.floor_ids.get(i).equals(this.node1.getNodeFloor()))
                current_floor = i;
        }
        map.setFloor(current_floor);
        updateMap();

        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);
        else
            map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);

        Pathfinder pathfinder;
        int select = AdminPageController.getChoosenAlg();
        switch(select){
            case 0 :
                pathfinder = new Pathfinder(new AStar());
                break;
            case 1 :
                pathfinder = new Pathfinder(new DepthFirst());
                break;
            case 2 :
                pathfinder = new Pathfinder(new BreadthFirst());
                break;
            default:
                pathfinder = new Pathfinder(new AStar());
                break;
        }

        pathfinder.findShortestPath(this.node1.getNodeID(), this.node2.getNodeID());
        map.drawPath(pathfinder.pathfinder_path.getAStarNodePath());

        if(pathfinder.pathfinder_path.getAStarNodePath().size()>1){
            double start_x = pathfinder.pathfinder_path.getAStarNodePath().get(0).getXCoord() / map_img.getImage().getWidth();
            double start_y = pathfinder.pathfinder_path.getAStarNodePath().get(0).getYCoord() / map_img.getImage().getHeight();
            map_scroll_pane.setHvalue(start_x);
            map_scroll_pane.setVvalue(start_y);

          map.drawPath(pathfinder.pathfinder_path.getAStarNodePath());
            QRCode qr = null;
            try {
                qr = new QRCode(pathfinder.pathfinder_path.getPathDirections().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            expanded_qr.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
            PATHFIND_READY = true;
        }
    }

    public void quickLocationFinding(String start_id, String goal_id){
        map.clearIcons();
        edu.wpi.cs3733d18.teamS.data.Node node1 = db_storage.getNodeByID(start_id);
        for(int i = 0; i < Map.floor_ids.size(); i++){
            if(Map.floor_ids.get(i).equals(node1.getNodeFloor()))
                current_floor = i;
        }
        map.setFloor(current_floor);
        updateMap();

        if(mode == mappingMode.MAP2D)
            map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);
        else
            map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);

        Pathfinder quickFinder = new Pathfinder(new BreadthFirst());
        quickFinder.findShortestPath(start_id, goal_id);
        map.drawPath(quickFinder.pathfinder_path.getAStarNodePath());

        this.node1 = db_storage.getNodeByID(quickFinder.pathfinder_path.getAStarNodePath().get(0).getID());
        this.node2 = db_storage.getNodeByID(quickFinder.pathfinder_path.getAStarNodePath().get(quickFinder.pathfinder_path.getAStarNodePath().size()-1).getID());

        double start_x = quickFinder.pathfinder_path.getAStarNodePath().get(0).getXCoord() / map_img.getImage().getWidth();
        double start_y = quickFinder.pathfinder_path.getAStarNodePath().get(0).getYCoord() / map_img.getImage().getHeight();
        map_scroll_pane.setHvalue(start_x);
        map_scroll_pane.setVvalue(start_y);

        // Building QR code and placing in scene
        QRCode qr = null;
        try {
            qr = new QRCode(quickFinder.pathfinder_path.getPathDirections().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        expanded_qr.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    public void updateMap(){
        Image m = getFloorImage(current_floor, mode);
        map_img.setImage(m);
        map_anchor_pane.setPrefSize(m.getWidth(), m.getHeight());
        floor_indicator.setText(map.floor_ids.get(map.getFloor()));
    }

    public void enable3DMapping(){
        map.clearIcons();
        mode = mappingMode.MAP3D;
        ArrayList<AStarNode> path = map.getPath();

        if(node1 != null && node2 != null){
            double start_x = path.get(0).getXCoord() / map_img.getImage().getWidth();
            double start_y = path.get(0).getYCoord() / map_img.getImage().getHeight();
            map_scroll_pane.setHvalue(start_x);
            map_scroll_pane.setVvalue(start_y);
        }

        map = new Map3D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);
        map.setPath(path);
        updateMap();
        if(node1 != null && node2 != null) {
            map.drawPath();
        }
    }

    public void enable2DMapping(){
        map.clearIcons();
        mode = mappingMode.MAP2D;
        ArrayList<AStarNode> path = map.getPath();

        if(node1 != null && node2 != null){
            double start_x = path.get(0).getXCoord() / map_img.getImage().getWidth();
            double start_y = path.get(0).getYCoord() / map_img.getImage().getHeight();
            map_scroll_pane.setHvalue(start_x);
            map_scroll_pane.setVvalue(start_y);
        }

        map = new Map2D(map_img, path_polyline, path_polyline_2, destination_img, map_scroll_pane, map_anchor_pane, current_floor);
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
        a_parent.getChildrenUnmodifiable().get(1).setEffect(adj);
        a_parent.getChildrenUnmodifiable().get(2).setEffect(adj);
        expanded_qr.isPreserveRatio();
        expanded_qr.setVisible(true);
    }

    @FXML
    BorderPane header_pane, footer_pane;

    public void onBigQRClick(){
        expanded_qr.setVisible(false);
        ObservableList<Node> list = main_pane.getChildren();
        for (Node a_node : list) {
            a_node.setStyle("-fx-background-color: null");
            a_node.setEffect(null);
        }
        header_pane.setStyle("-fx-background-color: #4863A0");
        footer_pane.setStyle("-fx-background-color: #4863A0");
        Parent a_parent = expanded_qr.getParent();
        a_parent.setEffect(null);
        a_parent.getChildrenUnmodifiable().get(0).setEffect(null);
        a_parent.getChildrenUnmodifiable().get(1).setEffect(null);
        a_parent.getChildrenUnmodifiable().get(2).setEffect(null);
    }

    public void onMapUp() {
        if(current_floor < 4){
            map.clearIcons();
            current_floor++;
            map.setFloor(current_floor);
            map.drawPath();
            updateMap();
            floor_indicator.setText(map.floor_ids.get(map.getFloor()));
        }
    }

    public void onMapDown() {
        if(current_floor > 0){
            map.clearIcons();
            current_floor--;
            map.setFloor(current_floor);
            map.drawPath();
            updateMap();
            floor_indicator.setText(map.floor_ids.get(map.getFloor()));
        }
    }

    private static Image getFloorImage(int floor, mappingMode mode){
        String[] images2d = {"images/2dMaps/00_thelowerlevel2.png",
                "images/2dMaps/00_thelowerlevel1.png",
                "images/2dMaps/01_thefirstfloor.png",
                "images/2dMaps/02_thesecondfloor.png",
                "images/2dMaps/03_thethirdfloor.png"};

        String[] images3d = {"images/3dMaps/L2-NO-ICONS.png",
                "images/3dMaps/L1-NO-ICONS.png",
                "images/3dMaps/1-NO-ICONS.png",
                "images/3dMaps/2-NO-ICONS.png",
                "images/3dMaps/3-NO-ICONS.png"};

        if(mode == mappingMode.MAP2D)
            return new Image(images2d[floor]);
        else
            return new Image(images3d[floor]);
    }

    public static boolean isPathfindReady(){
        return PATHFIND_READY;
    }
}
