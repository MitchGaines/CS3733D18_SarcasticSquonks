package edu.wpi.cs3733d18.teamS.database;


import java.sql.ResultSet;

/**
 * An interface to help the classes work with in the edu.wpi.cs3733d18.teamS.database.
 *
 * @author Joseph Turcotte
 * @version 1.0
 * Date: March 24, 2018
 */
public interface IDatabase {

    /**
     * Connects to the embedded driver.
     */
    void connect(); // to connect to the embedded driver

    /**
     * Disconnects from the embedded driver.
     */
    void disconnect(); // to disconnect from the embedded driver

    /**
     * Checks for a tables existence.
     *
     * @param table_name the name of the table.
     */
    boolean doesTableExist(String table_name); // checks for table existence

    /**
     * Creates a table.
     *
     * @param table_name the name of the table.
     * @param columns    the columns (fields) of the edu.wpi.cs3733d18.teamS.database.
     */
    void createTable(String table_name, String[] columns); // creates a table

    /**
     * Removes a given table from the edu.wpi.cs3733d18.teamS.database.
     *
     * @param table_name the name of the table to remove.
     */
    void dropTable(String table_name); // deletes a table

    ResultSet query(String tableName, String[] columns,
                    String where_condition, String[] where_args, String order_by); // executes a query

    /**
     * Inserts an entry into a table.
     *
     * @param table_name name of the table.
     * @param values     value to be inserted.
     */
    void insert(String table_name, String[] values); // inserts an entry into a table


    /**
     * Updates an entry (or multiple entries) in the edu.wpi.cs3733d18.teamS.database.
     *
     * @param table_name      the name of the table.
     * @param values          the new values to insert.
     * @param where_condition optional argument that restricts the query (e.g. "node_id = ?"); default null.
     * @param where_args      optional arguments to replace "?" in the condition (similar to Android); default null.
     */
    void update(String table_name, String[] values, String where_condition, String[] where_args); // updates an entry in a table

    /**
     * Removes an entry from the table with the given name.
     *
     * @param table_name      the name of the table.
     * @param where_condition optional argument that restricts the query (e.g. "node_id = ?"); default null.
     * @param where_args      optional arguments to replace "?" in the condition (similar to Android); default null.
     */
    void delete(String table_name, String where_condition, String[] where_args); // deletes an entry from a table

    /**
     * Adds quotes to a given string for SQL statements.
     *
     * @param s the string to add quotes to.
     */
    String addQuotes(String s); // add quotes to a string

    /**
     * Gets the status of the edu.wpi.cs3733d18.teamS.database (open/closed).
     */
    boolean getIsOpen(); // gets open status of edu.wpi.cs3733d18.teamS.database
}

