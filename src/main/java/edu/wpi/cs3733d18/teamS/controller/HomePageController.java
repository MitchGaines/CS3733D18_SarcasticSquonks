package edu.wpi.cs3733d18.teamS.controller;

import com.gluonhq.charm.glisten.control.ExpansionPanel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamS.user.InvalidPasswordException;
import edu.wpi.cs3733d18.teamS.user.InvalidUsernameException;
import edu.wpi.cs3733d18.teamS.user.LoginHandler;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Controller for the homepage.
 *
 * @author Matthew McMillan
 * @author Mitch Gaines
 * @author Joe Turcotte
 * @author Cormac Lynch-Collier
 * @author Will Lucca
 * @version 1.3, April 13, 2018
 */
public class HomePageController {

    /**
     * Stores the boolean for whether or not the stairs option has been selected.
     */
    private static boolean include_stairs = false;

    /**
     * Stores the default location for the Kiosk.
     */
    private static String KIOSK_DEFAULT_LOCATION = "BINFO00102";

    @FXML
    JFXButton pathfind;

    @FXML
    JFXButton login_btn;

    @FXML
    JFXButton about;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Label wrong_credentials;

    @FXML
    Label time, time2;

    @FXML
    BorderPane main_pane;

    @FXML
    JFXComboBox<String> combobox_start;

    @FXML
    JFXComboBox<String> combobox_end;

    @FXML
    JFXComboBox<String> language_selector;

    @FXML
    StackPane stack_pane;

    @FXML
    ExpansionPanel exp_panel;
    @FXML
    JFXButton REST;
    @FXML
    JFXButton EXIT;
    @FXML
    JFXButton INFO;
    @FXML
    JFXToggleButton stairs_toggle;
    @FXML
    ImageView minimap;
    @FXML
    JFXButton map;
    @FXML
    Text use_map;

    /**
     * Stores the LoginHandler.
     */
    private LoginHandler loginHandler;

    /**
     * Stores the ObservableList for node locations.
     */
    private ObservableList<edu.wpi.cs3733d18.teamS.data.Node> locations = FXCollections.observableArrayList();

    /**
     * Stores the FuzzyComboBox associated with the start location ComboBox.
     */
    private FuzzyComboBox auto_combobox_start;

    /**
     * Stores the FuzzyComboBox associated with the end location ComboBox.
     */
    private FuzzyComboBox auto_combobox_end;

    /**
     * Retrieves whether or not stairs are included.
     *
     * @return The boolean value of include_stairs.
     */
    public static boolean includeStairs() {
        return include_stairs;
    }

    /**
     * Sets the Kiosk's default location.
     *
     * @param kioskDefaultLocation the default location for the kiosk.
     */
    public static void setKioskDefaultLocation(String kioskDefaultLocation) {
        KIOSK_DEFAULT_LOCATION = kioskDefaultLocation;
    }

//    public void setStart(String node_id) {
//        combobox_start.getSelectionModel().select(Storage.getInstance().getNodeByID(node_id));
//    }

    public void setEnd(String node_id) {
        combobox_end.getSelectionModel().select(Storage.getInstance().getNodeByID(node_id).toString());
    }

