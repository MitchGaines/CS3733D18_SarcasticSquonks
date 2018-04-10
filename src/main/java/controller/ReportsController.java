package controller;

import com.jfoenix.controls.*;
import internationalization.AllText;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SelectionMode;
import org.joda.time.DateTime;
import service.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class ReportsController extends UserController {
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

    @Override
    public void initialize() {
        populateReportTypes();
        bar_chart.setVisible(false);
        setUpListView();
    }

    private void populateReportTypes() {
        report_type_menu.getItems().removeAll(report_type_menu.getItems());
        //todo: i18n
        for (String key : REPORT_TYPE_KEYS) {
            report_type_menu.getItems().add(AllText.get(key));
        }
    }

    private static final String[] REPORT_TYPE_KEYS = {"num_requests_report", "average_time_report"};
    private static final String[] REPORT_TYPE_UNIT_KEYS = {"num_requests_unit", "hours_unit"};


    private void setUpListView() {
        service_type_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        service_type_list.getItems().addAll(ServiceType.getServiceTypes());
    }

    private DateTime javaToJoda(java.time.LocalDateTime java_time) {
        return new org.joda.time.DateTime(java_time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private org.joda.time.DateTime getStartTime() {
        LocalDate date = start_date_picker.getValue();
        LocalTime time = start_time_picker.getValue();
        java.time.LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
    }


    private DateTime getEndTime() {
        LocalDate date = end_date_picker.getValue();
        LocalTime time = end_time_picker.getValue();
        java.time.LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
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


        main_pane.setCenter(chart);
        chart.setVisible(true);
    }
}
