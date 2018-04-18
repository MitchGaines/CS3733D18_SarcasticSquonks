package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.ApacheDatabase;
import edu.wpi.cs3733d18.teamS.database.CSVReader;
import edu.wpi.cs3733d18.teamS.database.CSVWriter;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * The main for the application.
 * @author Matthew McMillan
 * @author Mitch Gaines
 * @author Joe Turcotte
 * @author Matthew Puentes
 * @author Danny Sullivan
 * @author Noah Hillman
 * @version 1.3, April 13, 2018
 */
public class Main extends Application {

    private static Stage primary_stage;

    public static void main(String[] args) {
        launch(args);

        Timeout.stop();
    }

    /**
     * Switches Scenes.
     * @param title title of the next scene.
     * @param fxml_name name of the scene.
     * @return the next scene.
     */
    public static Object switchScenes(String title, String fxml_name) {
        try {
            double old_height = primary_stage.getHeight();
            double old_width = primary_stage.getWidth();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml_name), AllText.getBundle());
            Parent user_parent = loader.load();
            Object controller = loader.getController();
            Scene new_scene = new Scene(user_parent, primary_stage.getWidth(), primary_stage.getHeight());
            primary_stage.setTitle(title);

            Timeout.addListenersToScene(new_scene);

            primary_stage.setScene(new_scene);
            primary_stage.setHeight(old_height);
            primary_stage.setWidth(old_width);
            primary_stage.show();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the initial stage of the program.
     * @param primary_stage the initial setup.
     * @throws Exception thrown if the program fails to start.
     */
    @Override
    public void start(Stage primary_stage) throws Exception {
        AllText.changeLanguage("en");

        this.primary_stage = primary_stage;

        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/mergedNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/mergedEdges.csv", "EDGES");

        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        primary_stage.setTitle("Brigham and Women's");

        Scene primary_scene = new Scene(root, 1200, 800);

        Timeout.addListenersToScene(primary_scene);

        Timeout.start();

        primary_stage.setScene(primary_scene);
        primary_stage.show();

        // before system shutdown
        primary_stage.setOnCloseRequest(windowEvent -> {

            // write to CSV files
            if (Storage.getInstance().getDatabase() != null) {
                CSVWriter csv_writer = new CSVWriter(Storage.getInstance().getDatabase());
                csv_writer.writeCSVFile("csv/mergedNodes.csv", "NODES");
                csv_writer.writeCSVFile("csv/mergedEdges.csv", "EDGES");
            }
        });

    }

    public static void handleClock(Label clock_label) {

    }
}
