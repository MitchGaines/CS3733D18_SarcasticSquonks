package user;

/**
 * AdminStaff.java
 * Class for user type AdminStaff
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public class AdminStaff extends Staff {

    public AdminStaff(String username, String password){
        super(username, password);
        type = user_type.ADMIN_STAFF;
    }
}
