package user;

        import database.ApacheDatabase;
        import database.CSVReader;
        import database.Storage;
        import sun.misc.BASE64Decoder;

        import java.util.List;

public class LoginHandler {
    private List<User> users;
    BASE64Decoder decoder = new BASE64Decoder();

    public LoginHandler(){
        Storage storage = new Storage();
        storage.setDatabase(new ApacheDatabase("apacheDB"));

        CSVReader user_table_reader = new CSVReader(storage.getDatabase());
        user_table_reader.readCSVFile("users", "user table");

        //users = storage.getAllUsers(); //This isn't actually implemented yet.
    }

    public User login(String username, String password) throws InvalidPasswordException, InvalidUsernameException{
        for (User user:users) {
            //TODO: actually use the user's username.
            String user_username = user.getUsername();
            user_username = "DoctorTom";
            if (username.equals(user_username)) {
                //TODO: actually decode the password.
                String decoded_password = "123pass";
                if (password.equals(decoded_password)) {
                    return user;
                }
                throw new InvalidPasswordException();
            }
        }
        throw new InvalidUsernameException();
    }
}

