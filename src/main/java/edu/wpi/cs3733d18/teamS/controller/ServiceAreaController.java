package edu.wpi.cs3733d18.teamS.controller;


import edu.wpi.cs3733d18.teamR.RaikouAPI;
import edu.wpi.cs3733d18.teamR.ServiceException;
import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.pathfind.*;
import edu.wpi.cs3733d18.teamS.service.ServiceLogEntry;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.Comparator;
import java.util.stream.Stream;

import edu.wpi.cs3733d18.SquonksAPI.controller.SquonksAPI;

public class ServiceAreaController {

    @FXML
    ComboBox<ServiceType> request_type_selector = new ComboBox<>();

    @FXML
    ComboBox<ServiceRequest> active_requests_box = new ComboBox<>();

    @FXML
    ComboBox<edu.wpi.cs3733d18.teamS.data.Node> service_location = new ComboBox<>();

    @FXML
    ComboBox<User> fulfiller_box = new ComboBox<>();

    @FXML
    Button request_service_button, pathfind_button;
    @FXML
    Button mark_completed_btn;
    private User user;
    private ObservableList<Node> locations = FXCollections.observableArrayList();
    @FXML
    private Text title_text, location_text;
    @FXML
    private TextField service_title;
    @FXML
    private TextArea description_field, description_text;
    private UserController parent;

    public void populateRequestTypes() {
        request_type_selector.valueProperty().set(null);
        request_type_selector.getItems().removeAll(request_type_selector.getItems());
        request_type_selector.getItems().addAll(Storage.getInstance().getAllServiceTypes());
        service_location.setConverter(new StringConverter<Node>() {
            @Override
            public String toString(edu.wpi.cs3733d18.teamS.data.Node node) {
                if (node == null) {
                    return "";
                } else {
                    return node.toString();
                }
            }

            @Override
            public edu.wpi.cs3733d18.teamS.data.Node fromString(String long_name) {
                for (edu.wpi.cs3733d18.teamS.data.Node node : locations) {
                    if (node.getLongName().equals(long_name)) {
                        return node;
                    }
                }
                return null;
            }
        });
        locations.sort(Comparator.comparing(Node::getLongName));
        service_location.setItems(locations);
    }

    public void populateRequestsBox() {
        if (active_requests_box == null) {
            return;
        }
        parent.declareOrClearEmergency();
        boolean emergency_declared = false;
        active_requests_box.valueProperty().set(null);
        active_requests_box.getItems().removeAll(active_requests_box.getItems());
        for (ServiceRequest sr : ServiceRequest.getUnfulfilledServiceRequests()) {
            if ((sr.getServiceType().getFulfillers().contains(user) && sr.getDesiredFulfiller() == null) ||

                    (sr.getDesiredFulfiller() != null && sr.getDesiredFulfiller().getUsername().equals(user.getUsername()))) {
                active_requests_box.getItems().add(sr);
                parent.declareOrClearEmergency();
            }
        }
    }

    public void markComplete() {
        ServiceRequest selected = active_requests_box.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.fulfill(user);

            populateRequestsBox();

            title_text.setVisible(false);
            location_text.setVisible(false);
            description_text.setVisible(false);
            mark_completed_btn.setVisible(false);
        }

