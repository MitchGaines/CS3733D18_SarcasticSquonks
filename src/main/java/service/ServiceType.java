package service;

import javafx.collections.ObservableList;
import org.joda.time.*;
import user.LoginHandler;
import user.User;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Defines a ServiceType.
 * @author Mathew McMillan
 * @version "%I%, %G%"
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

    public void setName(String name) { this.name = name; }

    /**
     * Stores a HashSet of users.
     */
    private HashSet<User> fulfillers = new HashSet<>();

    /**
     * Stores a HashSet of ServiceTypes.
     */
    private static HashSet<ServiceType> serviceTypes = new HashSet<>();

    /**
     * Constructs a ServiceType using a string for the name, a boolean to determine if the
     * request is an immediate emergency, and a HashSet of users that can fulfill the request.
     * @param name A string for the name of the ServiceType.
     * @param emergency A boolean to to determine if the ServiceType is an emergency.
     * @param fulfillers A HashSet of users able to fulfill the request.
     */
    private ServiceType(String name, boolean emergency, HashSet<User> fulfillers) {
        this.name = name;
        this.emergency = emergency;
        this.fulfillers = fulfillers;
    }

    /**
     * Retrieves the ServiceTypes created.
     * @return a HashSet of ServiceTypes.
     */
    public static HashSet<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    /**
     * Adds a ServiceType to the HashSet of ServiceTypes in the class and returns a new ServiceType.
     * @param name the name of the ServiceType.
     * @param emergency a boolean to determine whether the ServiceType is an emergency.
     * @param fulfillers a HashSet of users that can fulfill the request.
     * @return returns the new ServiceType.
     */
    public static ServiceType createServiceType(String name, boolean emergency, HashSet<User> fulfillers) {

        if (fulfillers == null) {
            fulfillers = new HashSet<>();
        }
        ServiceType new_service = new ServiceType(name, emergency, fulfillers);
        serviceTypes.add(new_service);
        return new_service;
    }

    @Override
    public String toString() {
        return getName();
    }

    // TODO for debugging
//    @Override
//    public String toString() {
//        return "ServiceType{" +
//                "name='" + name + '\'' +
//                ", emergency=" + emergency +
//                ", fulfillers=" + fulfillers +
//                '}';
//    }
    /**
     * Retrieves the HashSet of Users that can fulfill a ServiceType.
     * @return the HashSet of Users.
     */
    public HashSet<User> getFulfillers() {
        return fulfillers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceType that = (ServiceType) o;
        return emergency == that.emergency &&
                Objects.equals(name, that.name) &&
                Objects.equals(fulfillers, that.fulfillers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, emergency, fulfillers);
    }

    ///////////////////// Fancy Reports ///////////////////////

    private Stream<ServiceRequest> requestedInRange(DateTime start, DateTime end) {
        return ServiceRequest.getServiceRequests().stream()
                .filter(e -> e.service_type == this && e.requestedDate.toDateTime().isBefore(end.toDateTime().toInstant()) && e.requestedDate.toDateTime().isAfter(start.toDateTime().toInstant()));
    }

    public long getNumRequests(DateTime start, DateTime end) {
        return requestedInRange(start, end).count();
    }

    public double getAverageFulfillmentTimeInHours(DateTime start, DateTime end) {
        return requestedInRange(start, end)
                .filter(ServiceRequest::isFulfilled)
                .mapToDouble(e -> ((double)(e.fulfilledDate.getMillis() - e.requestedDate.getMillis())) / DateTimeConstants.MILLIS_PER_HOUR)
                .average()
                .orElse(0);
    }
}
