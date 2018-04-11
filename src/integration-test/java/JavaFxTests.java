//import edu.wpi.cs3733d18.teamS.controller.Main;
//import edu.wpi.cs3733d18.teamS.database.ApacheDatabase;
//import edu.wpi.cs3733d18.teamS.database.CSVReader;
//import edu.wpi.cs3733d18.teamS.database.CSVWriter;
//import edu.wpi.cs3733d18.teamS.database.Storage;
//import edu.wpi.cs3733d18.teamS.internationalization.AllText;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.HorizontalDirection;
//import javafx.geometry.VerticalDirection;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.MouseButton;
//import javafx.stage.Stage;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.testfx.api.FxRobotException;
//import org.testfx.api.FxToolkit;
//import org.testfx.framework.junit.ApplicationTest;
//import java.util.concurrent.TimeoutException;
//import static org.testfx.api.FxAssert.verifyThat;
//import static org.testfx.matcher.control.LabeledMatchers.hasText;
//
///**
// * JavaFxTests.java
// * Unit tests for our GUI using TestFX
// *
// * Author: Danny Sullivan
// * Date: April 3, 2018
// */
//
///*
//    Ensure screen is at 1080p resolution for optimal results.
//    Errors are known to occur on 2160p resolution, where tests
//    do not run as expected. Tests also take much longer at
//    higher resolutions.
//
//    All tests return edu.wpi.cs3733d18.teamS.user to home screen if not on it.
// */
//
//public class JavaFxTests extends ApplicationTest {
//
//    //launch program
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        AllText.changeLanguage("en");
//
//        // set edu.wpi.cs3733d18.teamS.database and storage class
//        Storage storage = Storage.getInstance();
//        storage.setDatabase(new ApacheDatabase("apacheDB"));
//
//        // read from CSV files
//        CSVReader csv_reader = new CSVReader(storage.getDatabase());
////        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
////        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");
//        csv_reader.readCSVFile("csv/mergedNodes.csv", "NODES");
//        csv_reader.readCSVFile("csv/mergedEdges.csv", "EDGES");
//        csv_reader.readCSVFile("csv/users.csv", "USERS");
//
//        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
//        primaryStage.setTitle("Brigham and Women's");
//        primaryStage.setScene(new Scene(root, 1200, 800));
//        primaryStage.show();
//        primaryStage.toFront();
//        primaryStage.requestFocus();
//        //ServiceType.createDummyTypes();
//        //TODO: actually use LoginHandler correctly.
//        // before system shutdown
//        primaryStage.setOnCloseRequest(windowEvent -> {
//
//            // write to CSV files
//            if (Storage.getInstance().getDatabase() != null) {
//                CSVWriter csv_writer = new CSVWriter(Storage.getInstance().getDatabase());
//                csv_writer.writeCSVFile("csv/mergedNodes.csv", "NODES");
//                csv_writer.writeCSVFile("csv/mergedEdges.csv", "EDGES");
//                csv_writer.writeCSVFile("csv/users.csv", "USERS");
//            }
//        });
//
//    }
//
//    //close after test so no other tests get messed up/IntelliJ (hopefully) doesn't crash
//    @After
//    public void afterEachTest() throws TimeoutException {
//        FxToolkit.hideStage();
//        release(new KeyCode[]{});
//        release(new MouseButton[]{});
//    }
//
//
//    /* Home screen tests */
//
//    @Test
//    public void testChangeLang() {
//        clickOn("#language_selector");
//        clickOn("Spanish");
//        clickOn("Confirm");
//        verifyThat("#edu.wpi.cs3733d18.teamS.pathfind", hasText("Direcciones"));
//    }
//
//    /* Causes gradle to fail, passes on my machine. Possibly due to accented e in "Inglés"
//    @Test
//    public void testChangeLangBack() {
//        clickOn("#language_selector");
//        clickOn("Spanish");
//        clickOn("Confirm");
//        clickOn("#language_selector");
//        clickOn("Inglés");
//        clickOn("Confirm");
//    }
//    */
//
//    @Test
//    public void testPathfind() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testPathfindSpanish() {
//        clickOn("#language_selector");
//        clickOn("Spanish");
//        clickOn("Confirm");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        verifyThat("#toggle_map_btn", hasText("Mapa en 3D"));
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testQRcodePopUp() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        clickOn("Step-by-Step Directions");
//        clickOn("#expanded_qr");
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testMapSwitcher() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        verifyThat("#toggle_map_btn", hasText("3D Map"));
//        clickOn("#toggle_map_btn");
//        verifyThat("#toggle_map_btn", hasText("2D Map"));
//        clickOn("#toggle_map_btn");
//        verifyThat("#toggle_map_btn", hasText("3D Map"));
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testSwitchFloors() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        clickOn("#map_img");
//        verifyThat("#floor_indicator", hasText("2"));
//        clickOn("Up");
//        verifyThat("#floor_indicator", hasText("3"));
//        clickOn("Down");
//        verifyThat("#floor_indicator", hasText("2"));
//        clickOn("Up");
//        verifyThat("#floor_indicator", hasText("3"));
//        clickOn("#toggle_map_btn");
//        clickOn("#map_img");
//        clickOn("Down");
//        verifyThat("#floor_indicator", hasText("2"));
//        clickOn("Up");
//        verifyThat("#floor_indicator", hasText("3"));
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testMapZoom() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Francis");
//        clickOn("15 Francis Security Desk Floor 2");
//        clickOn("#combobox_end");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#edu.wpi.cs3733d18.teamS.pathfind");
//        clickOn("#zoom_out");
//        clickOn("#zoom_out");
//        clickOn("#zoom_in");
//        clickOn("#zoom_in");
//        clickOn("#toggle_map_btn");
//        clickOn("#zoom_out");
//        clickOn("#zoom_out");
//        clickOn("#zoom_in");
//        clickOn("#zoom_in");
//        clickOn("#back_button");
//    }
//
//
//    /* Commented out to pass travis, works on my machine
//    @Test
//    public void testQuickLocationInfo() {
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        clickOn("#combobox_start");
//        write("15 Lobby");
//        clickOn("15 Lobby Entrance Floor 2");
//        clickOn("#INFO");
//        clickOn("#back_button");
//    }
//
//
//    @Test
//    public void testQuickLocationRest() {
//        clickOn("#REST");
//        clickOn("#back_button");
//    }
//
//    @Test
//    public void testQuickLocationDept() {
//        clickOn("#DEPT");
//        clickOn("#back_button");
//    }
//    */
//
//}
