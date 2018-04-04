package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import service.ServiceLogEntry;
import service.ServiceRequest;
import service.ServiceType;
import user.User;

public class ServiceAreaController {
//
    @FXML
    ComboBox<ServiceType> request_type_selector;

    @FXML
    ComboBox<ServiceRequest> active_requests_box;

    private User user;

    public void populateRequestTypes() {
        request_type_selector.valueProperty().set(null);
        request_type_selector.getItems().removeAll(request_type_selector.getItems());
        request_type_selector.getItems().addAll(ServiceType.getServiceTypes());
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

    @FXML
    Button request_service_button;

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
                service_location.getText());

        populateRequestsBox();

        service_title.setText("");
        service_location.setText("");
        description_field.setText("");
        populateRequestTypes();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Service Request Created");
        alert.setHeaderText("Service Request Created");
        alert.setContentText("Your service request was created successfully.");
        alert.showAndWait();

    }


    @FXML
    private Text title_text, location_text;

    @FXML
    private TextField service_title, service_location;

    @FXML
    private TextArea description_field, description_text;

    @FXML
     Button mark_completed_btn;

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
        location_text.setText(selected_request.getLocation());

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
    }

    private UserController parent;

    public void setParent(UserController user_controller) {
        parent = user_controller;
    }

}
