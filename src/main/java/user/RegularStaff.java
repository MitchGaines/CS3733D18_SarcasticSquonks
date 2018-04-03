package user;

/**
 * RegularStaff.java
 * Class for user type RegularStaff
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public class RegularStaff extends Staff {

    public RegularStaff(String username, String password){
        super(username, password);
        type = user_type.REGULAR_STAFF;
    }

}
