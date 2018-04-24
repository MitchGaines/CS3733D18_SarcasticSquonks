package edu.wpi.cs3733d18.teamS.pathfind;

import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.animation.*;
import javafx.scene.Node;
import edu.wpi.cs3733d18.teamS.controller.PathfindController;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.codec.binary.Base64;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Path.java
 * Holds path information from AStar and calculates text directions.
 *
 * @author Noah Hillman
 * @author Mitch Gaines
 * @version %I%, %G%
 * Date: April 2, 2018
 */

public class Path {
    private static final double FEET_PER_PIXEL = .323;
    private static final int MIN_TURN_ANGLE = 30;
    private static final double NUM_ANTS_PER_SECOND = 1.8;
    public int seg_index = 0;
    ArrayList<AStarNode> algorithm_node_path = new ArrayList<>();
    public ArrayList<PathSegment> path_segments = new ArrayList<>();


    public URL getPathDirectionsURL(ArrayList<String> path_list) throws MalformedURLException {
        String host = "159.203.189.146";
        //String host = "localhost";
        int port = 3000;
        String protocol = "http";
        String path = "/";

        StringBuilder path_description = new StringBuilder();

        for (String step : path_list) {
            path_description.append(step);
        }

        byte[] path_bytes = path_description.toString().getBytes();
        String enc_bytes = Base64.encodeBase64String(path_bytes);
        path += enc_bytes;

        return new URL(protocol, host, port, path);
    }

    /**
     * getPathDirections
     * Once path is generated, will generate text descriptions for turns based on the path nodes, it also manages and
     * condenses repeated go straight instructions into a single, text instruction.
     *
     * @return URL based on calculated turns at each node.
     */
    public ArrayList<String> getPathDirections() {
        ArrayList<String> path_description = new ArrayList<>();

        path_description.add("- " + AllText.get("directions_from") + ": " + algorithm_node_path.get(0).getLongName()
                + " " + AllText.get("to") + ": " + algorithm_node_path.get(algorithm_node_path.size() - 1).getLongName() + ".");

        path_description.add("- " + AllText.get("first_step_dirs")+ " " + algorithm_node_path.get(1).short_name + ".");

        for (int i = 1; i < (algorithm_node_path.size() - 1); i++) {
            int distance = calcDistance(algorithm_node_path.get(i - 1), algorithm_node_path.get(i));

            int curr_floor = 0;
            int next_floor = 0;

            switch (algorithm_node_path.get(i - 1).floor) {
                case "L2":
                    curr_floor = -2;
                    break;
                case "L1":
                    curr_floor = -1;
                    break;
                case "G":
                    break;
                default:
                    curr_floor = Integer.parseInt(algorithm_node_path.get(i - 1).floor);
                    break;
            }

            switch (algorithm_node_path.get(i).floor) {
                case "L2":
                    next_floor = -2;
                    break;
                case "L1":
                    next_floor = -1;
                    break;
                case "G":
                    break;
                default:
                    next_floor = Integer.parseInt(algorithm_node_path.get(i).floor);
                    break;
            }

            while (i + 1 < algorithm_node_path.size() - 1
                    && calcTurnDir(algorithm_node_path.get(i), algorithm_node_path.get(i + 1)) == 'S') {
                distance += calcDistance(algorithm_node_path.get(i), algorithm_node_path.get(i + 1));
                i++;
            }

            path_description.add("- " + AllText.get("in") + " " + distance + " " + AllText.get("feet") + ", "
                    + calcTurn(algorithm_node_path.get(i), algorithm_node_path.get(i + 1)) + " at " + algorithm_node_path.get(i+1).getLongName() + ".");

            if (next_floor > curr_floor) {
                path_description.add("- " + AllText.get("stairs_up") + " " + algorithm_node_path.get(i).floor + ".");
            } else if (next_floor < curr_floor) {
                path_description.add("- " + AllText.get("stairs_down") + " " + algorithm_node_path.get(i).floor + ".");
            }
        }

        path_description.add("- " + AllText.get("destination_to") + " " + calcDistance(algorithm_node_path.get(algorithm_node_path.size() - 2),
                algorithm_node_path.get(algorithm_node_path.size() - 1)) + " " + AllText.get("feet") + ".");

        return path_description;
    }

