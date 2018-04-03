import controller.Main;
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
 * April 3, 2018
 */

/*  Ensure screen is at 1080p resolution for optimal results.
    Errors are known to occur on 2160p resolution, where tests
    do not run as expected. Tests also take much longer at
    higher resolutions.

    All tests return user to home screen if not on it.
 */

public class JavaFxTests extends ApplicationTest {

    //launch Main
    @Before
    public void setUpClass() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    //show screen
    @Override
    public void start(Stage stage) {
        stage.show();
    }

    //close after test so no other tests get messed up/IntelliJ (hopefully) doesn't crash
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /* Universal functions to make testing easier */

    // hit logout button on page (assuming there is one)
    public void logout() {
        clickOn("#logout_btn");
    }

    public void loginAsDoc() {
        write("doctor");
        clickOn("#password");
        write("doctor");
        clickOn("#login_btn");
    }

    public void loginAsAdmin() {
        write("admin");
        clickOn("#password");
        write("admin");
        clickOn("#login_btn");
    }

    public void loginAsReg() {
        write("regstaff");
        clickOn("#password");
        write("regstaff");
        clickOn("#login_btn");
    }

    // hits the back button, assuming there is a back button
    public void goBack() {
        clickOn("#back_btn");
    }

    /* Tests for home screen/visitors */

    @Test(expected = FxRobotException.class) //expects to throw exception
    public void clickOnRandom() {
        clickOn("This is not an element"); //element does not exist
    }

    @Test
    public void clickOnLoginButton() {
        clickOn("#login_btn"); //click on login button
        verifyThat("#wrong_credentials", hasText("Wrong username or password")); //checks label status
    }

    @Test
    public void wrongCredentials() {
        write("visitor");
        clickOn("#password");
        write("notCorrectPassword");
        clickOn("#login_btn");
        verifyThat("#wrong_credentials", hasText("Wrong username or password")); //checks label status
    }

    @Test
    public void chooseQuickLocation() {
        clickOn("Quick Locations");
        clickOn("Exit");
        clickOn("Named Locations");
        clickOn("Cafeteria");
        //clickOn("Room No."); //possibly going to need an object ID for room number TextField object
        //write("some room"); //can't do this without being able to click on room no.
        clickOn("Stairs");
        clickOn("#pathfind");
        clickOn("#back_button");
    }

    /* Tests for Doctors */

    @Test
    public void loginDoctorTest() {
        loginAsDoc();
        logout();
    }

    @Test
    public void makeRequestDoctor() {
        loginAsDoc();
        clickOn("Type of Request");
        clickOn("Action 1");
        clickOn("Type of Request");
        clickOn("Action 2");
        clickOn("Request");
        logout();
    }

    @Test
    public void markRequestCompletedDoctor() {
        loginAsDoc();
        clickOn("My Requests");
        clickOn("Action 1");
        clickOn("Mark Completed"); //clicks the wrong one of the two buttons since names are the same, no ID
        clickOn("My Requests");
        clickOn("Action 2");
        clickOn("Mark Completed"); //clicks the wrong one of the two buttons since names are the same, no ID
        logout();
    }

    /* Tests for Admins */

    @Test
    public void loginAdminTest() {
        loginAsAdmin();
        logout();
    }

    @Test
    public void makeRequestAdmin() {
        loginAsAdmin();
        clickOn("Type of Request");
        clickOn("Action 1");
        clickOn("Type of Request");
        clickOn("Action 2");
        clickOn("Request");
        logout();
    }

    @Test
    public void removeRequestAdmin() {
        loginAsAdmin();
        clickOn("My Requests");
        clickOn("Action 1");
        clickOn("Mark Completed");
        clickOn("My Requests");
        clickOn("Action 2");
        clickOn("Mark Completed");
        logout();
    }

    @Test
    public void checkUnfilledFields() {
        loginAsAdmin();
        clickOn("Modify Map");
        clickOn("#add_loc");
        verifyThat("#add_loc_fail", hasText("Please fill all fields")); //ensure error text is thrown
        goBack();
        logout();
    }

    @Test
    public void checkPartialUnfilledFields() {
        loginAsAdmin();
        clickOn("Modify Map");
        clickOn("#floor");
        write("2");
        clickOn("#building");
        write("some building");
        clickOn("#add_loc");
        verifyThat("#add_loc_fail", hasText("Please fill all fields")); //ensure error text is thrown
        goBack();
        logout();
    }

    @Test
    public void addNode() {
        loginAsAdmin();
        clickOn("Modify Map");
        clickOn("#floor");
        write("2");
        clickOn("#building");
        write("some building");
        clickOn("#loc_type");
        write("some location");
        clickOn("#long_name");
        write("some long name");
        clickOn("#short_name");
        write("name");
        clickOn("#add_loc");
        clickOn("#pane");
        goBack();
        logout();
    }

    @Test
    public void deleteNode() {
        loginAsAdmin();
        clickOn("Modify Map");
        clickOn("#trash_can");
        clickOn("#pane");
        clickOn("#delete_loc_cancel");
        goBack();
        logout();
    }

    @Test
    public void addAndDeleteNodes() {
        loginAsAdmin();
        clickOn("Modify Map");
        clickOn("#floor");
        write("2");
        clickOn("#building");
        write("some building");
        clickOn("#loc_type");
        write("some location");
        clickOn("#long_name");
        write("some long name");
        clickOn("#short_name");
        write("name");
        clickOn("#add_loc");
        clickOn("#pane");
        clickOn("#trash_can");
        clickOn("#pane");
        clickOn("#delete_loc_cancel");
        goBack();
        logout();
    }

    @Test
    public void goBackTest() {
        loginAsAdmin();
        clickOn("Modify Map");
        goBack();
        logout();
    }

    /* Tests for Regular Staff */

    @Test
    public void loginRegTest() {
        loginAsReg();
        logout();
    }

    @Test
    public void makeRequestsReg() {
        loginAsReg();
        clickOn("Type of Request");
        clickOn("Action 1");
        clickOn("Type of Request");
        clickOn("Action 2");
        clickOn("Request");
        logout();
    }

    @Test
    public void removeRequestReg() {
        loginAsReg();
        clickOn("My Requests");
        clickOn("Action 1");
        clickOn("Mark Completed");
        clickOn("My Requests");
        clickOn("Action 2");
        clickOn("Mark Completed");
        logout();
    }

    // Simulate multiple different users over one instance

    @Test
    public void multipleUsers() {
        chooseQuickLocation();
        addAndDeleteNodes();
        loginDoctorTest();
        loginRegTest();
        loginAdminTest();
        makeRequestDoctor();
        removeRequestReg();
        makeRequestAdmin();
        removeRequestAdmin();
        makeRequestsReg();
        chooseQuickLocation();
        loginRegTest();
    }
}
