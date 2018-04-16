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

import java.util.concurrent.TimeoutException;

public class DoctorTests extends ApplicationTest {
    private static Stage primary_stage;

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

        Parent root = FXMLLoader.load(getClass().getResource("/DoctorPage.fxml"), AllText.getBundle());
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
}