    /**
     * Performs this function during creation of Controller sets up the ComboBoxes
     * by pulling all nodes from the edu.wpi.cs3733d18.teamS.database.
     */
    public void initialize() {
        HomePageController.setKioskDefaultLocation(Storage.getInstance().getDefaultKioskLocation());
        locations.addAll(Storage.getInstance().getAllNodes());

        ArrayList<edu.wpi.cs3733d18.teamS.data.Node> to_remove = new ArrayList<>();
        for (edu.wpi.cs3733d18.teamS.data.Node location : locations) {
            if (location.getNodeType().equals("HALL") ||
                    location.getNodeType().equals("ELEV") ||
                    location.getNodeType().equals("STAI") ||
                    location.getShortName().equals("TRAIN")) {

                to_remove.add(location);
            }
        }
        locations.removeAll(to_remove);
        locations.sort(Comparator.comparing(edu.wpi.cs3733d18.teamS.data.Node::getLongName));
        int default_index = 0;
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getNodeID().equals(KIOSK_DEFAULT_LOCATION)) {
                default_index = i;
                break;
            }
        }

        auto_combobox_start = new FuzzyComboBox(combobox_start, locations);
        auto_combobox_end = new FuzzyComboBox(combobox_end, locations);
        combobox_start.setValue(locations.get(default_index).toString());

        loginHandler = new LoginHandler();

        updatedTime();

        language_selector.getItems().removeAll(language_selector.getItems());
        for (String language : AllText.getLanguages()) {
            language_selector.getItems().add(AllText.get(language));
        }

        login_btn.defaultButtonProperty().bind(Bindings.or(username.focusedProperty(), password.focusedProperty()));
    }

    /**
     * Updates clock to live time
     */
    public void updatedTime() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();

            time.setText(dtf.format(now));
            time2.setText(dtf.format(now));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * Changes the language of the scenes.
     */
    public void onLanguageChange() {
        if (language_selector.getSelectionModel().isEmpty()) {
            return;
        }
        AllText.changeLanguage(AllText.getLanguages()[language_selector.getSelectionModel().getSelectedIndex()]);
        Main.switchScenes("Brigham and Women's", "/HomePage.fxml");

    }

    /**
     * Sets start_loc and end_loc to the values selected in the combobox, then switches view to
     * PathfindPage, initializing PathfindController.
     *
     * @param event the click of the mouse on the button.
     * @throws IOException
     */
    @FXML
    void onPathfindClick(ActionEvent event) throws IOException {
        if (auto_combobox_start.getValue() == null || auto_combobox_end.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Please select a starting and ending location");
            alert.setHeaderText("Please select a starting and ending location");
            alert.setContentText("You must select both a starting and ending location to get directions.");
            alert.showAndWait();
        } else if (auto_combobox_start.getValue().equals(auto_combobox_end.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Your starting and ending locations can't be the same");
            alert.setHeaderText("Your starting and ending locations can't be the same");
            alert.setContentText("You must select a starting and ending location that are different from each other.");
            alert.showAndWait();
        }
        include_stairs = stairs_toggle.isSelected();

        SearchAlgorithm alg;
        int select = AdminSpecialOptionsController.getChoosenAlg();
        if (select == 1) {
            alg = new Dijkstras();
        } else if (select == 2) {
            alg = new DepthFirst();
        } else if (select == 3) {
            alg = new BreadthFirst();
        } else {
            alg = new AStar();
        }
        Pathfinder finder = new Pathfinder(alg);
        finder.findShortestPath(auto_combobox_start.getValue().getNodeID(), auto_combobox_end.getValue().getNodeID());
        if(finder.pathfinder_path.getAStarNodePath().size() <= 1){
            return;
        }
        Map.path = finder.pathfinder_path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    /**
     * Does the Quick location pathfinding and displays a message if the user is already at the quick location area.
     *
     * @param event Clicking the button.
     * @throws IOException
     */
    @FXML
    void onQuickClick(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        if (auto_combobox_start.getValue().getNodeID().contains(button.getId())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Can't Get Directions");
            alert.setHeaderText("You have specified a location, however you are already there");
            alert.setContentText("Reason: You are already at the type location you have specified, please choose another quick-select location");
            alert.showAndWait();
            return;
        }

        Pathfinder quick_finder = new Pathfinder(new Dijkstras());
        quick_finder.findShortestPath(auto_combobox_start.getValue().getNodeID(), button.getId());
        Path path = quick_finder.pathfinder_path;
        if (path.getAStarNodePath().size() <= 1) {
            return;
        }
        Map.path = path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    @FXML
    void onAboutClick(ActionEvent event) {    // about screen
        Main.switchScenes("About", "/AboutPage.fxml");
    }
    
    @FXML
    void onCreditsClick(ActionEvent event) {    // about screen
        Main.switchScenes("About", "/AboutPage.fxml");
    }

    //THIS IS A TEST TO TRY OUT DIFFERENT USERS

    /**
     * Tests to try out different users.
     *
     * @param event The click.
     * @throws IOException the exception thrown when the program fails to read or write a file.
     */
    public void onLoginClick(ActionEvent event) throws IOException {
        try {
            User user = loginHandler.login(username.getText(), password.getText());
            switch (user.getType()) {
                case DOCTOR:
                    openUser(event, "/DoctorPage.fxml", user);
                    break;
                case ADMIN_STAFF:
                    openUser(event, "/AdminPage.fxml", user);
                    break;
                case REGULAR_STAFF:
                    openUser(event, "/RegStaffPage.fxml", user);
                    break;
            }
        } catch (InvalidUsernameException e) {
            wrong_credentials.setText("Wrong username or password");
        }
    }

    public void onMapClick() {
        Main.switchScenes("HomepageMap", "/HomepageMap.fxml");
    }

    //PART OF THE USER TEST
    public void openUser(ActionEvent event, String page, User user) throws IOException {
        UserController user_controller = (UserController) Main.switchScenes("User", page);
        user_controller.setUp(user, page);
    } //END OF TEST

    public void mouseEnter() {
        minimap.setOpacity(.6);
        use_map.setOpacity(.6);
    }

    public void mouseExit() {
        minimap.setOpacity(.9);
        use_map.setOpacity(.9);
    }
}
