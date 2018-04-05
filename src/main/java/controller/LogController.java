package controller;

import internationalization.AllText;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import service.ServiceLogEntry;
import user.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class LogController {

    @FXML
    TableView log_table;

    public void initialize() {

        log_table.getColumns().removeAll(log_table.getColumns());

        TableColumn<ServiceLogEntry, String> nameCol = new TableColumn("Service Request");
        TableColumn<ServiceLogEntry, String> typeCol = new TableColumn("Service Type");
        TableColumn<ServiceLogEntry, String> statusCol = new TableColumn("Action");
        TableColumn<ServiceLogEntry, String> userCol = new TableColumn("User");
        TableColumn<ServiceLogEntry, String> timeCol = new TableColumn("Time");

        nameCol.prefWidthProperty().bind(log_table.widthProperty().multiply(0.2));
        typeCol.prefWidthProperty().bind(log_table.widthProperty().multiply(0.2));
        statusCol.prefWidthProperty().bind(log_table.widthProperty().multiply(0.2));
        userCol.prefWidthProperty().bind(log_table.widthProperty().multiply(0.2));
        timeCol.prefWidthProperty().bind(log_table.widthProperty().multiply(0.2));

        log_table.getColumns().addAll(nameCol, typeCol, statusCol, userCol, timeCol);

        nameCol.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            p.set(e.getValue().getService_request().getTitle());
            return p;
        });

        statusCol.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            p.set(e.getValue().isCompleted() ? "Completed" : "Requested");
            return p;
        });

        userCol.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            if (e.getValue().isCompleted()) {
                p.set(e.getValue().getService_request().getFulfiller().getUsername());
            } else {
                p.set(e.getValue().getService_request().getRequester().getUsername());
            }
            return p;
        });

        timeCol.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            DateFormat date_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            p.setValue(date_format.format(e.getValue().getTime()));
            //date_format.fo
            //p.setValue("sometime");
            return p;
        });

        typeCol.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            p.setValue(e.getValue().getService_request().getServiceType().getName());
            return p;
        });



    }

    public void populateTable() {
        if (user.getType() == User.user_type.ADMIN_STAFF) {
            log_table.setItems(FXCollections.observableArrayList(ServiceLogEntry.getOverallLog()));
        } else {
            log_table.setItems(FXCollections.observableArrayList(ServiceLogEntry.getLogForUser(user)));
        }

    }

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    private String return_page;

    public void setReturnPage(String page) {
        return_page = page;
    }

    public void onBackClick(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(return_page), AllText.getBundle());
        Parent user_parent = (Parent)loader.load();
        UserController controller = loader.<UserController>getController();
        controller.setUser(user);
        controller.setPage(return_page);
        controller.populateBoxes();
        Scene user_scene = new Scene(user_parent);
        Stage user_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user_stage.setTitle("User");
        user_stage.setScene(user_scene);
        user_stage.show();
    }

}
