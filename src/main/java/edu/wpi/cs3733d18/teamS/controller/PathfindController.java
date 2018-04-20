package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;

import java.io.IOException;
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
    VBox  direct_list;

    @FXML
    ScrollPane directions_pane;

    @FXML
    Pane directions_anchor;

    @FXML
    TextField phone_field;

    @FXML
    Button call_btn, text_btn;

    /**
     * Stores the zoom factor.
     */
    private double zoom_factor;

    /**
     * Stores the controller between the database and the application.
     */
    private Storage db_storage;

    /**
     * Stores a boolean to track whether or not the program is already in the 3D mode.
     */
    private boolean in3DMode = false;

    /**
     * The current floor on the application.
     */
    public static int current_floor;

    /**
     * Stores the Map.
     */
    private Map map;

    private ArrayList<String> path_steps;

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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
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
        stack_pane.setAlignment(zoom_scroll, Pos.TOP_LEFT);
        zoom_scroll.setMin(0.5);
        zoom_scroll.setMax(1.8);
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
    }

    /**
     * Zooms the screen in or out depending on the button pushed.
     *
     * @param zoom_amount the amount the screen will scale in or out.
     */
    private void zoom(double zoom_amount) {
        zoom_factor = zoom_amount;
        zoom_scroll.setValue(zoom_factor);
        if (zoom_factor < 0.5) {
            zoom_factor = 0.5;
        }
        if (zoom_factor > 1.8) {
            zoom_factor = 1.8;
        }
        map_scroll_pane.getContent().setScaleX(zoom_factor);
        map_scroll_pane.setLayoutX(zoom_factor);
        map_scroll_pane.getContent().setScaleY(zoom_factor);
        map_scroll_pane.setLayoutY(zoom_factor);
        map_scroll_pane.snapshot(new SnapshotParameters(), new WritableImage(1, 1));
    }

    /**
     * Sets the zoom amount for when the zoom out node is clicked in the scene.
     */
    public void onZoomOut() {
        zoom(zoom_factor - 0.2);
    }

    /**
     * Sets the zoom amount for when the zoom in node is clicked in the scene.
     */
    public void onZoomIn() {
        zoom(zoom_factor + 0.2);
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
        zoom(1);
        map_scroll_pane.setHvalue(.5);
        map_scroll_pane.setVvalue(.5);
        double nodes_width = p.getBoundsInParent().getWidth();
        double nodes_height = p.getBoundsInParent().getHeight();

        double pane_width = map_scroll_pane.getWidth();
        double pane_height = map_scroll_pane.getHeight();

        double zoom1 = (pane_width / (nodes_width + 250));
        double zoom2 = (pane_height / (nodes_height + 250));
        double zoom_amount = Math.min(zoom1, zoom2);


        double map_width = map_img.getFitWidth();
        double map_height = map_img.getFitHeight();

        double poly_width = p.getBoundsInParent().getWidth();
        double poly_height = p.getBoundsInParent().getHeight();
        double poly_x = p.getBoundsInParent().getMinX();
        double poly_y = p.getBoundsInParent().getMinY();
        double desired_x = poly_x + (poly_width / 2);
        double desired_y = poly_y + (poly_height / 2);
        desired_x = p.getPoints().get(0);
        desired_y = p.getPoints().get(1);
        if(map.is_3D){
            desired_y += 400;
        }

        map_scroll_pane.setHvalue((desired_x - (0.5 * pane_width)) / (map_width - pane_width));
        map_scroll_pane.setVvalue((desired_y - (0.5 * pane_height)) / (map_height - pane_height));
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
        Image m = getFloorImage(current_floor, in3DMode);
        map_img.setImage(m);
        map.clearIcons();
        fitToPath(nodes);
        map_anchor_pane.getChildren().addAll(nodes);
        step_indicator.setText(AllText.get("step") + ": " + (map.getPath().seg_index + 1) + " / " + map.getPath().getPathSegments().size());
        floor_indicator.setText(AllText.get("floor") + ": " + Map.floor_ids.get(current_floor));
    }

    /**
     * Manages switching the map between 3d and 2D and updating the nodes.
     */
    public void toggleMappingType() {
        if (map.is_3D) {
            toggle_map_btn.setText(AllText.get("3d_map"));
            in3DMode = false;
            map.is_3D = false;
        } else {
            toggle_map_btn.setText(AllText.get("2d_map"));
            in3DMode = true;
            map.is_3D = true;
        }

        Path path = null;
        if (map != null) {
            map.clearIcons();
            path = map.getPath();
        }

        if (path != null) {
            Map.path = path;
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
        //stack_pane.getChildrenUnmodifiable().get(2).setEffect(adj);
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
        //stack_pane.getChildrenUnmodifiable().get(2).setEffect(null);
    }

    public void onPhoneCallBtnClick(){
        if(!phone_field.getText().isEmpty()){
            InteractivePhone call = new InteractivePhone(path_steps);
            call.callDirections(phone_field.getText());
            onBigQRClick();
        }
    }

    public void onPhoneTextBtnClick(){
        if(!phone_field.getText().isEmpty()){
            InteractivePhone call = new InteractivePhone(path_steps);
            call.textDirections(phone_field.getText(), directions_url);
            onBigQRClick();
        }
    }

    public void onPrevClick() {
        updateMap(map.prevStep(map.is_3D));
        if (map.getPath().seg_index < map.getPath().path_segments.size() && next_btn.disableProperty().getValue()) {
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
        }
        else if (step_description.contains(AllText.get("turn_right"))) {
            icon = new ImageView(new Image("images/directionsIcons/right.png"));
        }
        else if (step_description.contains(AllText.get("continue_straight")) || step_description.contains(AllText.get("first_step_dirs"))) {
            icon = new ImageView(new Image("images/directionsIcons/walking.png"));
        }
        else if (step_description.contains(AllText.get("stairs_up"))) {
            icon = new ImageView(new Image("images/mapIcons/up.png"));
        }
        else if (step_description.contains(AllText.get("stairs_down"))) {
            icon = new ImageView(new Image("images/mapIcons/down.png"));
        }
        else if (step_description.contains(AllText.get("destination_to"))){
            icon = new ImageView(new Image("images/mapIcons/destinationIcon.png"));
        }
        else {
            icon = new ImageView();
            icon.setFitWidth(32);
            icon.setFitHeight(32);
        }
        return icon;
    }
}
