// Will Lucca

package pathfind;

import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import java.util.ArrayList;

public class Map2D extends Map {

    private final int MAP_WIDTH = 5000;
    private final int MAP_HEIGHT = 3400;

    private ImageView map;
    private Polyline path_polyline;
    private ImageView destination_img;
    private ScrollPane scroll_pane;

    /**
     * Constructs a 2D map with path drawing capabilities.
     * @param map FXML ImageView that the map refers to.
     * @param path_polyline FXML Polyline used for drawing on the map.
     * @param destination_img FXML ImageView that will mark the destination of the path.
     * @param scroll_pane FXML ScrollPane containing map image.
     * @author Will Lucca
     */
    public Map2D(ImageView map, Polyline path_polyline, ImageView destination_img, ScrollPane scroll_pane){
        this.map = map;
        this.path_polyline = path_polyline;
        this.destination_img = destination_img;
        this.scroll_pane = scroll_pane;
    }

    /**
     * Draws path on map.
     * @param path List of nodes to draw along.
     * @author Will Lucca
     */
    public void drawPath(ArrayList<AStarNode> path) {
        double x_coord = 0;
        double y_coord = 0;
        path_polyline.getPoints().clear();
        Bounds img_bounds = map.getBoundsInParent();
        for (AStarNode node : path) {
            x_coord = ((double)node.getXCoord() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX();
            y_coord = ((double)node.getYCoord() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY();
            path_polyline.getPoints().addAll(x_coord, y_coord);
        }

        // Positioning star
        destination_img.setTranslateX(x_coord - destination_img.getFitWidth() / 2);
        destination_img.setTranslateY(y_coord - destination_img.getFitHeight() / 2);

        // Centering ScrollPane on path
        int start_x = path.get(0).getXCoord();
        int end_x = path.get(path.size() - 1).getXCoord();
        int start_y = path.get(0).getYCoord();
        int end_y = path.get(path.size() - 1).getYCoord();
        scroll_pane.setHvalue(start_x + Math.abs((end_x - start_x) / 2));
        scroll_pane.setVvalue(start_y + Math.abs((end_y - start_y) / 2));
    }
}
