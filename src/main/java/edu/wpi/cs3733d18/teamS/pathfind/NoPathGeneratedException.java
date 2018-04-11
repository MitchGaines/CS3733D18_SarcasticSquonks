package edu.wpi.cs3733d18.teamS.pathfind;

public class NoPathGeneratedException extends Exception {
    public NoPathGeneratedException(){
        super("can't generated direction on an empty pathfinder_path");
    }
}
