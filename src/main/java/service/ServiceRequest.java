package service;

import data.Node;
import user.User;

import java.util.HashSet;

public class ServiceRequest {

    private static HashSet<ServiceRequest> service_requests = new HashSet<>();
    String title, description;
    ServiceType service_type;
    private User requester, fulfiller;


    /**
     * Creates a new service request. This method is only used within this class, createServiceRequest() should normally be used
     * @param title the name of the service request
     * @param description a brief description
     * @param type the type of service requested
     * @param requester the user requesting the service
     * @param location the location where the service is needed
     */
    private ServiceRequest(String title, String description, ServiceType type, User requester, Node location) {
        this.title = title;
        this.description = description;
        this.service_type = type;
        this.requester = requester;
        this.location = location;
    }

    /**
     * Returns all service requests, even completed ones
     * @return a hashset of all service requests, both completed and not
     */
    public static HashSet<ServiceRequest> getServiceRequests() {
        return service_requests;
    }

    private Node location;

    /**
     * Gets a hashset of all service requests that need to be fulfilled
     * @return all services without a fulfiller
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
     * Creates a new service request
     * @param title the title of the request
     * @param description a brief description
     * @param type the type of request
     * @param requester the user requesting the service
     * @param location the location where the service is needed
     */
    public static void createService(String title, String description, ServiceType type, User requester, Node location) {
        service_requests.add(new ServiceRequest(title, description, type, requester, location));
    }

    /**
     * Marks the specified user as the fulfiller of the service request
     * @param user the user who fulfills the service request
     * @return true if the service request was successfully marked as fulfilled, or false if it was fulfilled already
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
     * Returns whether or not the service request is fulfilled (whether or not a fulfiller exists)
     * @return whether or not the service request is fulfilled
     */
    public boolean isFulfilled() {
        return fulfiller != null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceType getServiceType() {
        return service_type;
    }

    public void setService_type(ServiceType service_type) {
        this.service_type = service_type;
    }


    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getFulfiller() {
        return fulfiller;
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }
}
