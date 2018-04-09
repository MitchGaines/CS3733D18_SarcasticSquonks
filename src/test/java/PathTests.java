import database.*;
import org.junit.Test;
import pathfind.*;
import pathfind.Path;
import pathfind.Pathfinder;


public class PathTests {
    private Pathfinder pathfinder = new Pathfinder(new AStar());
    private Path path = pathfinder.pathfinder_path;

    @Test
    public void testAStar() throws Exception{
        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");

        Pathfinder pathfinder = new Pathfinder(new AStar());
        pathfinder.findShortestPath("BHALL01602","BREST00402");
        //System.out.println(pathfinder.pathfinder_path.getPathDirections());
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("NODES");
    }

    @Test
    public void testBreadthFirstNearestLocation() {
        // set database and storage class
        Storage storage = Storage.getInstance();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        // read from CSV files
        CSVReader csv_reader = new CSVReader(storage.getDatabase());
        csv_reader.readCSVFile("csv/MapBNodes.csv", "NODES");
        csv_reader.readCSVFile("csv/MapBEdges.csv", "EDGES");

        Pathfinder pathfinder = new Pathfinder(new BreadthFirst());
        pathfinder.findShortestPath("BHALL01602","DEPT");
        //System.out.println(pathfinder.pathfinder_path.getPathDirections().toString());
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("NODES");
    }
}
