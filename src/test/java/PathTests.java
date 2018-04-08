import database.*;
import org.junit.Assert;
import org.junit.Test;
import pathfind.*;
import pathfind.Path;
import pathfind.Pathfinder;

import java.util.ArrayList;

public class PathTests {
    private Pathfinder pathfinder = new Pathfinder(new AStar());
    private Path path = pathfinder.pathfinder_path;

    @Test
    public void testPathDescription(){
        AStarNode node1 = new AStarNode("a1", 0, 0, "first node");
        AStarNode node2 = new AStarNode("a2", 100, 100, "second node");
        AStarNode node3 = new AStarNode("a3", 100, 200, "third node");
        AStarNode node4 = new AStarNode("a4", 300, 100, "forth node");
        AStarNode node5 = new AStarNode("a5", 400, 50, "fifth node");

        node5.setParent(node4);
        node4.setParent(node3);
        node3.setParent(node2);
        node2.setParent(node1);

        ArrayList<AStarNode> pathList = new ArrayList<>();
        pathList.add(node1);
        pathList.add(node2);
        pathList.add(node3);
        pathList.add(node4);
        pathList.add(node5);
        path.setAlgorithm_node_path(pathList);
        String description = path.getPathDirections();
        Assert.assertTrue(description.contains("Directions from: first node to: fifth node"));
    }

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
        System.out.println(pathfinder.pathfinder_path.getPathDirections());
        storage.getDatabase().dropTable("EDGES");
        storage.getDatabase().dropTable("NODES");
    }
}
