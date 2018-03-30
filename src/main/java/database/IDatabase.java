package database;

/*
 * IDatabase
 * Author: Joseph Turcotte
 * Date: March 24, 2018
 */

import java.sql.ResultSet;

public interface IDatabase {

    void connect(); // to connect to the embedded driver
    void disconnect(); // to disconnect from the embedded driver

    boolean doesTableExist(String table_name); // checks for table existence

    void createTable(String table_name, String[] columns); // creates a table
    void dropTable(String table_name); // deletes a table

    ResultSet query(String tableName, String[] columns,
                    String where_condition, String[] where_args, String order_by); // executes a query

    void insert(String table_name, String[] values); // inserts an entry into a table
    void update(String table_name, String[] values, String where_condition, String[] where_args); // updates an entry in a table
    void delete(String table_name, String where_condition, String[] where_args); // deletes an entry from a table

    public String addQuotes(String s); // add quotes to a string

    public boolean getIsOpen(); // gets open status of database
}

