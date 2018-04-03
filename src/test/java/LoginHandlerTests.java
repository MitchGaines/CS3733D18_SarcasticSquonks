import org.junit.Assert;
import org.junit.Test;
import user.*;

public class LoginHandlerTests {
/**
 * LoginHandlerTests.java
 * Tests login functionality
 * Author: Matthew Puentes
 * Date: April 2, 2018
 */

    @Test
    public void LoginDoctor() {
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        try {
            u = lh.login("Doctor", "Password");
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
            u = lh.login("Admin", "SecurePassword");
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
            u = lh.login("Bob", "abc");
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
        u = lh.login("Bob", "oh god no");
    }

    @Test(expected = InvalidUsernameException.class)
    public void invalidUsername() throws Exception{
        LoginHandler lh = new LoginHandler();
        lh.__generateDummyUsers();
        User u;
        u = lh.login("Wilson Wong", "Willy Wongka");
    }
}