    /**
     * calcDistance
     * Calculates and returns the distance between two node and converts it to feet.
     *
     * @param current_node The node the code is currently on.
     * @param next_node    The next node in the pathway.
     * @return The Distance between two nodes.
     */
    private int calcDistance(AStarNode current_node, AStarNode next_node) {
        return (int) (current_node.distanceTo(next_node) * FEET_PER_PIXEL);
    }

    /**
     * calcTurnDir
     * Retrieves a Char for the turning directing based on where the edu.wpi.cs3733d18.teamS.user is walking from.
     *
     * @param current_node node where the turn occurs.
     * @param next_node    node where the turn will lead.
     * @return string for the turning direction: L, R, S.
     */
    private char calcTurnDir(AStarNode current_node, AStarNode next_node) {
        int previous_x = current_node.getParent().getXCoord();
        int previous_y = current_node.getParent().getYCoord();
        int current_x = current_node.getXCoord();
        int current_y = current_node.getYCoord();
        int next_x = next_node.getXCoord();
        int next_y = next_node.getYCoord();

        double previous_angle = angle(previous_x, previous_y, current_x, current_y);
        double new_angle = angle(current_x, current_y, next_x, next_y);
        double angle = new_angle - previous_angle;

        if (angle <= -MIN_TURN_ANGLE) {
            return 'L';
        } else if (angle >= MIN_TURN_ANGLE) {
            return 'R';
        } else {
            return 'S';
        }
    }

    /**
     * calcTurn
     * Retrieves a string for the turning directing based on where the edu.wpi.cs3733d18.teamS.user is walking from.
     *
     * @param current_node node where the turn occurs.
     * @param next_node    node where the turn will lead.
     * @return string for the turning direction: turn left, turn right, continue straight.
     */
    private String calcTurn(AStarNode current_node, AStarNode next_node) {
        int previous_x = current_node.getParent().getXCoord();
        int previous_y = current_node.getParent().getYCoord();
        int current_x = current_node.getXCoord();
        int current_y = current_node.getYCoord();
        int next_x = next_node.getXCoord();
        int next_y = next_node.getYCoord();

        double previous_angle = angle(previous_x, previous_y, current_x, current_y);
        double new_angle = angle(current_x, current_y, next_x, next_y);
        double angle = new_angle - previous_angle;
        if (angle <= -MIN_TURN_ANGLE) {
            return AllText.get("turn_left");
        } else if (angle >= MIN_TURN_ANGLE) {
            return AllText.get("turn_right");
        } else {
            return AllText.get("continue_straight");
        }
    }

    /**
     * getAStarNodePath
     * Retrieves the algorithm node path.
     *
     * @return the algorithm node path.
     */
    public ArrayList<AStarNode> getAStarNodePath() {
        return algorithm_node_path;
    }

