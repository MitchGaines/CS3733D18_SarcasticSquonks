package pathfind;

public class NoPathFoundException extends Exception {
    public NoPathFoundException() {
        super("Ran out of neighbors, no path available");
    }
}
