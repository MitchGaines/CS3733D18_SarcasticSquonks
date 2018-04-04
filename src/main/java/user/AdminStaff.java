package user;

/**
 * A Class for the specific user type AdminStaff.
 * @author Danny Sullivan
 * @version %I%, %G%
 */
public class AdminStaff extends Staff {

    /**
     * Constructs an Admin Staff member.
     * @param username the username.
     * @param password the password.
     */
    public AdminStaff(String username, String password){
        super(username, password);
        type = user_type.ADMIN_STAFF;
    }
}
