package pathfind;

import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import jdk.nashorn.internal.ir.debug.ASTWriter;

import java.util.ArrayList;

public class Map3D extends Map {

    private final int MAP_WIDTH = 5000;
    private final int MAP_HEIGHT = 2774;

    /**
     * Constructs a 3D map with path drawing capabilities
     * @param map FXML ImageView that the map refers to
     * @param path_polyline FXML Polyline used for drawing on the map
     * @param destination_img FXML ImageView that will mark the destination of the path
     * @author Will Lucca
     */
    public Map3D(ImageView map, Polyline path_polyline, Polyline path_polyline_2, ImageView destination_img, ScrollPane scroll_pane, int floor){
        this.map = map;
        this.path_polyline = path_polyline;
        this.path_polyline_2 = path_polyline_2;
        this.destination_img = destination_img;
        this.scroll_pane = scroll_pane;
        this.floor = floor;
    }


    public void drawPath() {
        double x_coord = 0;
        double y_coord = 0;
        path_polyline.getPoints().clear();
        path_polyline_2.getPoints().clear();
        Bounds img_bounds = map.getBoundsInParent();

        boolean polyline_begun = false;
        boolean polyline_broken = false;
        for (AStarNode node : path){
            if (Map.floor_ids[this.floor].equals(node.floor)) {
                polyline_begun = true;
                x_coord = ((double) node.getX_coord_3d() / MAP_WIDTH) * img_bounds.getWidth() + img_bounds.getMinX();
                y_coord = ((double) node.getY_coord_3d() / MAP_HEIGHT) * img_bounds.getHeight() + img_bounds.getMinY();
                if(polyline_broken)
                    path_polyline_2.getPoints().addAll(x_coord, y_coord);
                else
                    path_polyline.getPoints().addAll(x_coord, y_coord);
            }
            else if(polyline_begun) {
                polyline_broken = true;
                polyline_begun = false;
            }
        }


        // Positioning star
        destination_img.setTranslateX(x_coord - destination_img.getFitWidth() / 2);
        destination_img.setTranslateY(y_coord - destination_img.getFitHeight() / 2);

        // Centering ScrollPane on path
        int start_x = path.get(0).getXCoord();
        int end_x = path.get(path.size() - 1).getXCoord();
        int start_y = path.get(0).getYCoord();
        int end_y = path.get(path.size() - 1).getYCoord();
        scroll_pane.setHvalue(start_x);// + Math.abs((end_x - start_x) / 2));
        scroll_pane.setVvalue(start_y);// + Math.abs((end_y - start_y) / 2));
    }
}
