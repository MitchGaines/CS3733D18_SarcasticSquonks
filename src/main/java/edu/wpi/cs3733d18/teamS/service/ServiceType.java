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
     * Stores a HashSet of ServiceTypes.
     */
    private static HashSet<ServiceType> serviceTypes = new HashSet<>();
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

        // create new lists of possible fulfillers
        HashSet<User> doctors = new HashSet<>();
        HashSet<User> staff = new HashSet<>();
        HashSet<User> admins = new HashSet<>();
        HashSet<User> cardio = new HashSet<>();
        HashSet<User> plasticspete = new HashSet<>();
        HashSet<User> russians = new HashSet<>();
        HashSet<User> spanish = new HashSet<>();

        // add the appropriate users to each fulfiller set
        doctors.add(storage.getUserByID(1));
        doctors.add(storage.getUserByID(4));
        doctors.add(storage.getUserByID(5));
        admins.add(storage.getUserByID(2));
        staff.add(storage.getUserByID(3));
        cardio.add(storage.getUserByID(4));
        plasticspete.add(storage.getUserByID(5));
        spanish.add(storage.getUserByID(6));
        russians.add(storage.getUserByID(7));

        createServiceType("Medical", true, doctors);
        createServiceType("Custodial", false, staff);
        createServiceType("Administrative", false, admins);
        createServiceType("Cardiology", true, cardio);
        createServiceType("Plastics", false, plasticspete);
        createServiceType("Spanish", true, spanish);
        createServiceType("Russian", true, russians);
    }

    /**
     * Retrieves the ServiceTypes created.
     *
     * @return a HashSet of ServiceTypes.
     */
    public static HashSet<ServiceType> getServiceTypes() {
        return serviceTypes;
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
        serviceTypes.add(new_service);
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
                .mapToDouble(e -> ((double) (e.fulfilledDate.getMillis() - e.requestedDate.getMillis())) / DateTimeConstants.MILLIS_PER_HOUR)
                .average()
                .orElse(0);
    }
}
