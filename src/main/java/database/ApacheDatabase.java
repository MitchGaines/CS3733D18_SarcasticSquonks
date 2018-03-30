package database;

/**
 * ApacheDatabase.java
 * An implementation of an Apache Derby database
 * Author: Joseph Turcotte
 * Date: March 24, 2018
 */

import org.apache.derby.jdbc.EmbeddedDriver;
import java.sql.*;

public class ApacheDatabase implements IDatabase {

    // fields
    private Connection connection; // establishes connection with driver
    private Statement statement; // statement to be executed
    private String db_name; // name of the database
    private boolean is_open = false; // tells whether connection is open

    /**
     * Constructor for the database
     * @param db_name name of the database
     */
    public ApacheDatabase(String db_name) {
        this.db_name = db_name;
    }

    /**
     * Connects to the embedded driver
     */
    @Override
    public void connect() {

        //System.out.println("-------Embedded Java DB Connection Testing --------");

        // silently return if connection is open
        if (is_open) {
            return;
        }

        // check for correct classpath
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            e.printStackTrace();
            return;
        }

        //System.out.println("Java DB driver registered!");

        // try to connect to database with given name
        try {
            DriverManager.registerDriver(new EmbeddedDriver());
            connection = DriverManager.getConnection("jdbc:derby:" + db_name + ";create=true");

            // set auto commit to false to control what is committed
            connection.setAutoCommit(false);

            // create a new statement
            statement = connection.createStatement();

            // open the connection
            is_open = true;
        } catch (SQLException e) {
            System.out.println("Could not connect to database: " + db_name + "\n" +
                    e.getMessage());
            e.printStackTrace();
        }

