package user;
import service.ServiceRequest;
import java.util.ArrayList;

public abstract class Staff extends User {
    private ArrayList<ServiceRequest> staff_service_requests;

    /**
     * Returns the array list of requests for current Staff
     *
     * @return array list of requests for the Staff
     */
    public ArrayList<ServiceRequest> getStaffServiceRequests() {
        return staff_service_requests;
    }
}
