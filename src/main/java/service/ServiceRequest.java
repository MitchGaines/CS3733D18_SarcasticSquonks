package service;

import user.User;

import java.util.HashSet;

/**
 *
 *
 *
 */
public class ServiceRequest {

    /**
     * Stores a HashSet of Service Requests.
     */
    private static HashSet<ServiceRequest> service_requests = new HashSet<>();

    /**
     * Stores the title and a description.
     */
    String title, description;

    /**
     * Stores the type of service.
     */
    ServiceType service_type;

    /**
     * Stores the User who made a request and the one who is doing the task
     */
    private User requester, fulfiller;


    /**
     * Creates a new service request. This method is only used within this class,
     * createServiceRequest() should normally be used.
     * @param title the name of the service request.
     * @param description a brief description.
     * @param type the type of service requested.
     * @param requester the user requesting the service.
     * @param location the location where the service is needed.
     */
    private ServiceRequest(String title, String description, ServiceType type, User requester, String location) {
        this.title = title;
        this.description = description;
        this.service_type = type;
        this.requester = requester;
        this.location = location;
    }

    /**
     * Returns all service requests, even completed ones.
     * @return a hashset of all service requests, both completed and not.
     */
    public static HashSet<ServiceRequest> getServiceRequests() {
        return service_requests;
    }

    private String location;

    /**
     * Gets a hashset of all service requests that need to be fulfilled.
     * @return all services without a fulfiller.
     */
    public static HashSet<ServiceRequest> getUnfulfilledServiceRequests() {
        HashSet<ServiceRequest> to_return = new HashSet<>();
        for (ServiceRequest sr : service_requests) {
            if (!sr.isFulfilled()) {
                to_return.add(sr);
            }
        }
        return to_return;
    }

    /**
     * Creates a new service request.
     * @param title the title of the request.
     * @param description a brief description.
     * @param type the type of request.
     * @param requester the user requesting the service.
     * @param location the location where the service is needed.
     */
    public static void createService(String title, String description, ServiceType type, User requester, String location) {
        ServiceRequest sr = new ServiceRequest(title, description, type, requester, location);
        service_requests.add(sr);

        ServiceLogEntry.log(sr, false);
    }

    public User getRequester() {
        return requester;
    }

    /**
     * Marks the specified user as the fulfiller of the service request.
     * @param user the user who fulfills the service request.
     * @return true if the service request was successfully marked as fulfilled, or false if it was fulfilled already.
     */
    public boolean fulfill(User user) {
        if (!isFulfilled()) {
            fulfiller = user;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether or not the service request is fulfilled (whether or not a fulfiller exists).
     * @return whether or not the service request is fulfilled.
     */
    public boolean isFulfilled() {
        return fulfiller != null;
    }

    /**
     * Retrieves the title.
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * @param title the title of the request.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the description of the request.
     * @return the description of the request.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the service request.
     * @param description a description of the service request.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the Service Type.
     * @return the service type.
     */
    public ServiceType getServiceType() {
        return service_type;
    }

    /**
     * Sets the service type.
     * @param service_type the type of service.
     */
    public void setService_type(ServiceType service_type) {
        this.service_type = service_type;
    }

    /**
     * Sets the user requesting the service.
     * @param requester
     */
    public void setRequester(User requester) {
        this.requester = requester;
    }

    /**
     * Retrieves the user fulfilling the request.
     * @return the fulfiller.
     */
    public User getFulfiller() {
        return fulfiller;
    }

    /**
     * Retrieves the location of the request.
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the request.
     * @param location the location of the request.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