        //System.out.println("Java DB connection established!");

    } // end connect

    /**
     * Disconnects from the embedded driver
     */
    @Override
    public void disconnect() {

        // silently return if connection is closed
        if (!is_open) {
            return;
        }

        // close connection and statement, shutdown driver
        try {
            statement.close();
            connection.close();
            DriverManager.getConnection("jdbc:derby:" + db_name + ";shutdown=true");
            is_open = false;
        } catch (SQLException e) {
            System.out.println("Java DB connection did not shutdown successfully.");
        }

        //System.out.println("Java DB connection shutdown successfully!");

    } // end disconnect

    /**
     * Checks whether the table with a given name exists in the database
     * @param table_name the name of the table
     * @return true if the table exists in the database, and false otherwise
     */
    @Override
    public boolean doesTableExist(String table_name) {

        // silently return if connection is closed
        if (!is_open) {
            return false;
        }

        // loop through list of tables in metadata and check for table name
        try {
            DatabaseMetaData md = connection.getMetaData();

            // retrieve all of the tables from the connection database
            ResultSet rs = md.getTables(null, null, "%", null);

            while (rs.next()) {
                // table name is the third entry in the table object
                String extractedName = rs.getString(3).toLowerCase();

                if (extractedName.equals(table_name.toLowerCase())) {
                    //System.out.println("Table with name " + tableName + " found!");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Table could not be found.");
            e.printStackTrace();
        }

        // default return
        return false;

    } // end doesTableExist

    /**
     * Creates a table in the database
     * @param table_name the name of the table to create
     * @param columns the columns (fields) of the database
     */
    @Override
    public void createTable(String table_name, String[] columns) {

        // silently return if table already exists or connection is closed
        if(!is_open || doesTableExist(table_name)){
            return;
        }

        // build the statement using string properties
        StringBuilder create_string = new StringBuilder();

        // join together column names separated by commas
        create_string.append(String.join(", ", columns));

        // try to execute the create statement
        try {
            statement.execute("CREATE TABLE " + table_name + "(\n" + create_string + "\n)");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Could not create table " + table_name);
            e.printStackTrace();
        }

        //System.out.println("Created table " + table_name);

    } // end createTable

    /**
     * Removes a given table from the database
     * @param table_name the name of the table to remove
     */
    @Override
    public void dropTable(String table_name) {

        // silently return if connection is closed
        if (!is_open) {
            return;
        }

        // try to delete the table
        try {
            String drop_table = "DROP TABLE " + table_name;
            statement.execute(drop_table);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Could not delete table " + table_name);
            e.printStackTrace();
        }

        //System.out.println("Deleted table " + table_name);

    } // end dropTable

    /**
     * Executes a query corresponding to a table in the database
     * @param table_name the name of the table
     * @param columns specific columns to select from the table; default null
     * @param where_condition optional argument that restricts the query (e.g. "node_id = ?"); default null
     * @param whereArgs optional arguments to replace "?" in the condition (similar to Android); default null
     * @param order_by optional argument that orders the table entries (e.g. "node_id DESC"); default null
     * @return a ResultSet corresponding to all table entries that match the query
     */
    public ResultSet query(String table_name, String[] columns,
                           String where_condition, String[] whereArgs, String order_by) {

        // silently return if connection is closed
        if (!is_open) {
            return null;
        }

        // build the query string
        StringBuilder query_string = new StringBuilder("SELECT ");

        // if there are specific columns to select, do so
        if (columns != null) {

            int length = columns.length;
            for (int i = 0; i < length; i++) {
                query_string.append(columns[i]);

                // if we aren't at the last value, separate with a comma
                if (i != length - 1) {
                    query_string.append(", ");
                }
            }
        } else {
            // select everything
            query_string.append("*");
        }

        // add from clause and table name
        query_string.append(" FROM ");
        query_string.append(table_name);

        // if there is a condition, include it
        if (where_condition != null) {
            query_string.append(" WHERE ");

            // replace question marks in where condition with strings (Android style!)
            if (where_condition.contains("?") && whereArgs != null && whereArgs.length > 0) {
                for (String whereArg : whereArgs) {
                    where_condition = where_condition.replaceFirst("\\?", "'" + whereArg + "'");
                }
            }

            // append the new where condition
            query_string.append(where_condition);
        } else {
            // if no condition, query every row! (not too dangerous)
            query_string.append(" WHERE 1 = 1");
        }

        // if there is an order, include it
        if (order_by != null) {
            query_string.append(" ORDER BY ");
            query_string.append(order_by);
        }

        // try the query
        try {
            return statement.executeQuery(query_string.toString());
        } catch (SQLException e) {
            System.out.println("Could not process query for table " + table_name);
            e.printStackTrace();
        }

        // default return value
        return null;

    } // end query

    /**
     * Inserts values into the table with a given name
     * @param table_name the name of the table
     * @param values the values to insert
     */
    @Override
    public void insert(String table_name, String[] values) {

        // silently return if connection is closed
        if (!is_open) {
            return;
        }

        // begin with insert statement and name of table
        StringBuilder insert_string = new StringBuilder("INSERT INTO " + table_name + " VALUES ( ");

        // loop through values and append each to insert string
        int length = values.length;
        for (int i = 0; i < length; i++) {
            insert_string.append(values[i]);

            // if we aren't at the last value, separate with a comma
            if (i != length - 1) {
                insert_string.append(", ");
            }
        }
        // close with a parenthesis
        insert_string.append(" )");

        // try to insert the entry
        try {
            statement.execute(insert_string.toString());
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Could not insert entry in table " + table_name);
            e.printStackTrace();
        }

        //System.out.println("Inserted entry in table " + table_name);

    } // end insert

    /**
     * Updates an entry (or multiple entries) in the database
     * @param table_name the name of the table
     * @param values the new values to insert
     * @param where_condition optional argument that restricts the query (e.g. "node_id = ?"); default null
     * @param whereArgs optional arguments to replace "?" in the condition (similar to Android); default null
     */
    @Override
    public void update(String table_name, String[] values, String where_condition, String[] whereArgs) {

        // silently return if connection is closed
        if (!is_open) {
            return;
        }

        // build update string
        StringBuilder update_string = new StringBuilder("UPDATE " + table_name + " SET ");

        // loop through values and update each
        int length = values.length;
        for (int i = 0; i < length; i++) {
            update_string.append(values[i]);

            // if we aren't at the last value, separate with a comma
            if (i != length - 1) {
                update_string.append(", ");
            }
        }

        // if there is a condition, include it
        if (where_condition != null) {
            update_string.append(" WHERE ");

            // replace question marks in where condition with strings (Android style!)
            if (where_condition.contains("?") && whereArgs != null && whereArgs.length > 0) {
                for (String whereArg : whereArgs) {
                    where_condition = where_condition.replaceFirst("\\?", "'" + whereArg + "'");
                }
            }

            // update with the new where condition
            update_string.append(where_condition);
        } else {
            // if no condition, update every row! (dangerous)
            update_string.append(" WHERE 1 = 1");
        }

        // try to update the entry
        try {
            statement.execute(update_string.toString());
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Could not update entry in table " + table_name);
            e.printStackTrace();
        }

        //System.out.println("Updated entry in table " + table_name);

    } // end update

    /**
     * Removes an entry from the table with the given name
     * @param table_name the name of the table
     * @param where_condition optional argument that restricts the query (e.g. "node_id = ?"); default null
     * @param whereArgs optional arguments to replace "?" in the condition (similar to Android); default null
     */
    public void delete(String table_name, String where_condition, String[] whereArgs) {

        // silently return if connection is closed
        if (!is_open) {
            return;
        }

        // build delete string
        StringBuilder delete_string = new StringBuilder("DELETE FROM " + table_name);

        // if we have a condition, process it
        if (where_condition != null) {
            delete_string.append(" WHERE ");

            // replace question marks in where condition with strings (Android style!)
            if (where_condition.contains("?") && whereArgs != null && whereArgs.length > 0) {
                for (String whereArg : whereArgs) {
                    where_condition = where_condition.replaceFirst("\\?", "'" + whereArg + "'");
                }
            }

            // delete with the new where condition
            delete_string.append(where_condition);
        } else {
            // if no condition, delete everything! (dangerous)
            delete_string.append(" WHERE 1 = 1");
        }

        // try to delete the entry
        try {
            statement.execute(delete_string.toString());
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Could not delete entry in table " + table_name);
            e.printStackTrace();
        }

        //System.out.println("Deleted entry in table " + table_name);

    } // end delete

    /**
     * Adds quotes to a given string for SQL statements
     * @param s the string to add quotes to
     * @return a String with quotes added around it
     */
    public String addQuotes(String s) {
        return String.format("'%s'", s.replaceAll("'", "''"));
    }

    /**
     * Gets the status of the database (open/closed)
     * @return true if the connection is open, false otherwise
     */
    public boolean getIsOpen() {
        return this.is_open;
    }
}

