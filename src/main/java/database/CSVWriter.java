package database;

/**
 * CSVWriter.java
 * Parses a table and writes to a CSV file
 * Author: Joseph Turcotte
 * Date: March 24, 2018
 * Sources: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 *          https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
 */

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.*;

public class CSVWriter { // TODO: the code seems a bit repetitive, but this is only called once at program shutdown

    // fields
    private ApacheDatabase data; // need the database so we can see the table

    /**
     * Constructor for a CSVReader
     * @param data the database to read data from
     */
    public CSVWriter(ApacheDatabase data) {
        this.data = data;
    }

    /**
     * Reads from a JavaDB table and writes the entries to a csv file at the end of the program
     * @param csv_file the name of the file to write to
     * @param table_name the name of the table to read from
     */
    public void writeCSVFile(String csv_file, String table_name) {

        // variables for writing
        FileWriter file_writer = null; // file writer for CSV file
        String file_header = ""; // header of CSV file

        // change table header based on table
        if (table_name.equals("NODES")) {
            file_header = "node_id,x_coord,y_coord,floor,building,node_type," +
                    "long_name,short_name,team_assigned,x_coord_3d,y_coord_3d";
        } else if (table_name.equals("EDGES")) {
            file_header = "edge_id,start_node,end_node";
        }

        // silently return if table doesn't exist
        if(!data.doesTableExist(table_name)){
            return;
        }

        try {
            file_writer = new FileWriter(csv_file);

            // write header to file
            file_writer.append(file_header);
            file_writer.append("\n");

            // query the database to get the table
            ResultSet r_set = data.query(table_name, null, null, null,null);

            // check type of table and then loop through it
            if (table_name.equals("NODES")) {

                // variables to hold values of each row
                int x_coord, y_coord, x_coord_3d, y_coord_3d;
                String node_id, floor, building, node_type, long_name, short_name, team_assigned;

                while (r_set.next()) {
                    // get values from each row
                    node_id = r_set.getString("node_id");
                    x_coord = r_set.getInt("x_coord");
                    y_coord = r_set.getInt("y_coord");
                    floor = r_set.getString("floor");
                    building = r_set.getString("building");
                    node_type = r_set.getString("node_type");
                    long_name = r_set.getString("long_name");
                    short_name = r_set.getString("short_name");
                    team_assigned = r_set.getString("team_assigned");
                    x_coord_3d = r_set.getInt("x_coord_3d");
                    y_coord_3d = r_set.getInt("y_coord_3d");

                    // write values to file, separated by commas
                    file_writer.append(node_id + "," + x_coord + "," + y_coord + "," + floor + ","
                            + building + "," + node_type + "," + long_name + "," + short_name + "," + team_assigned +
                            "," + x_coord_3d + "," + y_coord_3d + "\n");
                }
            } else if (table_name.equals("EDGES")) {

                // variables to hold values of each row
                String edge_id, start_node, end_node;

                while (r_set.next()) {
                    edge_id = r_set.getString("edge_id");
                    start_node = r_set.getString("start_node");
                    end_node = r_set.getString("end_node");

                    // write values to file, separated by commas
                    file_writer.append(edge_id + "," + start_node + "," + end_node + "\n");
                }
            }

            // close result set
            r_set.close();

            // drop the table
            data.dropTable(table_name);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // close the file writer
                file_writer.flush();
                file_writer.close();
            } catch (IOException e) {
                System.out.println("Error while flushing and closing file writer!");
                e.printStackTrace();
            }
        }

        //System.out.println("Wrote to file successfully!");
    }
}
