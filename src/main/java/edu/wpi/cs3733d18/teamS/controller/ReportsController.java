package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.*;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class ReportsController extends UserController {
    private static final String[] REPORT_TYPE_KEYS = {"num_requests_report", "average_time_report"};
    private static final String[] REPORT_TYPE_UNIT_KEYS = {"num_requests_unit", "hours_unit"};
    @FXML
    JFXTimePicker start_time_picker, end_time_picker;
    @FXML
    JFXDatePicker start_date_picker, end_date_picker;
    @FXML
    BarChart<String, Number> bar_chart;
    @FXML
    JFXListView<ServiceType> service_type_list;
    @FXML
    JFXComboBox<String> report_type_menu;
    @FXML
    JFXButton run_report_button;
    @FXML
    JFXToggleButton chart_toggle;
    @FXML
    Label instructions;
    @FXML
    Text user_name;
    private boolean empty = true;
    private BarChart chart;
    private TableView table;

    @Override
    public void initialize() {
        populateReportTypes();
        setUpListView();
        instructions.setWrapText(true);
        service_type_list.getSelectionModel().selectedItemProperty().addListener(e -> onGoButton());
    }

    private void populateReportTypes() {
        report_type_menu.getItems().removeAll(report_type_menu.getItems());
        //todo: i18n
        for (String key : REPORT_TYPE_KEYS) {
            report_type_menu.getItems().add(AllText.get(key));
        }
    }

    private void setUpListView() {
        service_type_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        service_type_list.getItems().addAll(ServiceType.getServiceTypes());
    }

    private DateTime javaToJoda(java.time.LocalDateTime java_time) {
        return new org.joda.time.DateTime(java_time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private org.joda.time.DateTime getStartTime() {
        if (start_date_picker.getValue() == null || start_time_picker.getValue() == null) {
            return null;
        }
        LocalDate date = start_date_picker.getValue();
        LocalTime time = start_time_picker.getValue();
        java.time.LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
    }

    private DateTime getEndTime() {
        if (end_date_picker.getValue() == null || end_time_picker.getValue() == null) {
            return null;
        }
        LocalDate date = end_date_picker.getValue();
        LocalTime time = end_time_picker.getValue();
        java.time.LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
    }

    public void onGoButton() {
        generateChart();
        generateTable();
        onToggle();
    }

    public void onToggle() {
        if (!empty) {
            if (chart_toggle.selectedProperty().getValue()) {
                main_pane.setCenter(chart);
            } else {
                main_pane.setCenter(table);
            }
        }
    }

    public void generateChart() {
        if (getStartTime() == null || getEndTime() == null || report_type_menu.getSelectionModel().isEmpty()) {
            return;
        }
        int selected_report_type = report_type_menu.getSelectionModel().getSelectedIndex();
        final CategoryAxis x_axis = new CategoryAxis();
        final NumberAxis y_axis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(x_axis, y_axis);
        chart.setTitle(AllText.get(REPORT_TYPE_KEYS[selected_report_type]));
        x_axis.setLabel(AllText.get("service_type"));
        y_axis.setLabel(AllText.get(REPORT_TYPE_UNIT_KEYS[selected_report_type]));


        for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {

            XYChart.Series series = new XYChart.Series();
            if (selected_report_type == 0) {
                series.getData().add(new XYChart.Data(type.getName(), type.getNumRequests(getStartTime(), getEndTime())));
            } else if (selected_report_type == 1) {
                series.getData().add(new XYChart.Data(type.getName(), type.getAverageFulfillmentTimeInHours(getStartTime(), getEndTime())));
            }
            chart.getData().add(series);
        }

        chart.setLegendVisible(false);

        this.chart = chart;
        empty = false;
    }

    public void generateTable() {
        if (getStartTime() == null || getEndTime() == null || report_type_menu.getSelectionModel().isEmpty()) {
            return;
        }

        int selected = report_type_menu.getSelectionModel().getSelectedIndex();
        TableView<ServiceType> table = new TableView<>();

        TableColumn<ServiceType, String> service_type_col = new TableColumn<>("Service Type");
        TableColumn<ServiceType, String> value_col = new TableColumn<>(String.format("%s (%s)", AllText.get(REPORT_TYPE_KEYS[selected]), AllText.get(REPORT_TYPE_UNIT_KEYS[selected])));
        table.getColumns().addAll(service_type_col, value_col);

        service_type_col.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            p.setValue(e.getValue().getName());
            return p;
        });

        value_col.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            if (selected == 0) {
                p.setValue("" + e.getValue().getNumRequests(getStartTime(), getEndTime()));
            } else if (selected == 1) {
                p.setValue("" + e.getValue().getAverageFulfillmentTimeInHours(getStartTime(), getEndTime()));
            }
            return p;
        });


        service_type_col.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        value_col.prefWidthProperty().bind(table.widthProperty().multiply(0.6));

        for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {
            table.getItems().add(type);
        }
        empty = false;
        this.table = table;
    }
}
