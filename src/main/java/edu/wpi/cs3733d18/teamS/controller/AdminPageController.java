package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d18.teamS.arduino.MotionSensor;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller that deals with the interactions between the admin and the user.
 * @author Matthew McMillan
 * @author Mitch Gaines
 * @author Danny Sullivan
 * @author Cormac Lynch-Collier
 * @author Noah Hillman
 * @version 1.3, April 13, 2018
 */
public class AdminPageController extends UserController {

    //UserController edu.wpi.cs3733d18.teamS.user = new UserController();
    /**
     * Stores the user.
     */
    UserController user = new UserController();

    /**
     * The main Pane.
     */
    @FXML
    BorderPane main_pane;

    /**
     * The time label.
     */
    @FXML
    Label time;

    /**
     * The Stack Pane
     */
    @FXML
    StackPane stack_pane;

    /**
     * Stores the object loader.
     */
    FXMLLoader loader;

    /**
     * Loads the reports.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void loadReports() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/Report.fxml"), AllText.getBundle());
        Parent root = loader.load();
        main_pane.setCenter(root);
    }

    public void loadUserReports() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/UserReport.fxml"), AllText.getBundle());
        Parent root = loader.load();
        main_pane.setCenter(root);
    }

    /**
     * Loads in the admin.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void loadAdmin() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/AdminSpecialOptions.fxml"), AllText.getBundle());
        Parent root = loader.load();
        AdminSpecialOptionsController asoc = loader.getController();
        asoc.setParent(this);
        asoc.setUp(this_user, page);
        main_pane.setCenter(root);
    }

    public void setUp(User user, String page) {
        this_user = user;
        this.page = page;
        super.setUp(user, page);
    }

    private User this_user;
    private String page;

    /**
     * Initializes the service requests menu.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void initialize() throws IOException {
        requestSidebarController.setParent(this);
        requestSidebarController.setAdminParent(this);
        serviceAreaController.setParent(this);
        MakeRequest();
        updatedTime();
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

    /**
     * Loads in the special requests.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void loadSpecialRequests() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/SpecialRequests.fxml"), AllText.getBundle());
        Parent root = loader.load();
        main_pane.setCenter(root);
    }

}
