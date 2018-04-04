package user;

/**
 * a class to be thrown to when there is a user name related error.
 * @author Mathew Puentes
 * @version %I%, %G%
 *
 */
public class InvalidUsernameException extends Exception{
    public InvalidUsernameException(){
        super("Invalid username");
    }
}