    /**
     * angle
     * Calculates the angle between two points based on there two dimensional coordinates, this takes in 4 ints that
     * represent the x and y coordinates of two points.
     *
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @return The angle as a double between the two points.
     */
    private double angle(int x1, int y1, int x2, int y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public void genPathSegments() {
        PathSegment seg = new PathSegment();
        String curr_floor = algorithm_node_path.get(0).floor;
        for (AStarNode node : algorithm_node_path) {
            if (!node.floor.equals(curr_floor)) {
                if (seg.seg_path.size() > 1) {
                    this.path_segments.add(seg);
                }
                seg = new PathSegment();
            }
            seg.seg_path.add(node);
            curr_floor = node.floor;
        }
        this.path_segments.add(seg);
    }

    public ArrayList<Node> prevSeg(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        if (seg_index > 0) {
            this.seg_index--;
        }
        Polyline p = this.path_segments.get(seg_index).genPolyline(is3D);
        fx_nodes.add(p);
        fx_nodes.addAll(genAnts(p));
        return fx_nodes;
    }

    public ArrayList<Node> nextSeg(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        if (seg_index < this.path_segments.size() - 1) {
            this.seg_index++;
        }
        Polyline p = this.path_segments.get(seg_index).genPolyline(is3D);
        fx_nodes.add(p);
        fx_nodes.addAll(genAnts(p));
        return fx_nodes;
    }

    public ArrayList<Node> thisSeg(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        Polyline p = this.path_segments.get(seg_index).genPolyline(is3D);
        fx_nodes.add(p);
        fx_nodes.addAll(genAnts(p));
        return fx_nodes;
    }

    public ArrayList<Node> genIcons(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        //adding icons for starting location
        AStarNode start_node = path_segments.get(seg_index).seg_path.get(0);
        ImageView start_icon;
        if (seg_index == 0) {
            start_icon = new ImageView("images/mapIcons/start_flag.png");
        } else {
            start_icon = new ImageView("images/mapIcons/middle_flag.png");
        }

        start_icon.setPreserveRatio(true);
        start_icon.setFitHeight(120);
        if (is3D) {
            start_icon.setX(start_node.getXCoord3D() - (start_icon.getFitHeight()/2));
            start_icon.setY(start_node.getYCoord3D() - (start_icon.getFitHeight()/2));
        } else {
            start_icon.setX(start_node.getXCoord() - (start_icon.getFitHeight()/2));
            start_icon.setY(start_node.getYCoord() - (start_icon.getFitHeight()/2));
        }
        start_icon.setId("temporaryIcon");
        start_icon.smoothProperty().setValue(true);
        start_icon.setOpacity(.8);
        fx_nodes.add(start_icon);

        StackPane start_pane = new StackPane();
        start_pane.setStyle("-fx-background-color: #f8f8f8;" +
                "-fx-border-width: 2px;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: #c8bf9b;" +
                "-fx-text-alignment: center;" +
                "-fx-fit-to-width: 20px;" +
                "-fx-padding: 5px;");
        start_pane.setId("temporaryIcon");
        Text t = new Text(start_node.getLongName());
        t.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        start_pane.getChildren().add(t);
        double height = start_pane.getBoundsInParent().getMaxY()+start_pane.getBoundsInParent().getMinY();
        double width = start_pane.getBoundsInParent().getMaxX()-start_pane.getBoundsInParent().getMinX();
        if (is3D){
            start_pane.setTranslateX(start_node.getXCoord3D() - (width / 2));
            start_pane.setTranslateY(start_node.getYCoord3D() - (height * 2));
        }else{
            start_pane.setTranslateX(start_node.getXCoord() - (width / 2) );
            start_pane.setTranslateY(start_node.getYCoord() - (height * 2));
        }
        fx_nodes.add(start_pane);

        //adding icons for end of path
        AStarNode end_node = path_segments.get(seg_index).seg_path.get(path_segments.get(seg_index).seg_path.size() - 1);
        double x_pos;
        double y_pos;
        ImageView icon;
        if (is3D) {
            x_pos = end_node.getXCoord3D();
            y_pos = end_node.getYCoord3D();
        } else {
            x_pos = end_node.getXCoord();
            y_pos = end_node.getYCoord();
        }
        if (nextFloorChange() < 0) {
            icon = new ImageView("images/mapIcons/elevator_down.png");
            icon.setPreserveRatio(true);
            icon.setFitHeight(50);
        } else if (nextFloorChange() > 0) {
            icon = new ImageView("images/mapIcons/elevator_up.png");
            icon.setPreserveRatio(true);
            icon.setFitHeight(50);
        } else {
            icon = new ImageView("images/mapIcons/goal_flag.png");
            icon.setPreserveRatio(true);
            icon.setFitHeight(120);
            icon.setOpacity(.8);
        }
        icon.setId("temporaryIcon");
        icon.smoothProperty().setValue(true);
        icon.setX(x_pos - (icon.getFitHeight()/2));
        icon.setY(y_pos - (icon.getFitHeight()/2));
        fx_nodes.add(icon);

        StackPane sp = new StackPane();
        sp.setStyle("-fx-background-color: #f8f8f8;" +
                "-fx-border-width: 2px;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: #c8bf9b;" +
                "-fx-text-alignment: center;" +
                "-fx-fit-to-width: 20px;" +
                "-fx-padding: 5px;");
        sp.setId("temporaryIcon");
        t = new Text(end_node.getLongName());
        t.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        sp.getChildren().add(t);
        height = sp.getBoundsInParent().getMaxY()+sp.getBoundsInParent().getMinY();
        width = sp.getBoundsInParent().getMaxX()-sp.getBoundsInParent().getMinX();
        sp.setTranslateX(x_pos - (width / 2));
        sp.setTranslateY(y_pos - (height * 2));

        boolean boxes_intersecting = true;
        while(boxes_intersecting){
            if(sp.getBoundsInParent().intersects(start_pane.getBoundsInParent())){
                sp.setTranslateX(sp.getTranslateX() + 15);
            }else{
                boxes_intersecting = false;
            }
        }
        fx_nodes.add(sp);

        return fx_nodes;
    }

    public int nextFloorChange() {
        if (path_segments.size() > seg_index + 1) {
            return (Map.floor_ids.indexOf(this.path_segments.get(seg_index + 1).seg_path.get(0).floor) - PathfindController.current_floor);
        }
        return 0;
    }

    ArrayList<Node> genAnts(Polyline p) {
        Ant ant = new Ant(p.getPoints().get(0), p.getPoints().get(1));
        for (int i = 2; i < p.getPoints().size(); i += 2) {
            ant.addStop(p.getPoints().get(i), p.getPoints().get(i + 1));
        }
        ArrayList<Node> nodes = new ArrayList<>();
        ant.playFromStart();
        nodes.add(ant.getImageView());

        // Clone and stagger multiple ants
        int num_staggered_ants = (int) Math.floor(ant.getDuration() * NUM_ANTS_PER_SECOND);
        for (int i = 1; i < num_staggered_ants; i++) {
            Ant staggered_ant = ant.duplicate();
            staggered_ant.playFrom(i * (ant.getDuration() / num_staggered_ants));
            nodes.add(staggered_ant.getImageView());
        }
        return nodes;
    }

    public ArrayList<PathSegment> getPathSegments() {
        return path_segments;
    }
}

class PathSegment {
    ArrayList<AStarNode> seg_path = new ArrayList<>();
    private static final double PATH_GLOW_DURATION = 2.5;

