package user;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * User.java
 * Abstract class for all possible user types
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public abstract class User {
    private boolean can_mod_map;
    private String username;
    private byte[] password_salt;
    private byte[] enc_password;
    public enum user_type{DOCTOR, ADMIN_STAFF, REGULAR_STAFF}
    user_type type;

    protected User(String username, String password){
        password_salt = new byte[16];
        new SecureRandom().nextBytes(password_salt);

        this.username = username;

        byte[] password_unsalted = password.getBytes();
        byte [] password_salted = new byte[password_unsalted.length + password_salt.length];
        System.arraycopy( password_unsalted, 0, password_salted, 0, password_unsalted.length);
        System.arraycopy( password_salt, 0, password_salted, password_unsalted.length, password_salt.length );

        enc_password = Base64.getEncoder().encode(password_salted);
    }

    /**
     * Takes a Boolean as a parameter and sets the permission
     * for the User if they are able to modify the map or not.
     *
     * @param if_can_mod_map is a boolean value, true if map can be
     *                       modified by the user, false otherwise
     */
    public void setCanModMap(boolean if_can_mod_map) {
        can_mod_map = if_can_mod_map;
    }

    /**
     * Returns whether or not the User has the ability to modify the map
     *
     * @return true if User can modify map, false otherwise
     */
    public boolean canModMap() {
        return can_mod_map;
    }

    /**
     * Takes a String as a parameter and changes the username of the current
     * User to the String in the parameter
     *
     * @param new_username the desired new username
     */
    public void setUsername(String new_username) {
        username = new_username;
    }

    /**
     * Returns the username of the current User
     *
     * @return username of the User
     */
    public String getUsername() {
        return username;
    }

    public byte[] getEncodedPassword() {
        return enc_password;
    }

    public byte[] getPasswordSalt() {
        return password_salt;
    }

    public user_type getType() {
        return type;
    }
}
