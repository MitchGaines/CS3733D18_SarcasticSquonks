package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.*;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.service.Day;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import edu.wpi.cs3733d18.teamS.user.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import javax.sound.sampled.Line;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.floor;

public class ReportsController extends UserController {
    private static final String[] REPORT_TYPE_KEYS = {"num_requests_report", "average_time_report", "user_breakdown_report", "user_fulfillment_report", "user_timespent_report", "num_requests_daily", "num_fulfills_daily"};
    private static final String[] REPORT_TYPE_UNIT_KEYS = {"num_requests_unit", "hours_unit", "requests_unit", "requests_unit", "hours_unit", "requests_unit", "requests_unit"};
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
    JFXToggleButton chart_toggle;
    @FXML
    Label instructions;
    @FXML
    Text user_name;
    private boolean empty = true;
    private BarChart main_bar_chart;
    private LineChart main_line_chart;
    private PieChart main_pie_chart;
    private TableView table;

    @Override
    public void initialize() {
        populateReportTypes();
        setUpListView();
        instructions.setWrapText(true);
        service_type_list.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ServiceType>() {
            @Override
            public void onChanged(Change<? extends ServiceType> c) {
                onGoButton();
            }
        });
        start_date_picker.setValue(LocalDate.now().minusDays(7));
        end_date_picker.setValue(LocalDate.now());
        start_time_picker.setValue(LocalTime.now());
        end_time_picker.setValue(LocalTime.now());
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
        service_type_list.getItems().addAll(Storage.getInstance().getAllServiceTypes());
    }

    private DateTime javaToJoda(LocalDateTime java_time) {
        return new DateTime(java_time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private DateTime getStartTime() {
        if (start_date_picker.getValue() == null || start_time_picker.getValue() == null) {
            return null;
        }
        LocalDate date = start_date_picker.getValue();
        LocalTime time = start_time_picker.getValue();
        LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
    }

    private DateTime getEndTime() {
        if (end_date_picker.getValue() == null || end_time_picker.getValue() == null) {
            return null;
        }
        LocalDate date = end_date_picker.getValue();
        LocalTime time = end_time_picker.getValue();
        LocalDateTime ldt = date.atTime(time);
        return javaToJoda(ldt);
    }

    public void onGoButton() {
        main_bar_chart = null;
        main_pie_chart = null;
        main_line_chart = null;
        generateBarChart();
        generateRightPieChart();
        generateLineChart();
        generateTable();
        onToggle();
    }

    public void onToggle() {
        if (!empty) {
            if (chart_toggle.selectedProperty().getValue()) {
                if (main_bar_chart != null) {
                    main_pane.setCenter(main_bar_chart);
                    System.out.println("Setting bar chart to center...");
                } else if (main_pie_chart != null) {
                    main_pane.setCenter(main_pie_chart);
                    System.out.println("Setting pie chart to center...");
                } else if (main_line_chart != null) {
                    main_pane.setCenter(main_line_chart);
                    System.out.println("using line chart...");
                } else {
                    System.out.println("all were null???");
                }
            } else {
                main_pane.setCenter(table);
            }
        }
    }

    private void generateRightPieChart() {
        switch (report_type_menu.getSelectionModel().getSelectedIndex()) {
            case 2:
                generatePieChart((start, end, type) -> {
                    return type.getRequestBreakdownByUser(start, end);
                });
                break;
            case 3:
                generatePieChart((start, end, type) -> type.getFulfillmentBreakdownByUser(start, end));
                break;
            case 4:
                generatePieChart((start, end, type) -> type.getTimeSpentByUser(start, end));
                break;
            default:
                main_pie_chart = null;
        }
    }

    private HashMap<User, Number> getPieHashmap(TriFunction<DateTime, DateTime, ServiceType, HashMap<User, Number>> data_source) {
        HashMap<User, Number> users_and_requests = new HashMap<>();
        for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {
            HashMap<User, Number> curr_hashmap = data_source.apply(getStartTime(), getEndTime(), type);
            //System.out.println("CurrHashMap size: " + curr_hashmap.size());
            for (User user : curr_hashmap.keySet()) {
                if (users_and_requests.containsKey(user)) {
                    users_and_requests.put(user, users_and_requests.get(user).doubleValue() + curr_hashmap.get(user).doubleValue());
                } else {
                    users_and_requests.put(user, curr_hashmap.get(user));
                }
            }
        }
        return users_and_requests;
    }

    private void generatePieChart(TriFunction<DateTime, DateTime, ServiceType, HashMap<User, Number>> data_source) {
        if (getStartTime() == null || getEndTime() == null || report_type_menu.getSelectionModel().isEmpty()) {
            return;
        }

        ObservableList<PieChart.Data> pie_chart_data = FXCollections.observableArrayList();
        HashMap<User, Number> users_and_requests = getPieHashmap(data_source);


        users_and_requests.keySet().forEach(e -> {
            if (users_and_requests.get(e).doubleValue() > 0.01) {
                pie_chart_data.add(new PieChart.Data(e.toString(), users_and_requests.get(e).doubleValue()));
            }
        });


        pie_chart_data.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " - ", numToString(data.getPieValue())
                        )
                )
        );


        PieChart pie_chart = new PieChart(pie_chart_data);


        this.main_pie_chart = pie_chart;

        empty = false;
    }

    private void generateLineChart() {
        int selected = report_type_menu.getSelectionModel().getSelectedIndex();
        switch (selected) {
            case 5:
                generateRightLineChart((start, end, type) -> type.getNumRequests(start, end));
                break;
            case 6:
                generateRightLineChart((start, end, type) -> type.getFulfilledInRange(start, end));
                break;
            default:
                main_line_chart = null;
        }
    }

    private void generateRightLineChart(TriFunction<DateTime, DateTime, ServiceType, Number> data_source) {
        if (getStartTime() == null || getEndTime() == null || report_type_menu.getSelectionModel().isEmpty()) {
            return;
        }


        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> line_chart = new LineChart<>(xAxis, yAxis);

        for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {
            XYChart.Series series = new XYChart.Series();

            int day_count = 1;
            for (long i = getStartTime().getMillis(); i < getEndTime().getMillis(); i += DateTimeConstants.MILLIS_PER_DAY) {
                long j = (i + DateTimeConstants.MILLIS_PER_DAY <= getEndTime().getMillis() ? i + DateTimeConstants.MILLIS_PER_DAY : getEndTime().getMillis());
                Number result = data_source.apply(new DateTime(i), new DateTime(j), type);
                series.getData().add(new XYChart.Data(day_count, result));
                day_count++;
            }
            series.setName(type.getName());
            line_chart.getData().add(series);
        }


        this.main_line_chart = line_chart;
        empty = false;
    }

    private String numToString(double n) {
        if (floor(n) == n) {
            return (int) n + "";
        } else {
            return (floor(n * 100) / 100) + "";
        }
    }

    public void generateBarChart() {
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


        XYChart.Series series = new XYChart.Series();
        ArrayList<XYChart.Data> data_list = new ArrayList<>();

        for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {
            if (type == null) {
                continue;
            }
            if (selected_report_type == 0) {
                XYChart.Data data = new XYChart.Data(type.getName(), type.getNumRequests(getStartTime(), getEndTime()));
                data_list.add(data);
                series.getData().add(data);
            } else if (selected_report_type == 1) {
                String type_name = type.getName();
                Double avg_time = type.getAverageFulfillmentTimeInHours(getStartTime(), getEndTime());
                XYChart.Data data = new XYChart.Data(type_name, avg_time);
                series.getData().add(data);
                data_list.add(data);
            } else {
                main_bar_chart = null;
                return;
            }
        }


        chart.getData().add(series);


        for (int i = 0; i < data_list.size(); i++) {
            data_list.get(i).getNode().getStyleClass().add("default-color" + (1 + (i % 7)));
            //data_list.get(i).getNode().getStyleClass().add("default-color" + (i % 7));
        }

        chart.setLegendVisible(false);

        this.main_bar_chart = chart;
        empty = false;
    }

    public void generateTable() {
        if (getStartTime() == null || getEndTime() == null || report_type_menu.getSelectionModel().isEmpty()) {
            return;
        }

        int selected = report_type_menu.getSelectionModel().getSelectedIndex();
        TableView table = new TableView<>();

        TableColumn<Object, String> service_type_col = new TableColumn<>("Service Type");
        if (selected >= 2 && selected <= 4) {
            service_type_col.setText("User");
        } else if (selected == 5 || selected == 6) {
            service_type_col.setText("Service Type");
        }
        TableColumn<Object, String> value_col = new TableColumn<>(String.format("%s (%s)", AllText.get(REPORT_TYPE_KEYS[selected]), AllText.get(REPORT_TYPE_UNIT_KEYS[selected])));
        table.getColumns().addAll(service_type_col, value_col);

        service_type_col.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            p.setValue(e.getValue().toString());
            return p;
        });

        if (selected == 5 || selected == 6) {
            TableColumn<Object, String> new_col = new TableColumn<>("Day");
            new_col.setCellValueFactory(e -> {
                SimpleStringProperty p = new SimpleStringProperty();
                p.setValue(((Day)e.getValue()).getDay_num() + "");
                return p;
            });

            new_col.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
            table.getColumns().add(1, new_col);
        }

        value_col.setCellValueFactory(e -> {
            SimpleStringProperty p = new SimpleStringProperty();
            if (selected == 0) {
                p.setValue("" + ((ServiceType)e.getValue()).getNumRequests(getStartTime(), getEndTime()));
            } else if (selected == 1) {
                p.setValue("" + ((ServiceType)e.getValue()).getAverageFulfillmentTimeInHours(getStartTime(), getEndTime()));
            } else if (selected == 2) {
                HashMap<User, Number> hashmap = getPieHashmap((start, end, type) -> type.getRequestBreakdownByUser(getStartTime(), getEndTime()));
                if (hashmap.containsKey(e.getValue())) {
                    p.setValue(numToString(hashmap.get(e.getValue()).doubleValue()));
                } else {
                    p.setValue("0");
                }
            } else if (selected == 3) {
                HashMap<User, Number> hashmap = getPieHashmap((start, end, type) -> type.getFulfillmentBreakdownByUser(getStartTime(), getEndTime()));
                if (hashmap.containsKey(e.getValue())) {
                    p.setValue(numToString(hashmap.get(e.getValue()).doubleValue()));
                } else {
                    p.setValue("0");
                }
            } else if (selected == 4) {
                HashMap<User, Number> hashmap = getPieHashmap((start, end, type) -> type.getTimeSpentByUser(getStartTime(), getEndTime()));
                if (hashmap.containsKey(e.getValue())) {
                    p.setValue(numToString(hashmap.get(e.getValue()).doubleValue()));
                } else {
                    p.setValue("0");
                }
            } else if (selected == 5 || selected == 6) {
                p.setValue(((Day)e.getValue()).getNum_requests() + "");
            }
            return p;
        });


        service_type_col.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        value_col.prefWidthProperty().bind(table.widthProperty().multiply(0.6));

        if (selected == 0 || selected == 1) {
            for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {
                table.getItems().add(type);
            }
        } else if (selected >= 2 && selected <= 4) {
            table.getItems().addAll(Storage.getInstance().getAllUsers());
        } else if (selected == 5 || selected == 6) {
            TriFunction<DateTime, DateTime, ServiceType, Number> data_source;
            if (selected == 5) {
                data_source = ((start, end, type) -> type.getNumRequests(start, end));
            } else {
                data_source = ((start, end, type) -> type.getFulfilledInRange(start, end));
            }
            for (ServiceType type : service_type_list.getSelectionModel().getSelectedItems()) {

                int day_count = 1;
                for (long i = getStartTime().getMillis(); i < getEndTime().getMillis(); i += DateTimeConstants.MILLIS_PER_DAY) {
                    long j = (i + DateTimeConstants.MILLIS_PER_DAY <= getEndTime().getMillis() ? i + DateTimeConstants.MILLIS_PER_DAY : getEndTime().getMillis());
                    Number result = data_source.apply(new DateTime(i), new DateTime(j), type);
                    table.getItems().add(new Day(type.getName(), day_count, result.intValue()));
                    day_count++;
                }
            }
        }
        empty = false;
        this.table = table;
    }

    @FunctionalInterface
    interface TriFunction<A,B,C,R> {

        R apply(A a, B b, C c);

        default <V> TriFunction<A, B, C, V> andThen(
                Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }
    }


}
