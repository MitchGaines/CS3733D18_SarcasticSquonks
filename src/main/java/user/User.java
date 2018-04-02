package user;

public abstract class User {
    private boolean can_mod_map;
    private String username;
    private byte[] password_salt;
    private enum user_type{}

    /**
     * Sets it so that current User can modify map
     */
    public void setCanModMap() {
        can_mod_map = true;
    }

    /**
     * Sets it so that current User cannot modify map
     */
    public void setCannotModMap() {
        can_mod_map = false;
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

    /**
     * Sets the password of the current User to the inputted byte[]
     *
     * @param new_password the desired new password
     */
    public void setPassword(byte[] new_password) {
        password_salt = new_password;
    }

    /**
     * Returns the password of the current User
     *
     * @return password of the current User
     */
    public byte[] getPassword() {
        return password_salt;
    }
}
