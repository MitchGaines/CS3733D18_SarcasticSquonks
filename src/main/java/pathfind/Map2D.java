// Will Lucca

package pathfind;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import java.util.ArrayList;

public class Map2D extends Map {

    private final int MAP_WIDTH = 5000;
    private final int MAP_HEIGHT = 3400;

    private ImageView map;
    private Polyline path_polyline;
    private ImageView destination_img;

    /**
     * Constructs a 2D map with path drawing capabilities
     * @param map FXML ImageView that the map refers to
     * @param path_polyline FXML Polyline used for drawing on the map
     * @param destination_img FXML ImageView that will mark the destination of the path
     * @author Will Lucca
     */
    public Map2D(ImageView map, Polyline path_polyline, ImageView destination_img){
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
        path_polyline.getPoints().clear();
        Bounds img_bounds = map.getBoundsInParent();
        for (AStarNode node : path) {
            path_polyline.getPoints().addAll(((double)node.getX_coord() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX(),
                    ((double)node.getY_coord() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY());
        }
    }
}
