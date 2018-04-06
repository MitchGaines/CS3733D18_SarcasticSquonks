package user;

/**
 * A class to be thrown to when there is a password related error.
 * @author Danny Sullivan
 * @version %I%, %G%
 *
 */
public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){
        super("Invalid password");
    }
}