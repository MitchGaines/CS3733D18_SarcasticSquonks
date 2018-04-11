package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.database.ApacheDatabase;
import edu.wpi.cs3733d18.teamS.database.CSVReader;
import edu.wpi.cs3733d18.teamS.database.CSVWriter;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.wpi.cs3733d18.teamS.service.ServiceType;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AllText.changeLanguage("en");
        
        // set edu.wpi.cs3733d18.teamS.database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
//        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
//        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");
        csv_reader.readCSVFile("csv/mergedNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/mergedEdges.csv", "EDGES");
//        csv_reader.readCSVFile("csv/users.csv", "USERS");

        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        primaryStage.setTitle("Brigham and Women's");

        Scene primary_scene = new Scene(root, 1200, 800);

        Timeout.setCurrStage(primaryStage);

        Timeout.addListenersToScene(primary_scene);

        Timeout.start();

        primaryStage.setScene(primary_scene);
        primaryStage.show();

        ServiceType.createDummyTypes();
        //TODO: actually use LoginHandler correctly.
        // before system shutdown
        primaryStage.setOnCloseRequest(windowEvent -> {

            // write to CSV files
            if (Storage.getInstance().getDatabase() != null) {
                CSVWriter csv_writer = new CSVWriter(Storage.getInstance().getDatabase());
                csv_writer.writeCSVFile("csv/mergedNodes.csv", "NODES");
                csv_writer.writeCSVFile("csv/mergedEdges_edited.csv", "EDGES");
                csv_writer.writeCSVFile("csv/users_edited.csv", "USERS");
                //storage.getDatabase().dropTable("SERVICES");
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
        Timeout.stop();
    }
}