package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * Parses a CSV file and writes to a table.
 * @Author: Joseph Turcotte
 * @version 1.0
 * Date: March 24, 2018
 * Sources: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 *          https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
 */
public class CSVReader { // TODO: the code seems a bit repetitive, but this is only called once at program startup

    /**
     * Stores null for the buffered reader.
     */
    private BufferedReader buffered_reader = null; // reader for file
    /**
     * Stores the database.
     */
    private IDatabase database;

    /**
     * Constructor for a CSVReader.
     * @param database the database that holds the tables.
     */
    public CSVReader(IDatabase database) {
        this.database = database;
    }

    /**
     * Loads a CSV file and populates a JavaDB table at the start of the program.
     * @param csv_file the name of the csv file to read from.
     * @param table_name the name of the table to create.
     */
    public void readCSVFile(String csv_file, String table_name) {

        // parsing variables
        String line; // modified by each line read in
        String csv_delimiter = ","; // delimiter for CSV files

        try {
            buffered_reader = new BufferedReader(new FileReader(csv_file));

            // skip first line (column names)
            line = buffered_reader.readLine();

            // check the table type, then loop through each line of the CSV file
            if (table_name.equals("NODES")) {
                while ((line = buffered_reader.readLine()) != null) {

                    // split line using comma delimiter
                    String[] values = line.split(csv_delimiter);

                    // add quotes so SQL won't complain
                    values[0] = database.addQuotes(values[0]);
                    values[3] = database.addQuotes(values[3]);
                    values[4] = database.addQuotes(values[4]);
                    values[5] = database.addQuotes(values[5]);
                    values[6] = database.addQuotes(values[6]);
                    values[7] = database.addQuotes(values[7]);
                    values[8] = database.addQuotes(values[8]);

                    // add values to table
                    database.insert(table_name, values);
                }
            } else if (table_name.equals("EDGES")) {
                while ((line = buffered_reader.readLine()) != null) {

                    // split line using comma delimiter
                    String[] values = line.split(csv_delimiter);

                    // add quotes so SQL won't complain
                    values[0] = database.addQuotes(values[0]);
                    values[1] = database.addQuotes(values[1]);
                    values[2] = database.addQuotes(values[2]);

                    // add values to table
                    database.insert(table_name, values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close the buffered reader, if any
            if (buffered_reader != null) {
                try {
                    buffered_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println("File read successfully!");
    }
}
