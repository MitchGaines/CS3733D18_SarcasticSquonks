package edu.wpi.cs3733d18.teamS.pathfind;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * This class is an animated "ant" that travels along a specified path repeatedly.
 *
 * @author Will Lucca
 */
public class Ant {
    private static final double PIXELS_PER_SECOND = 55.0; // marching speed

    private ImageView image = new ImageView(new Image("images/mapIcons/ant.png"));
    private Timeline timeline = new Timeline();
    private ArrayList<Double> coord_path = new ArrayList<>();
    private double total_animation_time = 0;
    private double last_x, last_y;


    /**
     * Constructs an Ant with no path.
     */
    public Ant() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        image.setId("temporaryIcon");
        image.setTranslateX(0 - image.getImage().getWidth() / 2);
        image.setTranslateY(0 - image.getImage().getHeight() / 2);
    }

    /**
     * Constructs an Ant that begins its animation at the given coordinates.
     *
     * @param start_x x-coordinate of start location.
     * @param start_y y-coordinate of start location.
     */
    public Ant(double start_x, double start_y) {
        this();
        last_x = start_x;
        last_y = start_y;
        addStop(start_x, start_y);
    }

    /**
     * addStop
     * Adds a stop to the animation cycle at the given coordinate.
     *
     * @param x x-coordinate to add stop at.
     * @param y y-coordinate to add stop at.
     */
    void addStop(double x, double y) {
        coord_path.add(x);
        coord_path.add(y);
        boolean is_start = timeline.getKeyFrames().size() == 0;
        timeline.getKeyFrames().add(new KeyFrame(
                is_start ? Duration.ZERO : Duration.seconds(total_animation_time += Math.hypot(x - last_x, y - last_y) / PIXELS_PER_SECOND),
                new KeyValue(
                        image.layoutXProperty(),
                        x,
                        Interpolator.LINEAR
                ),
                new KeyValue(
                        image.layoutYProperty(),
                        y,
                        Interpolator.LINEAR
                )
        ));
        last_x = x;
        last_y = y;
    }

    /**
     * playFromStart
     * Begins marching animation from the first stop on the path.
     */
    void playFromStart() {
        timeline.playFromStart();
    }

    /**
     * playFrom
     * Begins marching animation from the specified starting time (in seconds).
     *
     * @param seconds time (in seconds) to begin the marching animation from.
     */
    void playFrom(double seconds) {
        timeline.playFrom(Duration.seconds(seconds));
    }

    /**
     * getDuration
     * Returns the duration (in seconds) of the marching animation from start to finish.
     *
     * @return the duration (in seconds) of the marching animation from start to finish
     */
    double getDuration() {
        return total_animation_time;
    }

    /**
     * getImageView
     * Returns the JavaFX ImageView associated with the Ant. Used to place this Ant in a scene.
     *
     * @return the JavaFX ImageView associated with the Ant.
     */
    ImageView getImageView() {
        return image;
    }

    /**
     * duplicate
     * Returns a deep-copy of this Ant. Used for creating multiple Ants that follow the same path.
     *
     * @return a deep-copy of this Ant.
     */
    Ant duplicate() {
        Ant duplicate = new Ant();
        for (int i = 0; i < coord_path.size(); i += 2) {
            duplicate.addStop(this.coord_path.get(i), this.coord_path.get(i + 1));
        }
        duplicate.total_animation_time = this.total_animation_time;
        duplicate.last_x = this.last_x;
        duplicate.last_y = this.last_y;
        return duplicate;
    }
}
