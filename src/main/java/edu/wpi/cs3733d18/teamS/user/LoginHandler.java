package edu.wpi.cs3733d18.teamS.user;

import edu.wpi.cs3733d18.teamS.database.Storage;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;

public class LoginHandler {

    public LoginHandler() {}

    private Storage storage = Storage.getInstance();

    /**
     * Generates an initial list of users to be stored in the database
     */
    public static void generateInitialUsers() {
        Storage storage = Storage.getInstance();

        // generate initial user objects to be stored in the database
        User u1 = new User("doctor", "doctor", User.user_type.DOCTOR, false);
        User u2 = new User("admin", "admin", User.user_type.ADMIN_STAFF, true);
        User u3 = new User("staff", "staff", User.user_type.REGULAR_STAFF, false);
        User u4 = new User("cardiocarl", "123", User.user_type.DOCTOR, false);
        User u5 = new User("plasticspete", "123", User.user_type.DOCTOR, false);
        User u6 = new User("spanishsue", "123", User.user_type.REGULAR_STAFF, false);
        User u7 = new User("russianrima", "123", User.user_type.REGULAR_STAFF, false);

        // save users to database
        storage.saveUser(u1);
        storage.saveUser(u2);
        storage.saveUser(u3);
        storage.saveUser(u4);
        storage.saveUser(u5);
        storage.saveUser(u6);
        storage.saveUser(u7);
    }

    /**
     * Login method in handler
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return the user who just logged in, or an exception
     * @throws InvalidUsernameException the credentials were incorrect
     */
    public User login(String username, String password) throws InvalidUsernameException {
        String encoded_password = User.encodePassword(password);
        User u = storage.getUserByCredentials(username, encoded_password);

        if (u != null) {
            return u;
        } else {
            throw new InvalidUsernameException();
        }
    }
}

