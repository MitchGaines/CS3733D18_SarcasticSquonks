package user;

/**
 * RegularStaff is a Class for the specific user type RegularStaff.
 * @author Danny Sullivan
 * @version %I%, %G%
 *
 */
public class RegularStaff extends Staff {

    /**
     * Constructs a regular staff member which has all the functionality of an
     * Admin staff member except the ability to edit the map.
     * @param username the user's username.
     * @param password the user's password.
     */
    public RegularStaff(String username, String password){
        super(username, password);
        type = user_type.REGULAR_STAFF;
    }

}
