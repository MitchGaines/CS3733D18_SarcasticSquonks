package edu.wpi.cs3733d18.teamS.service;

import edu.wpi.cs3733d18.teamS.database.Storage;
import edu.wpi.cs3733d18.teamS.user.LoginHandler;
import edu.wpi.cs3733d18.teamS.user.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        storage.saveFulfiller(storage.getServiceTypesByName("Medical"), storage.getUserByID(1));
        storage.saveFulfiller(storage.getServiceTypesByName("Medical"), storage.getUserByID(4));
        storage.saveFulfiller(storage.getServiceTypesByName("Medical"), storage.getUserByID(5));
        storage.saveFulfiller(storage.getServiceTypesByName("Custodial"), storage.getUserByID(3));
        storage.saveFulfiller(storage.getServiceTypesByName("Administrative"), storage.getUserByID(2));
        storage.saveFulfiller(storage.getServiceTypesByName("Cardiology"), storage.getUserByID(4));
        storage.saveFulfiller(storage.getServiceTypesByName("Plastics"), storage.getUserByID(5));
        storage.saveFulfiller(storage.getServiceTypesByName("Spanish"), storage.getUserByID(6));
        storage.saveFulfiller(storage.getServiceTypesByName("Russian"), storage.getUserByID(7));

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
        return storage.getAllServiceRequests().stream()
                .filter(e -> e.service_type.getName().equals(this.getName()) && e.requestedDate.toDateTime().isBefore(end.toDateTime().toInstant()) && e.requestedDate.toDateTime().isAfter(start.toDateTime().toInstant()));
    }



    public double getFulfilledInRange(DateTime start, DateTime end) {
        return storage.getAllServiceRequests().stream()
                .filter(e -> e.isFulfilled() && e.service_type.getName().equals(this.getName()) && e.fulfilledDate.toDateTime().isBefore(end.toDateTime().toInstant()) && e.fulfilledDate.toDateTime().isAfter(start.toDateTime().toInstant())).count();
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

    // returns a hashmap with a user and the number of services they requested in the time period
    public HashMap<User, Number> getFulfillmentBreakdownByUser(DateTime start, DateTime end) {
        System.out.println("DING");
        HashMap<User, Number> return_hashmap = new HashMap<>();
        for (User user : Storage.getInstance().getAllUsers()) {
            Stream<ServiceRequest> in_range = requestedInRange(start, end);
            in_range.filter(e -> e.isFulfilled()).forEach(e -> {
                if (e.getFulfiller().getUserID() == user.getUserID()) {
                    if (return_hashmap.containsKey(user)) {
                        return_hashmap.put(user, return_hashmap.get(user).intValue() + 1);
                    } else {
                        return_hashmap.put(user, 1);
                    }
                }
            });
        }
        return return_hashmap;
    }

    // returns a hashmap with a user and the number of services they fulfilled in the time period
    public HashMap<User, Number> getRequestBreakdownByUser(DateTime start, DateTime end) {
        System.out.println("DONG");
        HashMap<User, Number> return_hashmap = new HashMap<>();
        for (User user : Storage.getInstance().getAllUsers()) {
            Stream<ServiceRequest> in_range = requestedInRange(start, end);
            in_range.forEach(e -> {
                if (e.getRequester().getUserID() == user.getUserID()) {
                    if (return_hashmap.containsKey(user)) {
                        return_hashmap.put(user, return_hashmap.get(user).intValue() + 1);
                    } else {
                        return_hashmap.put(user, 1);
                    }
                }
            });
        }
        return return_hashmap;
    }


    // returns a hashmap with a user and the number of services they fulfilled in the time period
    public HashMap<User, Number> getTimeSpentByUser(DateTime start, DateTime end) {
        HashMap<User, Long> milli_hashmap = new HashMap<>();
        for (User user : Storage.getInstance().getAllUsers()) {
            Stream<ServiceRequest> in_range = requestedInRange(start, end);
            in_range.forEach(e -> {
                if (e.getRequester().getUserID() == user.getUserID()) {
                    if (milli_hashmap.containsKey(user)) {
                        milli_hashmap.put(user, milli_hashmap.get(user) + 1);
                    } else {
                        milli_hashmap.put(user, (e.getFulfilledDate().getMillis() - e.getRequestedDate().getMillis()));
                        //System.out.println(milli_hashmap.get(user));
                    }
                }
            });
        }
        HashMap<User, Number> return_hashmap = new HashMap<>();
        for (Map.Entry<User, Long> entry : milli_hashmap.entrySet()) {
            return_hashmap.put(entry.getKey(), ((double) entry.getValue() / DateTimeConstants.MILLIS_PER_HOUR));
        }
        return return_hashmap;
    }

    public int totalInRange(DateTime start, DateTime end) {
        return (int) requestedInRange(start, end).count();
    }
}
