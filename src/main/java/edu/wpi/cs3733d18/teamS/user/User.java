package edu.wpi.cs3733d18.teamS.user;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * User.java
 * Class for all possible edu.wpi.cs3733d18.teamS.user types
 * Author: Danny Sullivan
 * Date: March 31, 2018
 * Modified by: Joseph Turcotte
 * Date: April 6, 2018
 */

public class User {
    user_type type;
    private long user_id;
    private boolean can_mod_map;
    private String username;
    private String first_name;
    private String last_name;
    private byte[] password_salt;
    private byte[] enc_password;

    public User(String username, String password, String first_name, String last_name, user_type type, boolean can_mod_map) {
        password_salt = new byte[16];
        //new SecureRandom().nextBytes(password_salt);

        this.username = username;

        byte[] password_unsalted = password.getBytes();
        byte[] password_salted = new byte[password_unsalted.length + password_salt.length];
        System.arraycopy(password_unsalted, 0, password_salted, 0, password_unsalted.length);
        System.arraycopy(password_salt, 0, password_salted, password_unsalted.length, password_salt.length);

        enc_password = Base64.getEncoder().encode(password_salted);

        this.first_name = first_name;
        this.last_name = last_name;
        this.type = type;
        this.can_mod_map = can_mod_map;
    }

    /**
     * Encodes a password
     *
     * @param password the password as a string
     * @return the password, hashed and salted (as a String)
     */
    public static String encodePassword(String password) {
        byte[] password_salt = new byte[16];
        //new SecureRandom().nextBytes(password_salt);

        byte[] password_unsalted = password.getBytes();
        byte[] password_salted = new byte[password_unsalted.length + password_salt.length];
        System.arraycopy(password_unsalted, 0, password_salted, 0, password_unsalted.length);
        System.arraycopy(password_salt, 0, password_salted, password_unsalted.length, password_salt.length);

        return new String(Base64.getEncoder().encode(password_salted));
    }

    /**
     * Takes a Boolean as a parameter and sets the permission
     * for the User if they are able to modify the map or not.
     *
     * @param if_can_mod_map is a boolean value, true if map can be
     *                       modified by the edu.wpi.cs3733d18.teamS.user, false otherwise
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
     * Returns the username of the current User
     *
     * @return username of the User
     */
    public String getUsername() {
        return username;
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

    public void setPassword(String new_password) {
        password_salt = new byte[16];
        byte[] password_unsalted = new_password.getBytes();
        byte[] password_salted = new byte[password_unsalted.length + password_salt.length];
        System.arraycopy(password_unsalted, 0, password_salted, 0, password_unsalted.length);
        System.arraycopy(password_salt, 0, password_salted, password_unsalted.length, password_salt.length);
        enc_password = Base64.getEncoder().encode(password_salted);
    }

    public byte[] getEncodedPassword() {
        return enc_password;
    }

    public byte[] getPasswordSalt() {
        return password_salt;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public user_type getType() {
        return type;
    }

    public void setType(user_type ut) {
        type = ut;
    }

    public long getUserID() {
        return user_id;
    }

    public void setUserID(long new_id) {
        user_id = new_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", can_mod_map=" + can_mod_map +
                ", username='" + username + '\'' +
                ", password_salt=" + Arrays.toString(password_salt) +
                ", enc_password=" + Arrays.toString(enc_password) +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id == user.user_id &&
                can_mod_map == user.can_mod_map &&
                Objects.equals(username, user.username) &&
                type == user.type;
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(user_id, can_mod_map, username, type);
        result = 31 * result + Arrays.hashCode(password_salt);
        result = 31 * result + Arrays.hashCode(enc_password);
        return result;
    }

    public enum user_type {DOCTOR, ADMIN_STAFF, REGULAR_STAFF}
}
