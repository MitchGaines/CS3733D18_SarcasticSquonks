package controller;

import database.ApacheDatabase;
import database.CSVReader;
import database.CSVWriter;
import database.Storage;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pathfind.QRCode;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");

        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        primaryStage.setTitle("Brigham and Women's");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();

        // before system shutdown
        primaryStage.setOnCloseRequest(windowEvent -> {

            // write to CSV files
            if (Storage.getInstance().getDatabase() != null) {
                CSVWriter csv_writer = new CSVWriter(Storage.getInstance().getDatabase());
                csv_writer.writeCSVFile("csv/MapBNodes.csv", "NODES");
                csv_writer.writeCSVFile("csv/MapBEdges.csv", "EDGES");
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
