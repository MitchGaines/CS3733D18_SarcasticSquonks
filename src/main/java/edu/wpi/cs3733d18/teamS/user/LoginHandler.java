package edu.wpi.cs3733d18.teamS.user;

import edu.wpi.cs3733d18.teamS.database.Storage;

import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;

public class LoginHandler {
    private static LinkedList<User> users = new LinkedList<>();

    public LoginHandler() {
        Storage storage = Storage.getInstance();

//        users = (LinkedList<User>)storage.getAllUsers();
        __generateDummyUsers(); // TODO no need to generate dummies
    }

    public static LinkedList<User> getUsers() {
        return users;
    }

    /**
     * This class is used in place of an actual edu.wpi.cs3733d18.teamS.database implementation, used for testing purposes
     */
    public static void __generateDummyUsers() {
        Storage storage = Storage.getInstance();

        User u1 = new User("doctor", "doctor", User.user_type.DOCTOR, false);
        User u2 = new User("admin", "admin", User.user_type.ADMIN_STAFF, true);
        User u3 = new User("staff", "staff", User.user_type.REGULAR_STAFF, false);
        User u4 = new User("cardiocarl", "123", User.user_type.DOCTOR, false);
        User u5 = new User("plasticspete", "123", User.user_type.DOCTOR, false);
        User u6 = new User("spanishsue", "123", User.user_type.REGULAR_STAFF, false);
        User u7 = new User("russianrima", "123", User.user_type.REGULAR_STAFF, false);


        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);
        users.add(u7);

        storage.saveUser(u1);
        storage.saveUser(u2);
        storage.saveUser(u3);
        storage.saveUser(u4);
        storage.saveUser(u5);
        storage.saveUser(u6);
        storage.saveUser(u7);
    }

    public User login(String username, String password) throws InvalidPasswordException, InvalidUsernameException {
        for (User user : users) {
            String user_username = user.getUsername();

            if (username.equals(user_username)) {
                byte[] user_salt = user.getPasswordSalt();
                byte[] decoded_salted_password = Base64.getDecoder().decode(user.getEncodedPassword());
                byte[] decoded_password_bytes = Arrays.copyOfRange(decoded_salted_password, 0,
                        decoded_salted_password.length - user_salt.length);
                String decoded_password = new String(decoded_password_bytes);

                if (password.equals(decoded_password)) {
                    return user;
                } else {
                    throw new InvalidPasswordException();
                }
            }
        }
        throw new InvalidUsernameException();
    }
}

