package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.pathfind.InteractivePhone;
import edu.wpi.cs3733d18.teamS.pathfind.Map;
import edu.wpi.cs3733d18.teamS.pathfind.Path;
import edu.wpi.cs3733d18.teamS.pathfind.QRCode;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PathfindController {
    /**
     * Nodes for zooming in and out of the map.
     */
    @FXML
    ImageView zoom_in, zoom_out;

    /**
     * Node for going to the previous segment of the path.
     */
    @FXML
    Button back_button;

    /**
     * The AnchorPane for the scene.
     */
    @FXML
    AnchorPane map_anchor_pane;

    @FXML
    Slider zoom_scroll;

    /**
     * The ScrollPane for the map image.
     */
    @FXML
    ScrollPane map_scroll_pane;

    /**
     * The map image.
     */
    @FXML
    ImageView map_img;

    /**
     * The BorderPane.
     */
    @FXML
    BorderPane main_pane;

    /**
     * The nodes for the time and current floor indicator.
     */
    @FXML
    Label time, floor_indicator, step_indicator;

    /**
     * The Header and Footer BorderPanes.
     */
    @FXML
    BorderPane header_pane, footer_pane;

    /**
     * The node for switching between 2D and 3D maps.
     */
    @FXML
    Button toggle_map_btn, next_btn, prev_btn;

    /**
     * Node for expanding the QR code with the step by step directions.
     */
    @FXML
    ImageView expanded_qr;

    //@FXML
    //AnchorPane directions_pane;

    @FXML
    StackPane stack_pane;

    @FXML
    HBox directions_box;

    @FXML
    VBox direct_list;

    @FXML
    ScrollPane directions_pane;

    @FXML
    Pane directions_anchor;

    @FXML
    TextField phone_field;

    @FXML
    Button call_btn, text_btn;

    @FXML
    HBox breadcrumb_box;

    @FXML
    HBox breadcrumb_label;

    /**
     * Stores the zoom factor.
     */
    private double zoom_factor;

    /**
     * Stores the controller between the database and the application.
     */
    private Storage db_storage;

    /**
     * The current floor on the application.
     */
    public static int current_floor;

    private static double MIN_ZOOM = .25;
    private static double MAX_ZOOM = 1.3;

    private boolean first_3d_switch = false;
    /**
     * Stores the Map.
     */
    private Map map;

    private ArrayList<String> path_steps;

    private ArrayList<Node> list_of_nodes = new ArrayList<>();

    private URL directions_url;

    /**
     * Returns the image of the floor based on whether or not the map is 3D or 2D.
     *
     * @param floor the floor requested.
     * @param is3D  boolean for whether the image needs to be 2D or 3D.
     * @return The image of the particular floor.
     */
    private static Image getFloorImage(int floor, boolean is3D) {
        String[] images2d = {"images/2dMaps/00_thelowerlevel2.png",
                "images/2dMaps/00_thelowerlevel1.png",
                "images/2dMaps/01_thefirstfloor.png",
                "images/2dMaps/02_thesecondfloor.png",
                "images/2dMaps/03_thethirdfloor.png"};

        String[] images3d = {"images/3dMaps/L2-NO-ICONS.png",
                "images/3dMaps/L1-NO-ICONS.png",
                "images/3dMaps/1-NO-ICONS.png",
                "images/3dMaps/2-NO-ICONS.png",
                "images/3dMaps/3-NO-ICONS.png"};

        if (!is3D)
            return new Image(images2d[floor]);
        else
            return new Image(images3d[floor]);
    }

    /**
     * Initializes the pathfind screen, by setting up the date and time, the database, and map.
     */
    public void initialize() {
        zoom_factor = 1;
        updatedTime();

        this.db_storage = Storage.getInstance();

        map = new Map(map_anchor_pane, false);
        updateMap(map.thisStep(map.is_3D));
        if (map.getPath().path_segments.size() < 2) {
            next_btn.disableProperty().setValue(true);
        }

        path_steps = map.getPath().getPathDirections();
        //directions_vbox.setMaxWidth(directions_pane.getMaxWidth());
        for (String path_step : path_steps) {
            Label step_label = new Label(path_step.substring(2), getDirectionsIcon(path_step));
            VBox.setMargin(step_label, new Insets(0, 0, 0, 15.0));
            //step_label.setMaxWidth(directions_pane.getWidth() - 20);
            step_label.setFont(new Font(24));
            step_label.setWrapText(true);
            step_label.setGraphicTextGap(15.0);
            step_label.setMinHeight(90);
            direct_list.getChildren().add(step_label);
            direct_list.getChildren().add(new Separator());
        }
        // Remove last separator
        directions_anchor.setPrefHeight(direct_list.getHeight());
        direct_list.getChildren().remove(direct_list.getChildren().size() - 1);

        try {
            directions_url = map.getPath().getPathDirectionsURL(path_steps);
            generateQRCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //setup the scroll pane
        stack_pane.setAlignment(zoom_scroll, Pos.TOP_LEFT);
        zoom_scroll.setMin(MIN_ZOOM);
        zoom_scroll.setMax(MAX_ZOOM);
        zoom_scroll.setValue(1);
        zoom_scroll.setShowTickMarks(true);
        zoom_scroll.setMajorTickUnit(0.2);
        zoom_scroll.setStyle("-fx-background-color: #4863A0;");
        zoom_scroll.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom(newValue.doubleValue());
            }
        });
        breadcrumb_box.setPickOnBounds(false);
        onNodeClick(0);
    }

    /**
     * Zooms the screen in or out depending on the button pushed.
     *
     * @param zoom_amount the amount the screen will scale in or out.
     */
    private void zoom(double zoom_amount) {
        double old_x = map_img.getImage().getWidth() * map_scroll_pane.getHvalue() + ((.5 - map_scroll_pane.getHvalue()) * map_scroll_pane.getWidth());
        double old_y = map_img.getImage().getHeight() * map_scroll_pane.getVvalue() + ((.5 - map_scroll_pane.getVvalue()) * map_scroll_pane.getHeight());
        zoom_factor = Math.max(Math.min(zoom_amount, MAX_ZOOM), MIN_ZOOM);
        zoom_scroll.setValue(zoom_factor);
        map_scroll_pane.getContent().setScaleX(zoom_factor);
        map_scroll_pane.setLayoutX(zoom_factor);
        map_scroll_pane.getContent().setScaleY(zoom_factor);
        map_scroll_pane.setLayoutY(zoom_factor);
        //scrollToPoint(old_x, old_y);
    }

    /**
     * Moves the viewport to the center of the given unscaled point using the current scale_factor
     *
     * @param x unscaled x position
     * @param y unscaled y position
     */
    private void scrollToPoint(double x, double y) {
        double pane_width = map_scroll_pane.getWidth();
        double pane_height = map_scroll_pane.getHeight();
        double map_width = map_img.getImage().getWidth();
        double map_height = map_img.getImage().getHeight();
        if (map.is_3D) {
            map_width = 5000;
            map_height = 2774;
        }
        if (pane_height == 0) {
            pane_height = 917;
            pane_width = 1920;
        }
        double new_x = (x - (map_width / 2)) * zoom_factor + (map_width / 2);
        double new_y = (y - (map_height / 2)) * zoom_factor + (map_height / 2);
        double h_val = Math.min(Math.max(0.0, (new_x - (0.5 * pane_width)) / (map_width - pane_width)), 1.0);
        double v_val = Math.min(Math.max(0.0, (new_y - (0.5 * pane_height)) / (map_height - pane_height)), 1.0);
        map_scroll_pane.setHvalue(h_val);
        map_scroll_pane.setVvalue(v_val);
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

    /**
     * Scrolls the map so that the start node is more centered on the screen.
     *
     * @param nodes
     */
    private void fitToPath(ArrayList<Node> nodes) {
        Polyline p = (Polyline) nodes.get(0);
        double nodes_width = p.getBoundsInParent().getWidth();
        double nodes_height = p.getBoundsInParent().getHeight();

        double pane_width = map_scroll_pane.getWidth();
        double pane_height = map_scroll_pane.getHeight();
        if (pane_height == 0 || pane_width == 0) {
            pane_width = 1920;
            pane_height = 917;
        }
        double zoom1 = (pane_width / (nodes_width + 200));
        double zoom2 = (pane_height / (nodes_height + 200));
        double zoom_amount = Math.min(zoom1, zoom2);
        zoom(zoom_amount);

        double poly_width = p.getBoundsInParent().getWidth();
        double poly_height = p.getBoundsInParent().getHeight();
        double poly_x = p.getBoundsInParent().getMinX();
        double poly_y = p.getBoundsInParent().getMinY();
        double desired_x = poly_x + (poly_width / 2);
        double desired_y = poly_y + (poly_height / 2);
        if (first_3d_switch) {
            first_3d_switch = false;
            desired_y -= 200;
        }
        scrollToPoint(desired_x, desired_y);
    }

    /**
     * Generates the QR code text directions for the pathway.
     */
    private void generateQRCode() {
        QRCode qr = new QRCode(directions_url.toString());
        expanded_qr.setImage(SwingFXUtils.toFXImage(qr.getQRCode(), null));
    }

    /**
     * Updates the map to display different nodes and icons and clears the previous ones.
     *
     * @param nodes ArrayList of nodes to be displayed on the new map.
     */
    public void updateMap(ArrayList<Node> nodes) {
        Image m = getFloorImage(current_floor, map.is_3D);
        map_img.setImage(m);
        map.clearIcons();
        //set click method calling
        for (Node a_node : nodes) {
            if (a_node.getId().equals("next_icon")) {
                a_node.setOnMouseClicked(event -> onNextClick());
            } else if(a_node.getId().equals("prev_icon")){
                a_node.setOnMouseClicked(event -> onPrevClick());
            }
        }
        breadcrumb_box.getChildren().clear();
        breadcrumb_label.getChildren().clear();
        ArrayList<Node> breadcrumbs = map.getPath().generateBreadcrumbs();
        for (Node breadcrumb : breadcrumbs){
            if(breadcrumb.getUserData() != null){
                String s = (String) breadcrumb.getUserData();
                int i = Integer.parseInt(s.substring(s.length()-1));
                breadcrumb.setOnMouseClicked(event -> onNodeClick(i));
            }

            breadcrumb_box.getChildren().add(breadcrumb);

            Text t = new Text();
            t.setTextAlignment(TextAlignment.CENTER);
            t.setFill(Color.WHITE);
            if (breadcrumb.getId().equals("flag")){
                String s = (String) breadcrumb.getUserData();
                t.setText(s.substring(0, s.length()-1));
                breadcrumb_label.getChildren().add(t);
            } else {
                t.setText("      ");
                breadcrumb_label.getChildren().add(t);
            }
        }
        map_anchor_pane.getChildren().addAll(nodes);
        fitToPath(nodes);
        this.list_of_nodes = nodes;
        step_indicator.setText(AllText.get("step") + ": " + (map.getPath().seg_index + 1) + " / " + map.getPath().getPathSegments().size());
        floor_indicator.setText(AllText.get("floor") + ": " + Map.floor_ids.get(current_floor));
    }

    private void onNodeClick(int seg_int){
        Glow g = new Glow(40);
        Path.seg_index = seg_int;
        updateMap(map.thisStep(map.is_3D));
        for (Node n : breadcrumb_box.getChildren()){
            if(n.getUserData() != null) {
                String s = (String) n.getUserData();
                int i = Integer.parseInt(s.substring(s.length() - 1));
                if(i == seg_int){
                    n.setEffect(new Glow(40));
                    n.setOpacity(1);
                } else {
                    n.setEffect(null);
                    n.setOpacity(0.25);
                }
            }
        }
        if (map.getPath().seg_index < map.getPath().path_segments.size() && next_btn.disableProperty().getValue()) {
            next_btn.disableProperty().setValue(false);
        }
        if (map.getPath().seg_index <= 0) {
            prev_btn.disableProperty().setValue(true);
        }
        if (map.getPath().seg_index > 0 && prev_btn.disableProperty().getValue()) {
            prev_btn.disableProperty().setValue(false);
        }
        if (map.getPath().seg_index >= map.getPath().path_segments.size() - 1) {
            next_btn.disableProperty().setValue(true);
        }
    }

    /**
     * Manages switching the map between 3d and 2D and updating the nodes.
     */
    public void toggleMappingType() {
        if (map.is_3D) {
            toggle_map_btn.setText(AllText.get("3d_map"));
            map.is_3D = false;
        } else {
            first_3d_switch = true;
            toggle_map_btn.setText(AllText.get("2d_map"));
            map.is_3D = true;
        }
        updateMap(map.thisStep(map.is_3D));
    }

    /**
     * Sets the screen to display the QR code.
     */
    public void onQRClick() {
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        ObservableList<Node> list = main_pane.getChildren();
        for (Node a_node : list) {
            a_node.setEffect(adj);
        }

        stack_pane.setEffect(null);
        stack_pane.getChildrenUnmodifiable().get(0).setEffect(adj);
        stack_pane.getChildrenUnmodifiable().get(1).setEffect(adj);
        stack_pane.getChildrenUnmodifiable().get(2).setEffect(adj);
        directions_box.setVisible(true);
        expanded_qr.isPreserveRatio();
    }

    /**
     * Sets the screen for expanding the QR code.
     */
    public void onBigQRClick() {
        directions_box.setVisible(false);
        ObservableList<Node> list = main_pane.getChildren();
        for (Node a_node : list) {
            a_node.setStyle("-fx-background-color: null");
            a_node.setEffect(null);
        }
        header_pane.setStyle("-fx-background-color: #4863A0");
        footer_pane.setStyle("-fx-background-color: #4863A0");

        stack_pane.setEffect(null);
        stack_pane.getChildrenUnmodifiable().get(0).setEffect(null);
        stack_pane.getChildrenUnmodifiable().get(1).setEffect(null);
        stack_pane.getChildrenUnmodifiable().get(2).setEffect(null);
    }

    public void onPhoneCallBtnClick() {
        if (!phone_field.getText().isEmpty()) {
            InteractivePhone call = new InteractivePhone(path_steps);
            call.callDirections(phone_field.getText());
            onBigQRClick();
        }
    }

    public void onPhoneTextBtnClick() {
        if (!phone_field.getText().isEmpty()) {
            InteractivePhone call = new InteractivePhone(path_steps);
            call.textDirections(phone_field.getText(), directions_url);
            onBigQRClick();
        }
    }

    public void onPrevClick() {
        updateMap(map.prevStep(map.is_3D));
        if (map.getPath().seg_index < map.getPath().path_segments.size() && next_btn.disableProperty().getValue() && map.getPath().path_segments.size() > 1) {
            next_btn.disableProperty().setValue(false);
        }
        if (map.getPath().seg_index <= 0) {
            prev_btn.disableProperty().setValue(true);
        }
    }

    /**
     * Updates the map to show the next segment of the path.
     */
    public void onNextClick() {
        updateMap(map.nextStep(map.is_3D));
        if (map.getPath().seg_index > 0 && prev_btn.disableProperty().getValue()) {
            prev_btn.disableProperty().setValue(false);
        }
        if (map.getPath().seg_index >= map.getPath().path_segments.size() - 1) {
            next_btn.disableProperty().setValue(true);
        }
    }

    private ImageView getDirectionsIcon(String step_description) {
        ImageView icon;
        if (step_description.contains(AllText.get("turn_left"))) {
            icon = new ImageView(new Image("images/directionsIcons/left.png"));
        } else if (step_description.contains(AllText.get("turn_right"))) {
            icon = new ImageView(new Image("images/directionsIcons/right.png"));
        } else if (step_description.contains(AllText.get("continue_straight")) || step_description.contains(AllText.get("first_step_dirs"))) {
            icon = new ImageView(new Image("images/directionsIcons/walking.png"));
        } else if (step_description.contains(AllText.get("stairs_up"))) {
            icon = new ImageView(new Image("images/mapIcons/up.png"));
        } else if (step_description.contains(AllText.get("stairs_down"))) {
            icon = new ImageView(new Image("images/mapIcons/down.png"));
        } else if (step_description.contains(AllText.get("destination_to"))) {
            icon = new ImageView(new Image("images/mapIcons/destinationIcon.png"));
        } else {
            icon = new ImageView();
            icon.setFitWidth(32);
            icon.setFitHeight(32);
        }
        return icon;
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
}
