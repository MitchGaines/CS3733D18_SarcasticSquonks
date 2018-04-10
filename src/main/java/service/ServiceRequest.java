package service;

import data.Node;
import database.Storage;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import user.User;
import java.util.HashSet;
import java.util.Objects;

/**
 * This Class manages the ServiceRequest types.
 * @author Mathew McMillan
 * @version "%I%, %G%"
 */
public class ServiceRequest {

    /**
     * Stores a HashSet of Service Requests.
     */
    private static HashSet<ServiceRequest> service_requests = new HashSet<>();

    /**
     * id of the service request
     */
    private long id;

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
     * Instance of the storage class
     */
    private static Storage storage = Storage.getInstance();

    /**
     * Creates a new service request. This method is only used within this class,
     * createServiceRequest() should normally be used.
     * @param title the name of the service request.
     * @param description a brief description.
     * @param type the type of service requested.
     * @param requester the user requesting the service.
     * @param location the location where the service is needed.
     */
    private ServiceRequest(String title, String description, ServiceType type, User requester, Node location) {
        this.title = title;
        this.requestedDate = DateTime.now();
        this.description = description;
        this.service_type = type;
        this.requester = requester;
        this.location = location;
    }

    /**
     * Returns all service requests, even completed ones.
     * @return a HashSet of all service requests, both completed and not.
     */
    public static HashSet<ServiceRequest> getServiceRequests() {
        return service_requests;
    }

    private Node location;

    /**
     * Gets a HashSet of all service requests that need to be fulfilled.
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

    DateTime requestedDate, fulfilledDate;

    public DateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(DateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public DateTime getFulfilledDate() {
        return fulfilledDate;
    }

    public void setFulfilledDate(DateTime fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
    }

    /**
     * Creates a new service request.
     * @param title the title of the request.
     * @param description a brief description.
     * @param type the type of request.
     * @param requester the user requesting the service.
     * @param location the location where the service is needed.
     */
    public static ServiceRequest createService(String title, String description, ServiceType type, User requester, Node location) {
        ServiceRequest sr = new ServiceRequest(title, description, type, requester, location);
        service_requests.add(sr);
        storage.saveRequest(sr);
        ServiceLogEntry.log(sr, false);
        return sr;
    }

    /**
     * Returns the one making the request.
     * @return the person who made the request.
     */
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
            fulfilledDate = DateTime.now();
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
     * Sets the fulfiller
     * @param fulfiller the fulfiller to set
     */
    public void setFulfiller(User fulfiller) { this.fulfiller = fulfiller; }

    /**
     * Retrieves the location of the request.
     * @return the location.
     */
    public Node getLocation() {
        return location;
    }

    /**
     * Sets the location of the request.
     * @param location the location of the request.
     */
    public void setLocation(Node location) {
        this.location = location;
    }

    public long getRequestID() { return this.id; }

    public void setRequestID(long id) { this.id = id; }

    @Override
    public String toString() {
        return getTitle();
    }

    // TODO for debugging
//    @Override
//    public String toString() {
//        return "ServiceRequest{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", service_type=" + service_type +
//                ", requester=" + requester +
//                ", fulfiller=" + fulfiller +
//                ", location=" + location +
//                ", requestedDate=" + requestedDate +
//                ", fulfilledDate=" + fulfilledDate +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequest that = (ServiceRequest) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(service_type, that.service_type) &&
                Objects.equals(requester, that.requester) &&
                Objects.equals(fulfiller, that.fulfiller) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, description, service_type, requester, fulfiller, location, requestedDate, fulfilledDate);
    }
}
