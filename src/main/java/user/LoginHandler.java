package user;
        import database.ApacheDatabase;
        import database.CSVReader;
        import database.Storage;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Base64;

public class LoginHandler {
    private static ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public LoginHandler(){
        /*
        Storage storage = new Storage();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        CSVReader user_table_reader = new CSVReader(storage.getDatabase());
        user_table_reader.readCSVFile("users", "user table");
        */

        //users = storage.getAllUsers(); //This isn't actually implemented yet.
    }

    /**
     * This class is used in place of an actual database implementation, will be removed
     *
     */
    public static void __generateDummyUsers(){
        User u1 = new Doctor("Doctor", "Password");
        User u2 = new AdminStaff("Admin", "SecurePassword");
        User u3 = new RegularStaff("Bob", "abc");
        users.add(u1);
        users.add(u2);
        users.add(u3);
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

