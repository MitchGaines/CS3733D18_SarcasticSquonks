import org.junit.Assert;
import org.junit.Test;
import user.Doctor;

/**
 * UserTests.java
 * Tests for all User types
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public class UserTests {
    Doctor doc1 = new Doctor();
    //Doctor doc2 = new Doctor();

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

    /*
    @Test
    public void testSalt() {
        Assert.assertEquals("[B@66cd51c3", doc1.generateSalt().toString());
    }

    @Test
    public void testSaltDifForUsers() {
        Assert.assertEquals("[B@4dcbadb4", doc1.generateSalt().toString());
        Assert.assertNotEquals("[B@4dcbadb4", doc2.generateSalt().toString());
    }


    @Test
    public void testSaltChanges() {
        doc1.generateSalt(); //salt is [B@1b9e1916
        Assert.assertNotEquals("[B@1b9e1916", doc1.generateSalt().toString());
    }
    */
}
