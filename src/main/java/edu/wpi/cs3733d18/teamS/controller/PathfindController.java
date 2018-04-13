package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PathfindController {

    private static boolean PATHFIND_READY = false;
    @FXML
    ImageView zoom_in, zoom_out;
    @FXML
    Button back_button;
    @FXML
    AnchorPane map_anchor_pane;
    @FXML
    ScrollPane map_scroll_pane;
    @FXML
    ImageView map_img;
    @FXML
    BorderPane main_pane;
    @FXML
    Label time, floor_indicator;
    @FXML
    BorderPane header_pane, footer_pane;
    @FXML
    Button toggle_map_btn;
    @FXML
    ImageView expanded_qr;
    private double zoom_factor;
    private Storage db_storage;
    private mappingMode mode = mappingMode.MAP2D; //The pathfinder defaults to being in 2D mode.
    private int current_floor;
    private Map map;

    private static Image getFloorImage(int floor, mappingMode mode) {
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

        if (mode == mappingMode.MAP2D)
            return new Image(images2d[floor]);
        else
            return new Image(images3d[floor]);
    }

    static boolean isPathfindReady() {
        return PATHFIND_READY;
    }

    public void initialize() {
        zoom_factor = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        this.db_storage = Storage.getInstance();


        map = new Map(map_anchor_pane, current_floor, false);
        updateMap();
    }

    private void zoom(double zoom_amount) {
        zoom_factor += zoom_amount;
        if (zoom_factor > 0.5) {
            zoom_factor = 0.5;
        }
        if (zoom_factor > 1.8) {
            zoom_factor = 1.8;
        }
        map_scroll_pane.getContent().setScaleX(zoom_factor);
        map_scroll_pane.setLayoutX(zoom_factor);
        map_scroll_pane.getContent().setScaleY(zoom_factor);
        map_scroll_pane.setLayoutY(zoom_factor);

    }

    public void onZoomOut() {
        zoom(-0.2);
    }

    public void onZoomIn() {
        zoom(0.2);
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        Main.switchScenes("Brigham and Women's", "/HomePage.fxml");
    }

    void doPathfinding(String node1, String node2) {
        pathfind(node1, node2, false);
    }

    void quickLocationFinding(String start_id, String goal_id) {
        pathfind(start_id, goal_id, true);
    }

    public void pathfind(String node1, String node2, boolean is_quick) {
        PATHFIND_READY = false;

        map.clearIcons();

        map = new Map(map_anchor_pane, current_floor, (mode == mappingMode.MAP3D));

        Pathfinder pathfinder;
        int select = AdminPageController.getChoosenAlg();
        if (is_quick || select == 2) {
            pathfinder = new Pathfinder(new BreadthFirst());
        } else if (select == 1) {
            pathfinder = new Pathfinder(new DepthFirst());
        } else {
            pathfinder = new Pathfinder(new AStar());
        }

        pathfinder.findShortestPath(node1, node2);

        Path path = pathfinder.pathfinder_path;

        if (path.getAStarNodePath().size() > 1) {
            PATHFIND_READY = true;

            current_floor = Map.floor_ids.indexOf(db_storage.getNodeByID(node1).getNodeFloor());

            map.setPath(path);
            updateMap();

            try {
                String directions = path.getPathDirections().toString();
                generateQRCode(directions);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void scrollToPath() {
        if (map.getPath() == null) {
            return;
        }
        ArrayList<AStarNode> path = map.getPath().getAStarNodePath();

        double start_x;
        double start_y;
        if (mode == mappingMode.MAP2D) {
            start_x = path.get(0).getXCoord() / map_img.getImage().getWidth();
            start_y = path.get(0).getYCoord() / map_img.getImage().getHeight();
        } else {
            start_x = path.get(0).getXCoord3D() / map_img.getImage().getWidth();
            start_y = path.get(0).getYCoord3D() / map_img.getImage().getHeight();
        }

        map_scroll_pane.setHvalue(start_x);
        map_scroll_pane.setVvalue(start_y);
    }

    private void generateQRCode(String directions) {
        QRCode qr;
        qr = new QRCode(directions);
        expanded_qr.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    private void updateMap() {
        updateMap(true);
    }

    private void updateMap(boolean scroll) {
        Image m = getFloorImage(current_floor, mode);
        map_img.setImage(m);
        map_anchor_pane.setPrefSize(m.getWidth(), m.getHeight());
        floor_indicator.setText(Map.floor_ids.get(map.getFloor()));

        map.clearIcons();
        map.setFloor(current_floor);
        map.drawPath();
        if (scroll) {
            scrollToPath();
        }
        floor_indicator.setText(Map.floor_ids.get(map.getFloor()));
    }

    public void toggleMappingType() {
        boolean is_3D = false;
        if (mode == mappingMode.MAP3D) {
            toggle_map_btn.setText(AllText.get("2d_map"));
            mode = mappingMode.MAP2D;
        } else if (mode == mappingMode.MAP2D) {
            toggle_map_btn.setText(AllText.get("3d_map"));
            mode = mappingMode.MAP3D;
            is_3D = true;
        }

        Path path = null;
        if (map != null) {
            map.clearIcons();
            path = map.getPath();
        }

        map = new Map(map_anchor_pane, current_floor, is_3D);

        if (path != null) {
            map.setPath(path);
        }

        updateMap();
    }

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

    public void onBigQRClick() {
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
        if (current_floor < 4) {
            current_floor++;
            updateMap(false);
            floor_indicator.setText(Map.floor_ids.get(map.getFloor()));
        }
    }

    public void onMapDown() {
        if (current_floor > 0) {
            current_floor--;
            updateMap(false);
            floor_indicator.setText(Map.floor_ids.get(map.getFloor()));
        }
    }

    private enum mappingMode {MAP3D, MAP2D}
}
