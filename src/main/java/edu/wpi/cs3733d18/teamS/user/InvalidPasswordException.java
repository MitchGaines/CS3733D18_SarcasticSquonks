package edu.wpi.cs3733d18.teamS.user;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("Invalid password");
    }
}