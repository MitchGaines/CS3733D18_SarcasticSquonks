/**
 * CSVTestSuite.java
 * A test suite for CSV reading and writing
 * Author: Joseph Turcotte
 * Date: March 28, 2018
 */

import edu.wpi.cs3733d18.teamS.database.ApacheDatabase;
import edu.wpi.cs3733d18.teamS.database.CSVReader;
import edu.wpi.cs3733d18.teamS.database.CSVWriter;
import edu.wpi.cs3733d18.teamS.database.Storage;
import org.junit.After;
import org.junit.Before;

public class CSVTestSuite {

    private Storage storage;
    private CSVReader csv_reader;
    private CSVWriter csv_writer;
    private String table_name_nodes = "NODES";
    private String table_name_edges = "EDGES";

    /**
     * Set up tests for CSV
     */
    @Before
    public void setUp() {

        storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/newNodeFile.csv", table_name_nodes);
        csv_reader.readCSVFile("csv/newEdgeFile.csv", table_name_edges);

        csv_writer = new CSVWriter(storage.getDatabase());
    }

    /**
     * Test that the files were read in correctly
     */
    /*
    @Test
    public void testRead() {

        List<Node> nodes = storage.getAllNodes();
        Assert.assertEquals(nodes.size(), 70);

        // check the 7th entry to make sure it was read correctly
        Node node6 = nodes.get(6);
        Assert.assertEquals(node6.getNodeID(), "BDEPT00602");
        Assert.assertEquals(node6.getXCoord(), 2782);
        Assert.assertEquals(node6.getYCoord(), 1303);

        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 90);

        // check the 7th entry to make sure it was read correctly
        Edge edge6 = edges.get(6);
        Assert.assertEquals(edge6.getEdgeID(), "BHALL00202_BHALL00502");
        Assert.assertEquals(edge6.getStartNode(), "BHALL00202");
        Assert.assertEquals(edge6.getEndNode(), "BHALL00502");
    }
    */

    /**
     * Test that the write worked correctly
     */
    /*
    @Test
    public void testWrite() {

        csv_writer.writeCSVFile("csv/newNodeFile.csv", "NODES");
        csv_writer.writeCSVFile("csv/newEdgeFile.csv", "EDGES");

        // re-create tables
        storage.setDatabase(storage.getDatabase());

        // read the files we just wrote to
        csv_reader.readCSVFile("csv/newNodeFile.csv", table_name_nodes);
        List<Node> nodes = storage.getAllNodes();
        Assert.assertEquals(nodes.size(), 70);

        csv_reader.readCSVFile("csv/newEdgeFile.csv", table_name_edges);
        List<Edge> edges = storage.getAllEdges();
        Assert.assertEquals(edges.size(), 90);
    }
    */
    @After
    public void breakDown() {
        // drop tables at the end
        storage.getDatabase().dropTable("NODES");
        storage.getDatabase().dropTable("EDGES");
    }
}
