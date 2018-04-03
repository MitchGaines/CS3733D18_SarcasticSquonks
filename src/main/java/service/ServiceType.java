package service;

import user.User;

import java.util.HashSet;

/**
 * Defines a ServiceType.
 * @author Mathew McMillian
 * @version 1.0
 *
 */
public class ServiceType {
    /**
     * Stores the name of the Service Type.
     */
    private String name;

    /**
     * Stores whether the Service Type is an emergency.
     */
    private boolean emergency;

    /**
     * Retrieves the value of the emergency field for the object.
     * @return the emergency field for the object.
     */
    private boolean isEmergency() {
        return emergency;
    }

    /**
     * Retrieves the name of the Service Type.
     * @return the name of the Service Type.
     */
    private String getName() {
        return name;
    }

    /**
     * Stores a HashSet of users.
     */
    private HashSet<User> fulfillers;


}
