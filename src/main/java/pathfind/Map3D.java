package pathfind;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public class Map3D extends Map {

    private final int MAP_WIDTH = 5000;
    private final int MAP_HEIGHT = 2774;

    private ImageView map;
    private Polyline path_polyline;
    private ImageView destination_img;

    /**
     * Constructs a 3D map with path drawing capabilities
     * @param map FXML ImageView that the map refers to
     * @param path_polyline FXML Polyline used for drawing on the map
     * @param destination_img FXML ImageView that will mark the destination of the path
     * @author Will Lucca
     */
    public Map3D(ImageView map, Polyline path_polyline, ImageView destination_img){
        this.map = map;
        this.path_polyline = path_polyline;
        this.destination_img = destination_img;
    }

    /**
     * Draws path on map
     * @param path List of nodes to draw along
     * @author Will Lucca
     */
    public void drawPath(ArrayList<AStarNode> path) {
        double x_coord = 0;
        double y_coord = 0;
        path_polyline.getPoints().clear();
        Bounds img_bounds = map.getBoundsInParent();
        for (AStarNode node : path) {
            x_coord = ((double)node.getX_coord_3d() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX();
            y_coord = ((double)node.getY_coord_3d() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY();
            path_polyline.getPoints().addAll(x_coord, y_coord);
        }
        destination_img.setTranslateX(x_coord - destination_img.getFitWidth() / 2);
        destination_img.setTranslateY(y_coord - destination_img.getFitHeight() / 2);
    }
}
