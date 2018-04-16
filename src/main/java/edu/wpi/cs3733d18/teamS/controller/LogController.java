package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.service.ServiceLogEntry;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Class that manages service request logs and methods related to them.
 * @author Matthew McMillan
 * @author Mitch Gaines
 * @author Danny Sullivan
 * @author Cormac Lynch-Collier
 * @version %I%, %G%
 *
 */
public class LogController {

    /**
     * The table for user data.
     */
    @FXML
    TableView log_table;

    /**
     * The Time display.
     */
    @FXML
    Label time;

    /**
     * The Border Pane
     */
    @FXML
    BorderPane main_pane;

    /**
     * The Username.
     */
    @FXML
    Text user_name;

    /**
     * Stores the User.
     */
    private User user;

    /**
     * Stores the text for the return page.
     */
    private String return_page;

    /**
     * Initializes and formats the log table.
     */
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

    /**
     * Populates the table with information.
     */
    public void populateTable() {
        if (user.getType() == User.user_type.ADMIN_STAFF) {
            log_table.setItems(FXCollections.observableArrayList(ServiceLogEntry.getOverallLog()));
        } else {
            log_table.setItems(FXCollections.observableArrayList(ServiceLogEntry.getLogForUser(user)));
        }

    }

    /**
     * TODO Figure out what these do exactly
     * @param user
     * @param return_page
     */
    public void setUp(User user, String return_page) {
        setUser(user);
        user_name.setText(user.getUsername());
        setReturnPage(return_page);
        populateTable();
    }

    /**
     * Sets the user.
     * @param user the user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the return page.
     * @param page the return page.
     */
    private void setReturnPage(String page) {
        return_page = page;
    }

    /**
     * TODO Figure out what this does
     * @param event
     * @throws IOException
     */
    public void onBackClick(ActionEvent event) throws IOException {
        UserController user_controller = (UserController) Main.switchScenes("User", return_page);
        user_controller.setUp(user, return_page);

    }

}
