package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;

/**
 * Class that deals with the 3D version of the map and related methods.
 * @author Will Lucca
 * @author Mathew Puentes
 * @author Mitch Gaines
 * @version %I%, %G%
 */
public class Map3D extends Map {


    /**
     * Stores the pixel count for the map width.
     */
    private final int MAP_WIDTH = 5000;
    /**
     * Stores the pixel count for the map height.
     */
    private final int MAP_HEIGHT = 2774;

    /**
     * Constructs a 3D map with path drawing capabilities
     * @param map FXML ImageView that the map refers to.
     * @param path_polyline FXML Polyline used for drawing on the map.
     * @param path_polyline_2
     * @param destination_img FXML ImageView that will mark the destination of the path.
     * @param destination_img
     * @param scroll_pane
     * @param floor
     */
    public Map3D(ImageView map, Polyline path_polyline, Polyline path_polyline_2, ImageView destination_img,
                 ScrollPane scroll_pane, AnchorPane map_anchor_pane, int floor){
        this.map = map;
        this.path_polyline = path_polyline;
        this.path_polyline_2 = path_polyline_2;
        this.destination_img = destination_img;
        this.scroll_pane = scroll_pane;
        this.map_anchor_pane = map_anchor_pane;
        this.floor = floor;
    }

    /**
     * drawPath
     * Draws a path onto of the map image by drawing a line between nodes in the path of the object.
     */
    public void drawPath() {
        double x_coord = 0;
        double y_coord = 0;
        path_polyline.getPoints().clear();
        path_polyline_2.getPoints().clear();
        Bounds img_bounds = map.getBoundsInParent();

        String last_floor_accessed = "";
        boolean polyline_begun = false;
        boolean polyline_broken = false;
        for (AStarNode node : path){
            if (Map.floor_ids.get(this.floor).equals(node.floor)) {
                last_floor_accessed = node.floor;
                polyline_begun = true;
                x_coord = ((double) node.getX_coord_3d() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX();
                y_coord = ((double) node.getY_coord_3d() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY();
                if(polyline_broken)
                    path_polyline_2.getPoints().addAll(x_coord, y_coord);
                else
                    path_polyline.getPoints().addAll(x_coord, y_coord);

                if(path.indexOf(node) == 0){
                    addIcon(x_coord, y_coord, "images/mapIcons/startingIcon.png");
                }
            }
            else if(polyline_begun) {
                if(Map.floor_ids.indexOf(node.floor) > Map.floor_ids.indexOf(last_floor_accessed)){
                    addIcon(x_coord, y_coord, "images/mapIcons/up.png");
                }else{
                    addIcon(x_coord, y_coord, "images/mapIcons/down.png");
                }
                polyline_broken = true;
                polyline_begun = false;
            }
        }


        // Positioning star
        destination_img.setTranslateX(x_coord - destination_img.getFitWidth() / 2);
        destination_img.setTranslateY(y_coord - destination_img.getFitHeight() / 2);
    }
}