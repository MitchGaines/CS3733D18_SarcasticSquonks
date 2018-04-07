package service;

import user.LoginHandler;
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
     * Creating three dummy types, adding appropriate staff as the fulfillers
     */
    public static void createDummyTypes() {
        LoginHandler.__generateDummyUsers();
        HashSet<User> doctors = new HashSet<>();
        HashSet<User> staff = new HashSet<>();
        HashSet<User> admins = new HashSet<>();

        doctors.add(LoginHandler.getUsers().get(0));
        admins.add(LoginHandler.getUsers().get(1));
        staff.add(LoginHandler.getUsers().get(2));

        createServiceType("Medical", true, doctors);
        createServiceType("Custodial", false, staff);
        createServiceType("Administrative", false, admins);
    }

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
    public boolean isEmergency() {
        return emergency;
    }

    /**
     * Retrieves the name of the Service Type.
     * @return the name of the Service Type.
     */
    public String getName() {
        return name;
    }

    /**
     * Stores a HashSet of users.
     */
    private HashSet<User> fulfillers = new HashSet<>();

    private static HashSet<ServiceType> serviceTypes = new HashSet<>();

    private ServiceType(String name, boolean emergency, HashSet<User> fulfillers) {
        this.name = name;
        this.emergency = emergency;
        this.fulfillers = fulfillers;
    }

    public static HashSet<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public static ServiceType createServiceType(String name, boolean emergency, HashSet<User> fulfillers) {
        ServiceType new_service = new ServiceType(name, emergency, fulfillers);
        serviceTypes.add(new_service);
        return new_service;
    }

    @Override
    public String toString() {
        return getName();
    }

    public HashSet<User> getFulfillers() {
        return fulfillers;
    }

}
