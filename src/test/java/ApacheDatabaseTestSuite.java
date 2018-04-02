/**
 * ApacheDatabaseTestSuite.java
 * A test suite for database operations
 * Author: Joseph Turcotte
 * Date: March 28, 2018
 */

import database.IDatabase;
import database.ApacheDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;

public class ApacheDatabaseTestSuite {

    private IDatabase data;
    private String table_name_edges;

    // ------------------- APACHE DATABASE TESTS -------------------- //

    /**
     * Set up tests for the database
     */
    @Before
    public void setUp() {
        data = new ApacheDatabase("apacheDB");
        table_name_edges = "EDGES";
    }

    /**
     * Test that we can connect to the database
     */
    @Test
    public void testConnection() {
        // connect to the database
        data.connect();

        // assert that the connection worked
        Assert.assertEquals(data.getIsOpen(), true);
    }

    /**
     * Test that a table we created exists (and one that we drop doesn't)
     */
    @Test
    public void testTableExistence() {
        // create table
        data.connect();
        String[] columns = {"edges varchar(100)"};
        data.createTable(table_name_edges, columns);

        // test for existence
        Assert.assertEquals(data.doesTableExist(table_name_edges), true);

        // drop the table
        data.dropTable(table_name_edges);

        // test for non-existence
        Assert.assertEquals(data.doesTableExist(table_name_edges), false);
    }

    /**
     * Test that a table we didn't create doesn't exist
     */
    @Test
    public void testTableNonExistence() {
        // table name doesn't exist in database
        data.connect();
        Assert.assertEquals(data.doesTableExist("JOE"), false);
    }

