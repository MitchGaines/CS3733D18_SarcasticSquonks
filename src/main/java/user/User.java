package user;

import sun.misc.BASE64Encoder;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Abstract class for all possible user types.
 * @author Danny Sullivan
 * @version %I%, %G%
 *
 */
public abstract class User {
    /**
     * Stores a boolean value for whether a user can modify the map.
     */
    private boolean can_mod_map;

    /**
     * The User's Username.
     */
    private String username;

    /**
     * Stores the salt for the password.
     */
    private byte[] password_salt;

    /**
     * Stores the encrypted password.
     */
    private byte[] enc_password;

    /**
     * Stores the Base 64 encoder.
     */
    private static BASE64Encoder encoder = new BASE64Encoder();

    /**
     * Stores the type of user.
     */
    public enum user_type{DOCTOR, ADMIN_STAFF, REGULAR_STAFF}
    user_type type;

    /**
     * Constructs a User by using its user name and password and then makes the encrypted password.
     * @param username the username of the user.
     * @param password the password of the user.
     */
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
     *                       modified by the user, false otherwise.
     */
    public void setCanModMap(boolean if_can_mod_map) {
        can_mod_map = if_can_mod_map;
    }

    /**
     * Returns whether or not the User has the ability to modify the map.
     *
     * @return true if User can modify map, false otherwise.
     */
    public boolean canModMap() {
        return can_mod_map;
    }

    /**
     * Takes a String as a parameter and changes the username of the current
     * User to the String in the parameter.
     *
     * @param new_username the desired new username.
     */
    public void setUsername(String new_username) {
        username = new_username;
    }

    /**
     * Returns the username of the current User.
     *
     * @return username of the User.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the encrypted password.
     * @return the encrypted password.
     */
    public byte[] getEncodedPassword() {
        return enc_password;
    }

    /**
     * Retrieves the password salt.
     * @return the password salt.
     */
    public byte[] getPasswordSalt() {
        return password_salt;
    }

    /**
     * Retrieves the type of user.
     * @return the user type.
     */
    public user_type getType() {
        return type;
    }
}
