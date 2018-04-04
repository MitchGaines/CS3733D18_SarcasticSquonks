package pathfind;

public class NoPathGeneratedException extends Exception {
    public NoPathGeneratedException(){
        super("can't generated direction on an empty path");
    }
}