    /**
     * Test inserting a value into a table
     */
    @Test
    public void testInsert() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entry into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);

        // assert that the entry was inserted (checking length)
        ResultSet rs = data.query(table_name_edges, null, null, null, null);
        int length = 0;
        try {
            while(rs.next()) {
                length++;
            }
        } catch (SQLException e) {
            // empty
        }

        Assert.assertEquals(length, 1);

        // insert another entry
        String[] new_values = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, new_values);

        // assert that the entry was inserted and that the fields are correct
        ResultSet rs2 = data.query(table_name_edges, null, null, null, null);
        int length2 = 0;
        try {
            while(rs2.next()) {
                length2++;
            }
        } catch (SQLException e) {
            // empty
        }

        Assert.assertEquals(length2, 2);

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test deleting an entry from a table
     */
    @Test
    public void testDelete() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entry into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);

        // subsequently delete the entry
        String where_condition = "edge_id = '1'";
        data.delete(table_name_edges, where_condition, null);

        // check that we have 0 entries
        ResultSet rs = data.query(table_name_edges, null, null, null, null);
        int length = 0;
        try {
            while(rs.next()) {
                length++;
            }
        } catch (SQLException e) {
            // empty
        }
        Assert.assertEquals(length, 0);

        // insert some new entries into the table
        String[] values2 = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values2);
        String[] values3 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values3);
        String[] values4 = {"'3'", "'node2'", "'node4'"};
        data.insert(table_name_edges, values4);

        // delete every entry in the table!
        data.delete(table_name_edges, null, null);

        // check that we have 0 entries
        ResultSet rs2 = data.query(table_name_edges, null, null, null, null);
        int length2 = 0;
        try {
            while(rs2.next()) {
                length2++;
            }
        } catch (SQLException e) {
            // empty
        }
        Assert.assertEquals(length2, 0);

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test that deleting an entry that doesn't exist in the table doesn't change anything
     */
    @Test
    public void testNoDelete() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entry into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);

        // condition doesn't match any entry, so nothing should be deleted
        String where_condition = "edge_id = '2'";
        data.delete(table_name_edges, where_condition, null);

        // check that we have 0 entries
        ResultSet rs = data.query(table_name_edges, null, null, null, null);
        int length = 0;
        try {
            while(rs.next()) {
                length++;
            }
        } catch (SQLException e) {
            // empty
        }
        Assert.assertEquals(length, 1);

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test updating an entry in the database
     */
    @Test
    public void testUpdate() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entries into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);
        String[] values2 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values2);

        // update the database
        String[] new_values = {"edge_id = '2'", "start_node = 'node2'", "end_node = 'node4'"};
        String where_condition = "edge_id = '2'";
        data.update(table_name_edges, new_values, where_condition, null);

        // check the values of the updated entry
        try {
            ResultSet rs = data.query(table_name_edges, null, where_condition, null, null);

            String edge_id = rs.getString("edge_id");
            Assert.assertEquals(edge_id, "'2'");

            String start_node = rs.getString("start_node");
            Assert.assertEquals(start_node, "'node_2'");

            String end_node = rs.getString("end_node");
            Assert.assertEquals(end_node, "'node4'");
        } catch (SQLException e) {
            // empty
        }

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test a generic query that grabs all information from the database
     */
    @Test
    public void testGenericQuery() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entry into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);

        // query the table
        ResultSet rs = data.query(table_name_edges, null, null, null, null);

        // check the values of the first entry
        try {
            String id = rs.getString("edge_id");
            String start_node = rs.getString("start_node");
            String end_node = rs.getString("end_node");

            Assert.assertEquals(id, "'1'");
            Assert.assertEquals(start_node, "'node_1'");
            Assert.assertEquals(end_node, "'node_2'");
        } catch (SQLException e) {
            // empty
        }

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test a query that only selects specific columns
     */
    @Test
    public void testSelectColumns() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entries into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);
        String[] values2 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values2);

        // query the table
        String[] columns_to_query = {"edge_id", "start_node"};
        ResultSet rs = data.query(table_name_edges, columns_to_query, null, null, null);

        // expect an SQLException to be thrown when we try to access end_node
        boolean didFail = false;
        try {
            String end_node = rs.getString("end_node");
        } catch (SQLException e) {
            didFail = true;
        }

        Assert.assertTrue(didFail);

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test a query with a specific condition
     */
    @Test
    public void testWhereClause() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entries into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);
        String[] values2 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values2);
        String[] values3 = {"'3'", "'node2'", "'node4'"};
        data.insert(table_name_edges, values3);

        // query the table with a specific condition
        String where_condition = "start_node = ?";
        String[] where_args = {"node2"};
        ResultSet rs = data.query(table_name_edges, null, where_condition, where_args, null);

        // test that we only have two entries
        int length = 0;
        try {
            while(rs.next()) {
                length++;
            }
            Assert.assertEquals(length, 2);
        } catch (SQLException e) {
            // empty
        }

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test a query with multiple conditions
     */
    @Test
    public void testMultipleWhereClauses() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entries into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);
        String[] values2 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values2);
        String[] values3 = {"'3'", "'node2'", "'node4'"};
        data.insert(table_name_edges, values3);

        // query the table with a specific condition
        String where_condition = "start_node = ? AND end_node = ?";
        String[] where_args = {"node2", "node4"};
        ResultSet rs = data.query(table_name_edges, null, where_condition, where_args, null);

        // test that we have the right entry and that we only have one
        try {
            String edge_id = rs.getString("edge_id");
            Assert.assertEquals(edge_id, "'3'");

            int length = 0;
            while(rs.next()) {
                length++;
            }
            Assert.assertEquals(length, 1);

        } catch (SQLException e) {
            // empty
        }

        // drop table at the end
        data.dropTable(table_name_edges);
    }

    /**
     * Test the order by clause
     */
    @Test
    public void testOrderByClause() {
        data.connect();
        String[] columns = {"edge_id varchar(100)", "start_node varchar(100)", "end_node varchar(100)"};
        data.createTable(table_name_edges, columns);

        // insert entries into table
        String[] values = {"'1'", "'node1'", "'node2'"};
        data.insert(table_name_edges, values);
        String[] values2 = {"'2'", "'node2'", "'node3'"};
        data.insert(table_name_edges, values2);
        String[] values3 = {"'3'", "'node2'", "'node4'"};
        data.insert(table_name_edges, values3);

        // query the table with a specific condition
        String order_by = "edge_id DESC";
        ResultSet rs = data.query(table_name_edges, null, null, null, order_by);

        // test the order of the entries
        try {
            Assert.assertEquals(rs.getString("edge_id"), "'3'");
            rs.next();
            Assert.assertEquals(rs.getString("edge_id"), "'2'");
            rs.next();
            Assert.assertEquals(rs.getString("edge_id"), "'1'");
        } catch (SQLException e) {
            // empty
        }

        // drop table at the end
        data.dropTable(table_name_edges);
    }
}

