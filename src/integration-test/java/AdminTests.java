import edu.wpi.cs3733d18.teamS.controller.Main;
import edu.wpi.cs3733d18.teamS.controller.Timeout;
import edu.wpi.cs3733d18.teamS.database.ApacheDatabase;
import edu.wpi.cs3733d18.teamS.database.CSVReader;
import edu.wpi.cs3733d18.teamS.database.CSVWriter;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import edu.wpi.cs3733d18.teamS.service.ServiceType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AdminTests extends ApplicationTest {

    private static Stage primary_stage;

    public static Object switchScenes(String title, String fxml_name) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml_name), AllText.getBundle());
            Parent user_parent = loader.load();
            Object controller = loader.getController();
            Scene new_scene = new Scene(user_parent, primary_stage.getWidth(), primary_stage.getHeight());
            primary_stage.setTitle(title);

            Timeout.addListenersToScene(new_scene);

            primary_stage.setScene(new_scene);
            primary_stage.show();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //launch program
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

        // generate an initial list of service types for the database
        ServiceType.createInitialServiceTypes();

        Parent root = FXMLLoader.load(getClass().getResource("/AdminPage.fxml"), AllText.getBundle());
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
                csv_writer.writeCSVFile("csv/mergedEdges_edited.csv", "EDGES");
            }
        });

    }

    //close after test so no other tests get messed up/IntelliJ (hopefully) doesn't crash
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testChangePathAlg() {
        clickOn("#path_algorithm_box");
        clickOn("Depth First");
        clickOn("#path_algorithm_box");
        clickOn("Dijkstras");
        clickOn("#path_algorithm_box");
        clickOn("AStar");
    }

    @Test
    public void testRequest() {
        clickOn("#request_type_selector");
        clickOn("#service_title");
        write("test");
        clickOn("#service_location");
        write("15 Francis Security Desk Floor 2");
        clickOn("#description_field");
        write("test");
        clickOn("#request_service_button");
    }

    @Test
    public void testCheckTasks() {
        clickOn("#active_requests_box");
    }

    @Test
    public void testModMap() {
        moveTo("#modify_map_btn");
    }

    @Test
    public void testViewLog() {
        moveTo("View log");
    }

    @Test
    public void testReports() {
        moveTo("#reports_button");
    }
}
