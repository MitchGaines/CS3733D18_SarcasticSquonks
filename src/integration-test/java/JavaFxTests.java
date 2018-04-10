import controller.Main;
import database.ApacheDatabase;
import database.CSVReader;
import database.CSVWriter;
import database.Storage;
import internationalization.AllText;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import java.util.concurrent.TimeoutException;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 * JavaFxTests.java
 * Unit tests for our GUI using TestFX
 *
 * Author: Danny Sullivan
 * Date: April 3, 2018
 */

/*
    Ensure screen is at 1080p resolution for optimal results.
    Errors are known to occur on 2160p resolution, where tests
    do not run as expected. Tests also take much longer at
    higher resolutions.

    All tests return user to home screen if not on it.
 */

public class JavaFxTests extends ApplicationTest {

    /*
    //launch Main
    @Before
    public void setUpClass() throws Exception {
        ApplicationTest.launch(Main.class);
    }
    */

    //show screen
    @Override
    public void start(Stage primaryStage) throws Exception{
        AllText.changeLanguage("en");

        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
//        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
//        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");
        csv_reader.readCSVFile("csv/mergedNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/mergedEdges.csv", "EDGES");
        csv_reader.readCSVFile("csv/users.csv", "USERS");

        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
        primaryStage.setTitle("Brigham and Women's");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
        //ServiceType.createDummyTypes();
        //TODO: actually use LoginHandler correctly.
        // before system shutdown
        primaryStage.setOnCloseRequest(windowEvent -> {

            // write to CSV files
            if (Storage.getInstance().getDatabase() != null) {
                CSVWriter csv_writer = new CSVWriter(Storage.getInstance().getDatabase());
                csv_writer.writeCSVFile("csv/mergedNodes.csv", "NODES");
                csv_writer.writeCSVFile("csv/mergedEdges.csv", "EDGES");
                csv_writer.writeCSVFile("csv/users.csv", "USERS");
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

    /* Functions to make my life easier */

    public void loginAsDoctor() {
        moveTo("#exp_panel");
        moveBy(1155, -25);
        clickOn();
        clickOn("#username");
        write("Doctor");
        clickOn("#password");
        write("Password");
        clickOn("#login_btn");
    }

    public void loginAsAdmin() {
        moveTo("#exp_panel");
        moveBy(1155, -25);
        clickOn();
        clickOn("#username");
        write("Admin");
        clickOn("#password");
        write("SecurePassword");
        clickOn("#login_btn");
    }

    public void loginAsRegStaff() {
        moveTo("#exp_panel");
        moveBy(1155, -25);
        clickOn();
        clickOn("#username");
        write("Bob");
        clickOn("#password");
        write("abc");
        clickOn("#login_btn");
    }

    public void logout() {
        clickOn("#logout_btn");
    }

    /* Home screen tests */

    @Test
    public void testPathfind() {
        clickOn("#combobox_start");
        clickOn("15 Francis Security Desk Floor 2");
        clickOn("#combobox_end");
        clickOn("15 Lobby Entrance Floor 2");
        clickOn("#pathfind");
        clickOn("Home");
    }

    @Test
    public void testPathfindSpanish() {
        clickOn("#language_selector");
        clickOn("Spanish");
        clickOn("Confirm");
        clickOn("#combobox_start");
        clickOn("15 Francis Security Desk Floor 2");
        clickOn("#combobox_end");
        clickOn("15 Lobby Entrance Floor 2");
        clickOn("#pathfind");
        clickOn("Home");
    }

    /*
    @Test
    public void testChangeLangBack() {
        clickOn("#language_selector");
        clickOn("Spanish");
        clickOn("Confirm");
        clickOn("#language_selector");
        clickOn("Inglés");
        clickOn("Confirm");
    }
    */

    @Test
    public void testChangeLang() {
        clickOn("#language_selector");
        clickOn("Spanish");
    }

    /* Login Tests */

    @Test
    public void testDoctorLogin() {
        loginAsDoctor();
    }

    @Test
    public void testAdminLogin() {
        loginAsAdmin();
    }

    @Test
    public void testRegStaffLogin() {
        loginAsRegStaff();
    }

    @Test
    public void testLogout() {
        loginAsDoctor();
        logout();
        //sleep(5000);
        loginAsAdmin();
        logout();
        //sleep(5000);
        loginAsRegStaff();
        logout();
    }

    /* Tests for Doctor */

    /*
    @Test
    public void testDoctorMakeRequest() {
        loginAsDoctor();
        clickOn("#request_type_selector");
        clickOn("Medical");
        clickOn("#service_title");
        write("Medical issue");
        clickOn("#service_location");
        write("hospital");
        clickOn("#description_field");
        write("injury");
        clickOn("#request_service");
        clickOn("OK");
        verifyThat("#emergency_label", hasText("EMERGENCY"));
        verifyThat("#emergency_title", hasText("Medical issue, hospital"));
        verifyThat("#emergency_details", hasText("injury"));
        clickOn("View log");
        clickOn("#back_btn");
        clickOn("#active_requests_box");
        clickOn("Medical issue");
        clickOn("#mark_completed_btn");
        logout();
    }
    */

    /* Tests for RegStaff */
    /*
    @Test
    public void testRegStaffMakeRequest() {
        loginAsRegStaff();
        clickOn("#request_type_selector");
        clickOn("Custodial");
        clickOn("#service_title");
        write("Custodial issue");
        clickOn("#service_location");
        write("hospital");
        clickOn("#description_field");
        write("mess");
        clickOn("#request_service");
        clickOn("OK");
        clickOn("View log");
        clickOn("#back_btn");
        clickOn("#active_requests_box");
        clickOn("Custodial issue");
        clickOn("#mark_completed_btn");
        logout();
    }
    */

}
