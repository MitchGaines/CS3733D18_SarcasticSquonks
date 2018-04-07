package user;

import database.Storage;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;

public class LoginHandler {
    private static LinkedList<User> users = new LinkedList<User>();

    public static LinkedList<User> getUsers() {
        return users;
    }

    public LoginHandler(){
        Storage storage = Storage.getInstance();

//        CSVReader user_table_reader = new CSVReader(storage.getDatabase()); // TODO move to Main
//        user_table_reader.readCSVFile("users", "user table");

        users = (LinkedList<User>)storage.getAllUsers();
        __generateDummyUsers();
    }

    /**
     * This class is used in place of an actual database implementation, used for testing purposes
     *
     */
    public static void __generateDummyUsers(){
        Storage storage = Storage.getInstance();

        User u1 = new User("Doctor", "Password", User.user_type.DOCTOR);
        User u2 = new User("Admin", "SecurePassword", User.user_type.ADMIN_STAFF);
        User u3 = new User("Bob", "abc", User.user_type.REGULAR_STAFF);
        users.add(u1);
        users.add(u2);
        users.add(u3);

        storage.saveUser(u1);
        storage.saveUser(u2);
        storage.saveUser(u3);
    }

    public User login(String username, String password) throws InvalidPasswordException, InvalidUsernameException {
        for (User user:users) {
            String user_username = user.getUsername();

            if (username.equals(user_username)) {
                byte[] user_salt = user.getPasswordSalt();
                byte[] decoded_salted_password = Base64.getDecoder().decode(user.getEncodedPassword());
                byte[] decoded_password_bytes = Arrays.copyOfRange(decoded_salted_password, 0,
                        decoded_salted_password.length - user_salt.length);
                String decoded_password = new String(decoded_password_bytes);

                if (password.equals(decoded_password)) {
                    return user;
                }else{
                    throw new InvalidPasswordException();
                }
            }
        }
        throw new InvalidUsernameException();
    }
}

