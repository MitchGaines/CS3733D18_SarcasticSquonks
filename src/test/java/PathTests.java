import org.junit.Assert;
import org.junit.Test;
import pathfind.Path;
import pathfind.AStarNode;
import pathfind.Pathfinder;

import java.util.ArrayList;

public class PathTests {
    private Pathfinder pathfinder = new Pathfinder();
    private Path path = pathfinder.path;

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
        path.setA_star_node_path(pathList);
        String description = path.getPathDirections();
        Assert.assertTrue(description.contains("First, begin walking "));
        //System.out.println(path.getPathDirections());
    }

}
