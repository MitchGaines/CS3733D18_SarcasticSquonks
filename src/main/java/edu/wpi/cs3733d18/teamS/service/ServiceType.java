package edu.wpi.cs3733d18.teamS.service;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.user.LoginHandler;
import edu.wpi.cs3733d18.teamS.user.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Defines a ServiceType.
 *
 * @author Mathew McMillan
 * @version "%I%, %G%"
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
     * Stores a HashSet of users.
     */
    private HashSet<User> fulfillers = new HashSet<>();

    /**
     * Stores instance of Storage class
     */
    private static Storage storage = Storage.getInstance();

    /**
     * Constructs a ServiceType using a string for the name, a boolean to determine if the
     * request is an immediate emergency, and a HashSet of users that can fulfill the request.
     *
     * @param name       A string for the name of the ServiceType.
     * @param emergency  A boolean to to determine if the ServiceType is an emergency.
     * @param fulfillers A HashSet of users able to fulfill the request.
     */
    private ServiceType(String name, boolean emergency, HashSet<User> fulfillers) {
        this.name = name;
        this.emergency = emergency;
        this.fulfillers = fulfillers;
    }

    /**
     * Creating initial service types, adding appropriate staff as the fulfillers
     */
    public static void createInitialServiceTypes() {
        Storage storage = Storage.getInstance();

        // create service types and save them to the database
        ServiceType medical = createServiceType("Medical", true, null);
        storage.saveServiceType(medical);
        ServiceType custodial = createServiceType("Custodial", false, null);
        storage.saveServiceType(custodial);
        ServiceType administrative = createServiceType("Administrative", false, null);
        storage.saveServiceType(administrative);
        ServiceType cardiology = createServiceType("Cardiology", true, null);
        storage.saveServiceType(cardiology);
        ServiceType plastics = createServiceType("Plastics", false, null);
        storage.saveServiceType(plastics);
        ServiceType span = createServiceType("Spanish", true, null);
        storage.saveServiceType(span);
        ServiceType russian = createServiceType("Russian", true, null);
        storage.saveServiceType(russian);

        // populate fulfiller table with potential fulfillers for each service type
        storage.saveFulfiller(storage.getServiceTypeByName("Medical"), storage.getUserByID(1));
        storage.saveFulfiller(storage.getServiceTypeByName("Medical"), storage.getUserByID(4));
        storage.saveFulfiller(storage.getServiceTypeByName("Medical"), storage.getUserByID(5));
        storage.saveFulfiller(storage.getServiceTypeByName("Custodial"), storage.getUserByID(3));
        storage.saveFulfiller(storage.getServiceTypeByName("Administrative"), storage.getUserByID(2));
        storage.saveFulfiller(storage.getServiceTypeByName("Cardiology"), storage.getUserByID(4));
        storage.saveFulfiller(storage.getServiceTypeByName("Plastics"), storage.getUserByID(5));
        storage.saveFulfiller(storage.getServiceTypeByName("Spanish"), storage.getUserByID(6));
        storage.saveFulfiller(storage.getServiceTypeByName("Russian"), storage.getUserByID(7));

        // save sets of fulfillers to each service type
        HashSet<User> doctors = storage.getAllFulfillersByType(medical);
        medical.setFulfillers(doctors);
        HashSet<User> staff = storage.getAllFulfillersByType(custodial);
        custodial.setFulfillers(staff);
        HashSet<User> admins = storage.getAllFulfillersByType(administrative);
        administrative.setFulfillers(admins);
        HashSet<User> cardio = storage.getAllFulfillersByType(cardiology);
        administrative.setFulfillers(cardio);
        HashSet<User> plastic = storage.getAllFulfillersByType(plastics);
        plastics.setFulfillers(plastic);
        HashSet<User> spanish = storage.getAllFulfillersByType(span);
        span.setFulfillers(spanish);
        HashSet<User> russ = storage.getAllFulfillersByType(russian);
        russian.setFulfillers(russ);
    }

    /**
     * Adds a ServiceType to the HashSet of ServiceTypes in the class and returns a new ServiceType.
     *
     * @param name       the name of the ServiceType.
     * @param emergency  a boolean to determine whether the ServiceType is an emergency.
     * @param fulfillers a HashSet of users that can fulfill the request.
     * @return returns the new ServiceType.
     */
    public static ServiceType createServiceType(String name, boolean emergency, HashSet<User> fulfillers) {

        if (fulfillers == null) {
            fulfillers = new HashSet<>();
        }
        ServiceType new_service = new ServiceType(name, emergency, fulfillers);
        return new_service;
    }

    /**
     * Retrieves the value of the emergency field for the object.
     *
     * @return the emergency field for the object.
     */
    public boolean isEmergency() {
        return emergency;
    }

    /**
     * Retrieves the name of the Service Type.
     *
     * @return the name of the Service Type.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
     *
     * @return the HashSet of Users.
     */
    public HashSet<User> getFulfillers() {
        return fulfillers;
    }

    /**
     * Sets fulfillers of a service type
     *
     * @param fulfillers a HashSet of users associated with a service type
     */
    public void setFulfillers(HashSet<User> fulfillers) {
        this.fulfillers = fulfillers;
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
                .filter(e -> e.service_type.getName().equals(this.getName()) && e.requestedDate.toDateTime().isBefore(end.toDateTime().toInstant()) && e.requestedDate.toDateTime().isAfter(start.toDateTime().toInstant()));
    }

    public long getNumRequests(DateTime start, DateTime end) {
        return requestedInRange(start, end).count();
    }

    public double getAverageFulfillmentTimeInHours(DateTime start, DateTime end) {
        return requestedInRange(start, end)
                .filter(ServiceRequest::isFulfilled)
                .mapToDouble(e -> ((double) (e.fulfilledDate.getMillis() - e.requestedDate.getMillis())) / DateTimeConstants.MILLIS_PER_HOUR)
                .average()
                .orElse(0);
    }
}