        ServiceLogEntry.log(selected, true);

    }

    private static User ANY_FULFILLER = new User(null, "", "Any fulfiller", "", null, false);

    public void onTypeSelect() {
        ServiceType selected_item = request_type_selector.getSelectionModel().getSelectedItem();
        if (selected_item != null) {
            fulfiller_box.getItems().removeAll(fulfiller_box.getItems());
            fulfiller_box.getItems().add(ANY_FULFILLER);
            fulfiller_box.getItems().addAll(selected_item.getFulfillers());
        }
        fulfiller_box.getSelectionModel().select(ANY_FULFILLER);
        fulfiller_box.setVisible(true);
    }

    public void doPathFind() {
        ServiceRequest service_request = active_requests_box.getSelectionModel().getSelectedItem();
        if (service_request == null) {
            return;
        }
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
        finder.findShortestPath(Storage.getInstance().getDefaultKioskLocation(), service_request.getLocation().getNodeID());
        if(finder.pathfinder_path.getAStarNodePath().size() <= 1){
            return;
        }
        Map.path = finder.pathfinder_path;
        Main.switchScenes("Pathfinder", "/PathfindPage.fxml");
    }

    public void doRequestService() {
        Storage storage = Storage.getInstance();
        if (request_type_selector.getSelectionModel().getSelectedItem() == null || fulfiller_box.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        User selected_user = fulfiller_box.getSelectionModel().getSelectedItem();
        User desired_user = selected_user == ANY_FULFILLER ? null : selected_user;
        ServiceRequest sr =
                ServiceRequest.createService(
                        service_title.getText(),
                        description_field.getText(),
                        request_type_selector.getSelectionModel().getSelectedItem(),
                        user,
                        service_location.getValue(),
                        desired_user);
        storage.saveRequest(sr);
        ServiceLogEntry.log(sr, false);
        populateRequestsBox();

        service_title.setText("");
        service_location.getEditor().setText("");
        description_field.setText("");
        populateRequestTypes();

        // hide fulfiller box
        fulfiller_box.setVisible(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service Request Created");
        alert.setHeaderText("Service Request Created");
        alert.setContentText("Your service request was created successfully.");
        alert.showAndWait();

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void loadRequestInfo() {

        ServiceRequest selected_request = active_requests_box.getSelectionModel().getSelectedItem();

        if (selected_request == null) {
            return;
        }

        title_text.setText(selected_request.getTitle());
        description_text.setText(selected_request.getDescription());
        location_text.setText(selected_request.getLocation().getShortName());

        title_text.setVisible(true);
        location_text.setVisible(true);
        description_text.setVisible(true);
        mark_completed_btn.setVisible(true);
        pathfind_button.setVisible(true);
    }

    public void setUpToComplete(User user) {
        this.user = user;
        title_text.setVisible(false);
        location_text.setVisible(false);
        description_text.setVisible(false);
        mark_completed_btn.setVisible(false);
        pathfind_button.setVisible(false);
    }

    public void setUpToMake(User user) {
        this.user = user;
        locations.addAll(Storage.getInstance().getAllNodes());
    }

    public void initialize() {
        if (fulfiller_box != null) {
            fulfiller_box.setVisible(false);
        }
    }

    /**
     * Autocomplete algorithm which sets the displayed items of a ComboBox to be only the ones that include the text
     * in the edit field as a substring.
     *
     * @param e KeyEvent representing the key that was typed.
     */
    @FXML
    void onKeyReleasedComboBox(KeyEvent e) {
        ComboBox<edu.wpi.cs3733d18.teamS.data.Node> combo_box = (ComboBox<edu.wpi.cs3733d18.teamS.data.Node>) (e.getSource());
        ObservableList<edu.wpi.cs3733d18.teamS.data.Node> filteredItems = FXCollections.observableArrayList();
        combo_box.show();
        TextField editor = combo_box.getEditor();

        if (editor.getText().equals("")
                || e.getCode() == KeyCode.BACK_SPACE
                || e.getCode() == KeyCode.DELETE
                || (e.getCode().isLetterKey() && editor.getCaretPosition() < editor.getText().length())) {
            combo_box.setItems(locations);
        }

        Stream<Node> items_stream = combo_box.getItems().stream();
        String user_text = editor.getText().toLowerCase();
        items_stream.filter(el -> el.toString().toLowerCase().contains(user_text)).forEach(filteredItems::add);

        if (!e.getCode().isArrowKey()) { // Doesn't change list while edu.wpi.cs3733d18.teamS.user is navigating dropdown with arrow keys
            if (e.getCode() == KeyCode.ENTER) {
                // Pressing enter clear the edit field if it was autocompleted. This prevents
                // that by storing the editor text and putting it back in afterwards
                String current_editor = editor.getText();
                combo_box.setItems(filteredItems);
                editor.setText(current_editor);
            } else {
                combo_box.setItems(filteredItems);
            }

            // Resize drop down
            int new_visible_size;
            new_visible_size = filteredItems.size() > 10 ? 10 : filteredItems.size();
            if (new_visible_size != combo_box.getVisibleRowCount()) {
                combo_box.hide();
                combo_box.setVisibleRowCount(new_visible_size);
                combo_box.show();
            }
        }
    }

    public void setParent(UserController user_controller) {
        parent = user_controller;
        //parent.declareOrClearEmergency();
    }

    @FXML
    Button prescription_request;

    public void prescriptionRequestClick() {
        Timeout.stop();
        RaikouAPI raikouAPI = new RaikouAPI();
        try {
            raikouAPI.run(100, 30, 900, 600, null, null, null);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        Timeout.start();
    }

}
