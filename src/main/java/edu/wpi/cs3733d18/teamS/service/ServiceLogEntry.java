package edu.wpi.cs3733d18.teamS.service;

import edu.wpi.cs3733d18.teamS.user.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class deals with the logging of the edu.wpi.cs3733d18.teamS.service requests.
 *
 * @author Mathew McMillan
 * @version "%I%, %G%"
 */
public class ServiceLogEntry {
    /**
     * Stores a new ArrayList of ServiceLogEntry for the class.
     */
    private static ArrayList<ServiceLogEntry> log = new ArrayList<>();
    /**
     * Stores the Date of the Request.
     */
    Date time = new Date();
    /**
     * Stores a ServiceRequest.
     */
    private ServiceRequest service_request;
    /**
     * Stores whether or not the request has been completed.
     */
    private boolean completed;

    /**
     * Constructs a ServiceLogEntry by using a ServiceRequest and a boolean value.
     *
     * @param service_request the request being made.
     * @param completed       a boolean that determines whether or not the request has been completed.
     */
    private ServiceLogEntry(ServiceRequest service_request, boolean completed) {
        this.service_request = service_request;
        this.completed = completed;
    }

    /**
     * Adds a ServicLogEntry to log.
     *
     * @param service_request ServiceRequest being made.
     * @param completed       boolean for whether the task has been completed.
     */
    public static void log(ServiceRequest service_request, boolean completed) {
        log.add(new ServiceLogEntry(service_request, completed));
    }

    /**
     * Retrieves the current list of the Log.
     *
     * @return the ArrayList of ServiceLogEntry's.
     */
    public static ArrayList<ServiceLogEntry> getOverallLog() {
        return log;
    }

    /**
     * Creates an ArrayList of all the entries the edu.wpi.cs3733d18.teamS.user is involved with, whether they were a requester or fulfiller.
     *
     * @param user The edu.wpi.cs3733d18.teamS.user who has either made or fulfilled requests.
     * @return The ArrayList of ServiceLogEntry's the edu.wpi.cs3733d18.teamS.user is involved in.
     */
    public static ArrayList<ServiceLogEntry> getLogForUser(User user) {
        ArrayList<ServiceLogEntry> to_return = new ArrayList<>();
        for (ServiceLogEntry e : log) {
            if (userInvolved(user, e.getService_request())) {
                to_return.add(e);
            }
        }
        return to_return;
    }

    /**
     * Checks to see if a edu.wpi.cs3733d18.teamS.user is involved as either the fulfiller or requester of a particular ServiceRequest.
     *
     * @param user            The edu.wpi.cs3733d18.teamS.user who has either made or fulfilled the request.
     * @param service_request The request that the edu.wpi.cs3733d18.teamS.user either is or is not involved with.
     * @return True if the edu.wpi.cs3733d18.teamS.user is involved as the fulfiller or requester and false if they are not either.
     */
    private static boolean userInvolved(User user, ServiceRequest service_request) {
        return (service_request.getServiceType().getFulfillers().contains(user)) ||
                (service_request.getRequester() != null && service_request.getRequester() == user);
    }

    /**
     * Retrieves the ServiceRequest.
     *
     * @return the ServiceRequest of the ServiceLogEntry.
     */
    public ServiceRequest getService_request() {
        return service_request;
    }

    /**
     * Retrieves the boolean value for whether or not the request has been fulfilled.
     *
     * @return true if the task has been completed, false if it has not.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Retrieves the date.
     *
     * @return the date.
     */
    public Date getTime() {
        return time;
    }

}
