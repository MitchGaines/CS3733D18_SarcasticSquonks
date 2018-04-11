import org.junit.Assert;
import org.junit.Test;
import edu.wpi.cs3733d18.teamS.user.User;

/**
 * UserTests.java
 * Tests for all User types
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public class UserTests {
    User doc1 = new User("doc1", "doc1", User.user_type.DOCTOR, false);

    @Test
    public void canModMap() {
        doc1.setCanModMap(true);
        Assert.assertTrue(doc1.canModMap());
    }

    @Test
    public void cannotModMap() {
        doc1.setCanModMap(false);
        Assert.assertFalse(doc1.canModMap());
    }

    @Test
    public void changedModMapPrivilege() {
        doc1.setCanModMap(true);
        doc1.setCanModMap(false);
        Assert.assertFalse(doc1.canModMap());
    }

    @Test
    public void setUsername() {
        doc1.setUsername("doctor");
        Assert.assertEquals("doctor", doc1.getUsername());
    }

    @Test
    public void changeUsername() {
        doc1.setUsername("doctor");
        doc1.setUsername("not_doctor");
        Assert.assertEquals("not_doctor", doc1.getUsername());
    }
}
