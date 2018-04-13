package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import org.apache.commons.codec.binary.Base64;

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
    ArrayList<AStarNode> algorithm_node_path = new ArrayList<>();

    public Path() {
    }


    /**
     * getPathDirections
     * Once path is generated, will generate text descriptions for turns based on the path nodes, it also manages and
     * condenses repeated go straight instructions into a single, text instruction.
     *
     * @return URL based on calculated turns at each node.
     */
    public URL getPathDirections() throws MalformedURLException {
        String host = "159.203.189.146";
        int port = 3000;
        String protocol = "http";
        String language = "en"; //TODO add multilangauge support
        String path = "/" + language + "/";

        if (this.algorithm_node_path.size() == 0) {
            return new URL(protocol, host, port, path);
        }
        StringBuilder path_description = new StringBuilder();
        path_description.append("Directions from: ").append(algorithm_node_path.get(0).getLongName()).append(" to: ").append(algorithm_node_path.get(algorithm_node_path.size() - 1).getLongName()).append(".").append(System.lineSeparator());

        path_description.append("First, begin walking towards ").append(algorithm_node_path.get(1).short_name).append(".").append(System.lineSeparator());
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

            path_description.append("In ").append(distance).append(" feet, ").append(calcTurn(algorithm_node_path.get(i), algorithm_node_path.get(i + 1))).append(".").append(System.lineSeparator());

            if (next_floor > curr_floor) {
                path_description.append("Use stairs/elevator to go up to floor ").append(algorithm_node_path.get(i).floor).append(".").append(System.lineSeparator());
            } else if (next_floor < curr_floor) {
                path_description.append("Use stairs/elevator to go down to floor ").append(algorithm_node_path.get(i).floor).append(".").append(System.lineSeparator());
            }
        }
        path_description.append("Destination is ").append(calcDistance(algorithm_node_path.get(algorithm_node_path.size() - 2), algorithm_node_path.get(algorithm_node_path.size() - 1))).append(" feet ahead.");

        byte[] path_bytes = path_description.toString().getBytes();
        String enc_bytes = Base64.encodeBase64String(path_bytes);
        path += enc_bytes;

        return new URL(protocol, host, port, path);
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
            return "turn left";
        } else if (angle >= MIN_TURN_ANGLE) {
            return "turn right";
        } else {
            return "continue straight";
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

    private double[] getPoints(boolean is_3D, AStarNode node) {
        if (is_3D) {
            return new double[]{node.getXCoord3D(), node.getYCoord3D()};
        } else {
            return new double[]{node.getXCoord(), node.getYCoord()};
        }
    }

    ArrayList<ImageView> generateIcons(boolean is_3D, int floor) {
        int current_floor = -1;
        int previous_floor;
        ArrayList<ImageView> icons = new ArrayList<>();
        double x;
        double y;

        for (AStarNode node : algorithm_node_path) {
            previous_floor = current_floor;
            current_floor = Map.floor_ids.indexOf(node.floor);
            double[] coords = getPoints(is_3D, node);
            x = coords[0];
            y = coords[1];

            if (current_floor > previous_floor) {
                if (previous_floor == floor) {
                    icons.add(generateIcon(2, x, y));
                }
            } else if (current_floor < previous_floor) {
                if (previous_floor == floor) {
                    icons.add(generateIcon(3, x, y));
                }
            }
        }
        return icons;
    }

    ArrayList<Polyline> generatePolylines(boolean is_3D, int floor) {
        double x;
        double y;
        ArrayList<Polyline> polylines = new ArrayList<>();
        int index = -1;
        boolean polyline_begun = false;
        for (AStarNode node : algorithm_node_path) {
            double[] coords = getPoints(is_3D, node);
            x = coords[0];
            y = coords[1];
            if (node.floor.equals(Map.floor_ids.get(floor))) {
                if (polyline_begun) {
                    polylines.get(index).getPoints().addAll(x, y);
                } else {
                    polyline_begun = true;
                    index++;
                    polylines.add(new Polyline());
                    polylines.get(index).getPoints().addAll(x, y);
                }
            } else {
                polyline_begun = false;
            }
        }
        for (Polyline p : polylines) {
            stylizePolyline(p);
        }
        return polylines;
    }


    private void stylizePolyline(Polyline p) {
        p.setId("temporaryIcon");
        p.setStyle("-fx-fill:#ff000000;" +
                "-fx-stroke:#0012ff;" +
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
        polylineEffect.setBumpInput(s);
        return polylineEffect;
    }

    private ImageView generateIcon(int pic_num, double x, double y) {
        String path;
        if (pic_num == 0) {
            path = "images/mapIcons/destinationIcon.png";
        } else if (pic_num == 1) {
            path = "images/mapIcons/startingIcon.png";
        } else if (pic_num == 2) {
            path = "images/mapIcons/up.png";
        } else if (pic_num == 3) {
            path = "images/mapIcons/down.png";
        } else {
            path = "images/mapIcons/destinationIcon.png";
        }

        ImageView icon = new ImageView(new Image(path));
        icon.setId("temporaryIcon");
        icon.setTranslateX(x - icon.getImage().getWidth() / 2);
        icon.setTranslateY(y - icon.getImage().getHeight() / 2);

        return icon;
    }
}