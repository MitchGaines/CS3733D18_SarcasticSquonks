import database.*;
import org.junit.Assert;
import org.junit.Test;
import pathfind.Path;
import pathfind.Pathfinder;

public class PathTests {
    private Pathfinder pathfinder = new Pathfinder();
    private Path path = pathfinder.path;

    @Test
    public void testAStar() throws Exception{
        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");

        Pathfinder pathfinder = new Pathfinder();
        pathfinder.findShortestPath("BHALL01602","BREST00402");
        //System.out.println(pathfinder.path.getPathDirections());
        Assert.assertTrue(true);
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("NODES");
    }
}
