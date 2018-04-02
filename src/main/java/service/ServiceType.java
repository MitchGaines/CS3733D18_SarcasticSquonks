package service;

import user.User;

import java.util.HashSet;

public class ServiceType {
    private String name;
    private boolean emergency;

    private boolean isEmergency() {
        return emergency;
    }

    private String getName() {
        return name;
    }

    private HashSet<User> fulfillers;


}
