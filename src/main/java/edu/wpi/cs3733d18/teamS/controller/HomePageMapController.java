package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamS.user.InvalidUsernameException;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class HomePageMapController {

    @FXML
    JFXButton floor_0, floor_1, floor_2, floor_3, floor_4, back_button;

    @FXML
    ImageView map_img;

    @FXML
    ScrollPane map_scroll_pane;

    @FXML
    AnchorPane map_anchor_pane;

    @FXML
    StackPane stack_pane;

    @FXML
    Label time;

    @FXML
    GridPane button_pane;

    private int floor = 1;

    private Storage database;

    private edu.wpi.cs3733d18.teamS.data.Node start_node;

    public void initialize() {
        database = Storage.getInstance();
        start_node = database.getNodeByID(database.getDefaultKioskLocation());
        floor = Map.floor_ids.indexOf(start_node.getNodeFloor());
        for (Node button : button_pane.getChildren()) {
            if (floor == Integer.parseInt(button.getId().replaceAll("[^0-9]", ""))) {
                button.setStyle("-fx-background-color: #a6a6a6; -fx-font-size: 30;");
            }
        }
        map_img.setImage(getFloorImage(floor));
        fitToPos(start_node.getXCoord(), start_node.getYCoord());
        drawStartIcon();
        updatedTime();
    }

    private void fitToPos(double x, double y) {
        map_scroll_pane.setHvalue(.5);
        map_scroll_pane.setVvalue(.5);

        double pane_width = map_scroll_pane.getWidth();
        double pane_height = map_scroll_pane.getHeight();
        pane_height = 800;
        pane_width = 1200;

        double map_width = map_img.getFitWidth();
        double map_height = map_img.getFitHeight();
        map_scroll_pane.setHvalue((x - (0.5 * pane_width)) / (map_width - pane_width));
        map_scroll_pane.setVvalue((y - (0.5 * pane_height)) / (map_height - pane_height));
    }

    private static Image getFloorImage(int floor) {
        String[] images2d = {"images/2dMaps/00_thelowerlevel2.png",
                "images/2dMaps/00_thelowerlevel1.png",
                "images/2dMaps/01_thefirstfloor.png",
                "images/2dMaps/02_thesecondfloor.png",
                "images/2dMaps/03_thethirdfloor.png"};
        return new Image(images2d[floor]);
    }

    private void drawStartIcon() {
        ImageView icon;
        icon = new ImageView("images/mapIcons/start_flag.png");
        icon.setId("temporaryIcon");
        icon.setPreserveRatio(true);
        icon.setFitHeight(120);
        icon.setOpacity(.8);
        icon.smoothProperty().setValue(true);
        icon.setX(start_node.getXCoord() - (icon.getFitHeight() / 2));
        icon.setY(start_node.getYCoord() - (icon.getFitHeight() / 2));
        map_anchor_pane.getChildren().add(icon);
    }

    @FXML
    public void onFloorClick(Event e) {
        JFXButton button = (JFXButton) e.getSource();
        floor = Integer.parseInt(button.getId().replaceAll("[^0-9]", ""));
        for (Node n : button_pane.getChildren()) {
            n.setStyle("-fx-text-fill: #ffffff; -fx-background-color: #4863A0; -fx-font-size: 30;");
        }
        button.setStyle("-fx-background-color: #a6a6a6; -fx-font-size: 30;");
        map_img.setImage(getFloorImage(floor));
        if (floor == Map.floor_ids.indexOf(start_node.getNodeFloor())) {
            map_anchor_pane.getChildren().get(1).setOpacity(1);
        } else {
            map_anchor_pane.getChildren().get(1).setOpacity(.4);
        }
    }

    @FXML
    public void onMapClick(javafx.scene.input.MouseEvent e) {
        //System.out.println("mouse clicked at: " + e.getX() + ", " + e.getY());
        double distance = 999999;
        edu.wpi.cs3733d18.teamS.data.Node best_node;
        best_node = database.getNodeByID(database.getDefaultKioskLocation());
        for (edu.wpi.cs3733d18.teamS.data.Node node : database.getAllNodes()) {
            double new_dist = calcDist(node, e.getX(), e.getY());
            if (Map.floor_ids.indexOf(node.getNodeFloor()) == floor && new_dist < distance && !node.equals(best_node)) {
                best_node = node;
                distance = new_dist;
            }
        }
        System.out.println("navigating to: " + best_node.getNodeID() + " at: " + best_node.getXCoord() + ", " + best_node.getYCoord());
        SearchAlgorithm alg;
        int select = AdminSpecialOptionsController.getChoosenAlg();
        if (select == 1) {
            alg = new Dijkstras();
        } else if (select == 2) {
            alg = new DepthFirst();
        } else if (select == 3) {
            alg = new BreadthFirst();
        } else {
            alg = new AStar();
        }
        Pathfinder finder = new Pathfinder(alg);
        finder.findShortestPath(database.getDefaultKioskLocation(), best_node.getNodeID());
/*        if (finder.pathfinder_path.getAStarNodePath().size() <= 1) {
            return;
        }
        Stage stage = (Stage) map_anchor_pane.getScene().getWindow();
        stage.close();*/
        Map.path = finder.pathfinder_path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    /**
     * Updates clock to live time
     */
    public void updatedTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();

            time.setText(dtf.format(now));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    private double calcDist(edu.wpi.cs3733d18.teamS.data.Node n1, double x, double y) {
        return (Math.sqrt((Math.pow(n1.getXCoord() - x, 2)) + Math.pow(n1.getYCoord() - y, 2)));
    }

    /**
     * When the back button is clicked it switches back to the home page.
     *
     * @param event the click.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void onBackButtonClick(ActionEvent event) throws IOException {
        Main.switchScenes("Brigham and Women's", "/HomePage.fxml");
    }
}
