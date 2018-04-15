package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.database.Storage;
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
import java.util.HashSet;
import java.util.stream.Stream;

public class ServiceAreaController {

    @FXML
    ComboBox<ServiceType> request_type_selector;

    @FXML
    ComboBox<ServiceRequest> active_requests_box;

    @FXML
    ComboBox<edu.wpi.cs3733d18.teamS.data.Node> service_location;
    @FXML
    Button request_service_button;
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
        parent.dismissEmergency();
        boolean emergency_declared = false;
        active_requests_box.valueProperty().set(null);
        active_requests_box.getItems().removeAll(active_requests_box.getItems());
        for (ServiceRequest sr : ServiceRequest.getUnfulfilledServiceRequests()) {
            if (sr.getServiceType().getFulfillers().contains(user)) {
                active_requests_box.getItems().add(sr);
                if (sr.getServiceType().isEmergency() && !emergency_declared) {
                    emergency_declared = true;
                    parent.declareEmergency(sr.getTitle(), sr.getLocation(), sr.getDescription());
                }
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

    public void doRequestService() {
        // todo: validation 4real
        if (request_type_selector.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        ServiceRequest.createService(
                service_title.getText(),
                description_field.getText(),
                request_type_selector.getSelectionModel().getSelectedItem(),
                user,
                service_location.getValue());

        populateRequestsBox();

        service_title.setText("");
        service_location.getEditor().setText("");
        description_field.setText("");
        populateRequestTypes();


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
    }

    public void initialize() {
        title_text.setVisible(false);
        location_text.setVisible(false);
        description_text.setVisible(false);
        mark_completed_btn.setVisible(false);
        locations.addAll(Storage.getInstance().getAllNodes());
        request_service_button.setText("Request service");
        //populateRequestsBox();
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
        parent.dismissEmergency();
    }

}
