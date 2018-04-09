package pathfind;

public class NoPathFoundException extends Exception {
    public NoPathFoundException() {
        super("Ran out of neighbors, no pathfinder_path available");
    }
}
