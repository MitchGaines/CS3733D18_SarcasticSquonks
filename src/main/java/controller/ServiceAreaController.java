package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        request_type_selector.getItems().removeAll();
        request_type_selector.getItems().addAll(ServiceType.getServiceTypes());
    }

    public void populateRequestsBox() {
        active_requests_box.getItems().removeAll();
        for (ServiceRequest sr : ServiceRequest.getUnfulfilledServiceRequests()) {
            if (sr.getServiceType().getFulfillers().contains(user)) {
                active_requests_box.getItems().add(sr);
            }
        }
    }

    @FXML
    Button requestServiceButton;

    public void markComplete() {
        if (active_requests_box.getSelectionModel().getSelectedItem() != null) {
            active_requests_box.getSelectionModel().getSelectedItem().fulfill(user);
        }
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

        System.out.println("Service requested");
        populateRequestsBox();

    }

    @FXML
    private TextField service_title, service_location;

    @FXML
    private TextArea description_field;

    public void setUser(User user) {
        this.user = user;
    }
}
