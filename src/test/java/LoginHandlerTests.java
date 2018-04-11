import database.ApacheDatabase;
import database.IDatabase;
import database.Storage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.*;

public class LoginHandlerTests {
/**
 * LoginHandlerTests.java
 * Tests login functionality
 * Author: Matthew Puentes
 * Date: April 2, 2018
 */

    private Storage storage;

    @Before
    public void setUp() {
        IDatabase data = new ApacheDatabase("apacheDB");
        storage = Storage.getInstance();
        storage.setDatabase(data);
    }

    @Test
    public void LoginDoctor() {
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        try {
            u = lh.login("doctor", "doctor");
            Assert.assertTrue(u.getType() == User.user_type.DOCTOR);
        } catch (InvalidPasswordException e) {
            Assert.fail();
        } catch (InvalidUsernameException e) {
            Assert.fail();
        }
    }

    @Test
    public void LoginAdmin() {
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        try {
            u = lh.login("admin", "admin");
            Assert.assertTrue(u.getType() == User.user_type.ADMIN_STAFF);
        } catch (InvalidPasswordException e) {
            Assert.fail();
        } catch (InvalidUsernameException e) {
            Assert.fail();
        }
    }

    @Test
    public void LoginRegular() {
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        try {
            u = lh.login("staff", "staff");
            Assert.assertTrue(u.getType() == User.user_type.REGULAR_STAFF);
        } catch (InvalidPasswordException e) {
            Assert.fail();
        } catch (InvalidUsernameException e) {
            Assert.fail();
        }
    }

    @Test(expected = InvalidPasswordException.class)
    public void invalidPassword() throws Exception{
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        u = lh.login("doctor", "oh god no");
    }

    @Test(expected = InvalidUsernameException.class)
    public void invalidUsername() throws Exception{
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        u = lh.login("Wilson Wong", "Willy Wongka");
    }

    @After
    public void breakDown() {
        // drop tables at the end
        storage.getDatabase().dropTable("NODES");
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("USERS");
        storage.getDatabase().dropTable("SERVICES");
    }
}