    Polyline genPolyline(boolean is3D) {
        Polyline polyline = new Polyline();
        for (AStarNode node : this.seg_path) {
            if (is3D) {
                polyline.getPoints().addAll((double) node.getXCoord3D(), (double) node.getYCoord3D());
            } else {
                polyline.getPoints().addAll((double) node.getXCoord(), (double) node.getYCoord());
            }
        }
        PathfindController.current_floor = Map.floor_ids.indexOf(seg_path.get(0).floor);
        stylizePolyline(polyline);
        return polyline;
    }


    private void stylizePolyline(Polyline p) {
        p.setId("temporaryIcon");
        p.setStyle("-fx-fill:#ff000000;" +
                "-fx-stroke:rgba(13,14,255,0.8);" +
                "-fx-stroke-line-cap:ROUND;" +
                "-fx-stroke-line-join:ROUND;" +
                "-fx-stroke-width:8.0");
        p.setEffect(generatePolylineEffect());
    }

    private Effect generatePolylineEffect() {
        Lighting polylineEffect = new Lighting();
        Shadow s = new Shadow();
        s.setHeight(11.12);
        s.setRadius(8.215);
        s.setWidth(23.74);
        polylineEffect.setLight(new Light.Distant());
        polylineEffect.setSpecularConstant(0.6);
        polylineEffect.setSpecularExponent(28);
        Timeline path_glow_anim = new Timeline(new KeyFrame(
                Duration.seconds(PATH_GLOW_DURATION / 2.0),
                new KeyValue(
                        polylineEffect.specularConstantProperty(),
                        1.6
                )
        ), new KeyFrame(
                Duration.seconds(PATH_GLOW_DURATION),
                new KeyValue(
                        polylineEffect.specularConstantProperty(),
                        0.6
                )
        ));
        path_glow_anim.setCycleCount(Timeline.INDEFINITE);
        path_glow_anim.play();
        polylineEffect.setBumpInput(s);
        return polylineEffect;
    }
}