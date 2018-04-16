package edu.wpi.cs3733d18.teamS.service;

import edu.wpi.cs3733d18.teamS.data.Node;
import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.user.User;
import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Objects;

/**
 * This Class manages the ServiceRequest types.
 *
 * @author Mathew McMillan
 * @version "%I%, %G%"
 */
public class ServiceRequest {

    /**
     * Stores a HashSet of Service Requests.
     */
    private static HashSet<ServiceRequest> service_requests = new HashSet<>();
    /**
     * Instance of the storage class
     */
    private static Storage storage = Storage.getInstance();
    /**
     * Stores the title and a description.
     */
    String title, description;

    /**
     * Stores the type of edu.wpi.cs3733d18.teamS.service.
     */
    ServiceType service_type;
    DateTime requestedDate, fulfilledDate;
    /**
     * id of the edu.wpi.cs3733d18.teamS.service request
     */
    private long id;
    /**
     * Stores the User who made a request and the one who is doing the task
     */
    private User requester, fulfiller;
    private Node location;

    /**
     * Creates a new edu.wpi.cs3733d18.teamS.service request. This method is only used within this class,
     * createServiceRequest() should normally be used.
     *
     * @param title       the name of the edu.wpi.cs3733d18.teamS.service request.
     * @param description a brief description.
     * @param type        the type of edu.wpi.cs3733d18.teamS.service requested.
     * @param requester   the edu.wpi.cs3733d18.teamS.user requesting the edu.wpi.cs3733d18.teamS.service.
     * @param location    the location where the edu.wpi.cs3733d18.teamS.service is needed.
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
     * Returns all edu.wpi.cs3733d18.teamS.service requests, even completed ones.
     *
     * @return a HashSet of all edu.wpi.cs3733d18.teamS.service requests, both completed and not.
     */
    public static HashSet<ServiceRequest> getServiceRequests() {
        return service_requests;
    }

    /**
     * Gets a HashSet of all edu.wpi.cs3733d18.teamS.service requests that need to be fulfilled.
     *
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
     * Creates a new edu.wpi.cs3733d18.teamS.service request.
     *
     * @param title       the title of the request.
     * @param description a brief description.
     * @param type        the type of request.
     * @param requester   the edu.wpi.cs3733d18.teamS.user requesting the edu.wpi.cs3733d18.teamS.service.
     * @param location    the location where the edu.wpi.cs3733d18.teamS.service is needed.
     */
    public static ServiceRequest createService(String title, String description, ServiceType type, User requester, Node location) {
        ServiceRequest sr = new ServiceRequest(title, description, type, requester, location);
        service_requests.add(sr);
        //storage.saveRequest(sr); // TODO we should not save to database here
        ServiceLogEntry.log(sr, false);
        return sr;
    }

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
     * Returns the one making the request.
     *
     * @return the person who made the request.
     */
    public User getRequester() {
        return requester;
    }

    /**
     * Sets the edu.wpi.cs3733d18.teamS.user requesting the edu.wpi.cs3733d18.teamS.service.
     *
     * @param requester
     */
    public void setRequester(User requester) {
        this.requester = requester;
    }

    /**
     * Marks the specified edu.wpi.cs3733d18.teamS.user as the fulfiller of the edu.wpi.cs3733d18.teamS.service request.
     *
     * @param user the edu.wpi.cs3733d18.teamS.user who fulfills the edu.wpi.cs3733d18.teamS.service request.
     * @return true if the edu.wpi.cs3733d18.teamS.service request was successfully marked as fulfilled, or false if it was fulfilled already.
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
     * Returns whether or not the edu.wpi.cs3733d18.teamS.service request is fulfilled (whether or not a fulfiller exists).
     *
     * @return whether or not the edu.wpi.cs3733d18.teamS.service request is fulfilled.
     */
    public boolean isFulfilled() {
        return fulfiller != null;
    }

    /**
     * Retrieves the title.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title of the request.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the description of the request.
     *
     * @return the description of the request.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the edu.wpi.cs3733d18.teamS.service request.
     *
     * @param description a description of the edu.wpi.cs3733d18.teamS.service request.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the Service Type.
     *
     * @return the edu.wpi.cs3733d18.teamS.service type.
     */
    public ServiceType getServiceType() {
        return service_type;
    }

    /**
     * Sets the edu.wpi.cs3733d18.teamS.service type.
     *
     * @param service_type the type of edu.wpi.cs3733d18.teamS.service.
     */
    public void setService_type(ServiceType service_type) {
        this.service_type = service_type;
    }

    /**
     * Retrieves the edu.wpi.cs3733d18.teamS.user fulfilling the request.
     *
     * @return the fulfiller.
     */
    public User getFulfiller() {
        return fulfiller;
    }

    /**
     * Sets the fulfiller
     *
     * @param fulfiller the fulfiller to set
     */
    public void setFulfiller(User fulfiller) {
        this.fulfiller = fulfiller;
    }

    /**
     * Retrieves the location of the request.
     *
     * @return the location.
     */
    public Node getLocation() {
        return location;
    }

    /**
     * Sets the location of the request.
     *
     * @param location the location of the request.
     */
    public void setLocation(Node location) {
        this.location = location;
    }

    public long getRequestID() {
        return this.id;
    }

    public void setRequestID(long id) {
        this.id = id;
    }

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
