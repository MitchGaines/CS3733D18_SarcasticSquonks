package edu.wpi.cs3733d18.teamS.database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Parses a table and writes to a CSV file.
 *
 * @author Joseph Turcotte
 * @version 1.0
 * Date: March 24, 2018
 * Sources: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 * https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
 */
public class CSVWriter {

    /**
     * Stores the edu.wpi.cs3733d18.teamS.database so we can see the table.
     */
    private IDatabase database; // need the edu.wpi.cs3733d18.teamS.database so we can see the table

    /**
     * Constructor for a CSVWriter
     *
     * @param database the edu.wpi.cs3733d18.teamS.database to read edu.wpi.cs3733d18.teamS.data from
     */
    public CSVWriter(IDatabase database) {
        this.database = database;
    }

    /**
     * Reads from a JavaDB table and writes the entries to a csv file at the end of the program.
     *
     * @param csv_file   the name of the file to write to.
     * @param table_name the name of the table to read from.
     */
    public void writeCSVFile(String csv_file, String table_name) {

        // variables for writing
        FileWriter file_writer = null; // file writer for CSV file
        String file_header = ""; // header of CSV file

        // silently return if table doesn't exist
        if (!database.doesTableExist(table_name)) {
            return;
        }

        // change table header based on table
        if (table_name.equals("NODES")) {
            file_header = "node_id,x_coord,y_coord,floor,building,node_type," +
                    "long_name,short_name,team_assigned,x_coord_3d,y_coord_3d";
//            file_header = "nodeID,xcoord,ycoord,floor,building,nodeType,longName," +
//                    "shortName,teamAssigned,xcoord3d,ycoord3d"; //This is for the team F nodes

        } else if (table_name.equals("EDGES")) {
            file_header = "edge_id,start_node,end_node";
        } else if (table_name.equals("USERS")) {
            file_header = "username,password,type,can_mod_map";
        }

        try {
            file_writer = new FileWriter(csv_file);

            // write header to file
            file_writer.append(file_header);
            file_writer.append("\n");

            // query the edu.wpi.cs3733d18.teamS.database to get the table
            ResultSet r_set = database.query(table_name, null, null, null, null);

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
                    file_writer.append(edge_id).append(",").append(start_node).append(",").append(end_node).append("\n");
                }
            } else if (table_name.equals("USERS")) {

                // variables to hold values of each row
                String username, password, user_type;
                boolean can_mod_map;

                while (r_set.next()) {
                    username = r_set.getString("username");
                    password = r_set.getString("password");
                    user_type = r_set.getString("user_type");
                    can_mod_map = r_set.getBoolean("can_mod_map");

                    // write values to file, separated by commas
                    file_writer.append(username + "," + password + "," + user_type + "," + can_mod_map + "\n");
                }
            }

            // close result set
            r_set.close();

            // drop the table
            database.dropTable(table_name);

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
