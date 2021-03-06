package edu.wpi.cs3733d18.teamS.pathfind;

import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Node;
import edu.wpi.cs3733d18.teamS.controller.PathfindController;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.apache.commons.codec.binary.Base64;
import javafx.scene.image.ImageView;

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
    public static int seg_index = 0;
    private static final double PATH_DRAW_SPEED = 250;
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

        path_description.add("- " + AllText.get("first_step_dirs") + " " + algorithm_node_path.get(1).short_name + ".");

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
                    + calcTurn(algorithm_node_path.get(i), algorithm_node_path.get(i + 1)) + " at " + algorithm_node_path.get(i + 1).getLongName() + ".");

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

    public Polyline getFullPathPolyline(boolean is_3D) {
        Polyline polyline = new Polyline();
        for (AStarNode node : algorithm_node_path) {
            if (is_3D) {
                polyline.getPoints().addAll((double) node.getXCoord3D(), (double) node.getYCoord3D());
            } else {
                polyline.getPoints().addAll((double) node.getXCoord(), (double) node.getYCoord());
            }
        }
        polyline.setId("full_path");
        polyline.setStyle("-fx-fill:#ff000000;" +
                "-fx-stroke:rgba(74,74,74,0.31);" +
                "-fx-stroke-line-cap:ROUND;" +
                "-fx-stroke-line-join:ROUND;" +
                "-fx-stroke-width:8.0");
        polyline.toBack();
        return polyline;
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
        seg_index = 0;
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
        playDrawingAnimation(p, is3D);
        fx_nodes.add(p);
        fx_nodes.add(getFullPathPolyline(is3D));
        fx_nodes.addAll(genAnts(p));
        return fx_nodes;
    }

    public ArrayList<Node> nextSeg(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        if (seg_index < this.path_segments.size() - 1) {
            this.seg_index++;
        }
        Polyline p = this.path_segments.get(seg_index).genPolyline(is3D);
        playDrawingAnimation(p, is3D);
        fx_nodes.add(p);
        fx_nodes.add(getFullPathPolyline(is3D));
        fx_nodes.addAll(genAnts(p));
        return fx_nodes;
    }

    public ArrayList<Node> thisSeg(boolean is3D) {
        ArrayList<Node> fx_nodes = new ArrayList<>();
        Polyline p = this.path_segments.get(seg_index).genPolyline(is3D);
        playDrawingAnimation(p, is3D);
        fx_nodes.add(p);
        fx_nodes.add(getFullPathPolyline(is3D));
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
        start_icon.toFront();
        start_icon.setPreserveRatio(true);
        start_icon.setFitHeight(120);
        if (is3D) {
            start_icon.setX(start_node.getXCoord3D() - (start_icon.getFitHeight() / 2));
            start_icon.setY(start_node.getYCoord3D() - (start_icon.getFitHeight() / 2));
        } else {
            start_icon.setX(start_node.getXCoord() - (start_icon.getFitHeight() / 2));
            start_icon.setY(start_node.getYCoord() - (start_icon.getFitHeight() / 2));
        }
        start_icon.setId("prev_icon");
        start_icon.smoothProperty().setValue(true);
        start_icon.setOpacity(.8);
        start_icon.setUserData(start_node);

        fx_nodes.add(start_icon);

        addTooltip(start_icon);

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
        icon.toFront();
        icon.setId("next_icon");
        icon.smoothProperty().setValue(true);
        icon.setX(x_pos - (icon.getFitHeight() / 2));
        icon.setY(y_pos - (icon.getFitHeight() / 2));
        icon.setUserData(end_node);
        fx_nodes.add(icon);

        addTooltip(icon);
        return fx_nodes;
    }

    public ArrayList<Node> generateBreadcrumbs(){
        ArrayList<Node> n = new ArrayList<>();
        String prevFloor = "";

        for(int i = 0; i < path_segments.size(); i++){

            Text t;
            if (i > 0){
                t = new Text("\n→");
                t.styleProperty().set("-fx-text-fill: #ffffff; -fx-font-size: 25");
                t.setFont(new Font(50));
                t.setFill(Color.WHITE);
                t.setTextAlignment(TextAlignment.CENTER);
                n.add(t);
            }

            ImageView iv = new ImageView();
            if(i != 0 && i != path_segments.size()-1){
//                iv.setUserData(prevFloor+"\nto\nFloor " + path_segments.get(i).seg_path.get(0).floor);
                iv.setUserData("Floor " + path_segments.get(i).seg_path.get(0).floor + i);
            } else {
                iv.setUserData("Floor " + path_segments.get(i).seg_path.get(0).floor + i);
            }

            if (i == 0){
                iv.setImage(new Image("images/mapIcons/start_flag_small.png"));
            } else if (i == path_segments.size() - 1) {
                iv.setImage(new Image("images/mapIcons/goal_flag_small.png"));
            } else {
                iv.setImage(new Image("images/mapIcons/middle_flag_small.png"));
            }


            n.add(iv);

            prevFloor = "Floor " + path_segments.get(i).seg_path.get(0).floor;

        }

        for(Node node : n){
            try{
                ImageView iv = (ImageView) node;
                iv.setId("flag");
                iv.setPreserveRatio(true);
                iv.setX(iv.getX() - 100 - (iv.getFitHeight()/2));
                iv.setY(iv.getY() - 100 - (iv.getFitHeight()/2));

                iv.setFitHeight(50);
                iv.setOpacity(.8);
                iv.smoothProperty().setValue(true);

            } catch (ClassCastException e){
                node.setId("text");
            }

//<!--<JFXButton fx:id="floor_0" alignment="CENTER" buttonType="RAISED" opacity="0.9" prefHeight="60.0" prefWidth="80.0"
// style="-fx-text-fill: #ffffff; -fx-background-color: #4863A0; -fx-font-size: 30;" text="L2" textAlignment="CENTER"
// GridPane.halignment="CENTER" GridPane.columnIndex="4" GridPane.valignment="CENTER" />-->
        }
        return n;
    }

    /**
     * addTooltip
     * Adds a tooltip to the supplied node, adding navigation instructions to the tooltip.
     *
     * @param n The node to bind the tooltip to
     * @return The angle as a double between the two points.
     * @author Matt Puentes
     */
    private void addTooltip(Node n) {
        String tooltip_text = "";
        if (n.getId() == "prev_icon" || n.getId() == "next_icon") {
            tooltip_text += ((AStarNode) n.getUserData()).getLongName();
            String id;
            int floor;
            if (n.getId() == "prev_icon") {
                id = prevFloorId();
                floor = prevFloorChange();
            } else {
                id = nextFloorId();
                floor = nextFloorChange();
            }
            if (floor != 0) {
                tooltip_text += "\n";
                tooltip_text += (floor > 0 ? "Up " : "Down ") + Math.abs(floor) + " floor"
                        + (Math.abs(floor) > 1 ? "s " : " ") + (n.getId() == "prev_icon" ? "from" : "to") + " \n" + id;
            }
        }
        Tooltip t = new Tooltip(tooltip_text);
        t.styleProperty().set("-fx-background-color: #4863A0; -fx-text-fill: #ffffff; -fx-font-size: 25");
        n.setOnMouseEntered(event -> t.show(n, event.getSceneX() + 100, event.getSceneY() - t.getHeight() + 100));
        n.setOnMouseExited(event -> t.hide());
    }

    public int nextFloorChange() {
        if (path_segments.size() > seg_index + 1) {
            return (Map.floor_ids.indexOf(this.path_segments.get(seg_index + 1).seg_path.get(0).floor) - PathfindController.current_floor);
        }
        return 0;
    }

    public int prevFloorChange() {
        if (seg_index > 0) {
            return (PathfindController.current_floor - Map.floor_ids.indexOf(this.path_segments.get(seg_index - 1).seg_path.get(0).floor));
        }
        return 0;
    }

    public String nextFloorId() {
        if (path_segments.size() > seg_index + 1) {
            return this.path_segments.get(seg_index + 1).seg_path.get(0).getLongName();
        }
        return "";
    }

    public String prevFloorId() {
        if (seg_index > 0) {
            return this.path_segments.get(seg_index - 1).seg_path.get(this.path_segments.get(seg_index - 1).seg_path.size() - 1).getLongName();
        }
        return "";
    }

    /**
     * Creates Ants that follow the given polyline and fill the length of the polyline.
     *
     * @param p the polyline to animate ants on top of.
     * @return an ArrayList of all the JavaFX Nodes that the Ants will animate to be placed in a scene.
     */
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

    /**
     * Creates and plays the animation of the visible portion of a path polyline, unless the polyline has only one point.
     *
     * @param p polyline to animate.
     * @param is3D whether or not the map it is displayed on is 3D.
     */
    void playDrawingAnimation(Polyline p, boolean is3D) {
        Platform.runLater(() -> {
            double length = this.path_segments.get(seg_index).getTotalLength(is3D);
            if (length == 0) {
                return;
            }
            p.getStrokeDashArray().addAll(length, length);
            p.setStrokeDashOffset(length);
            new Timeline(new KeyFrame(
                    Duration.seconds(length / PATH_DRAW_SPEED),
                    new KeyValue(
                            p.strokeDashOffsetProperty(),
                            0.0,
                            Interpolator.EASE_BOTH
                    )
            )).play();
        });
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

    /**
     * Calculates the total length (in pixels) of this PathSegment.
     *
     * @param is3D whether or not the map it is displayed on is 3D.
     * @return the total length (in pixels) of this PathSegment. Zero if the segment has only one point.
     */
    double getTotalLength(boolean is3D) {
        double total_length = 0;
        int x, y, prev_x, prev_y;
        for (int i = 1; i < seg_path.size(); i++) {
            if (is3D) {
                x = seg_path.get(i).getXCoord3D();
                y = seg_path.get(i).getYCoord3D();
                prev_x = seg_path.get(i - 1).getXCoord3D();
                prev_y = seg_path.get(i - 1).getYCoord3D();
            } else {
                x = seg_path.get(i).getXCoord();
                y = seg_path.get(i).getYCoord();
                prev_x = seg_path.get(i - 1).getXCoord();
                prev_y = seg_path.get(i - 1).getYCoord();
            }
            total_length += Math.pow(Math.pow(x - prev_x, 2) + Math.pow(y - prev_y, 2), 0.5);
        }
        return total_length;
    }
}