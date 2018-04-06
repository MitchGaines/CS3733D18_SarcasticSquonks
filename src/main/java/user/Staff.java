package user;
import service.ServiceRequest;
import java.util.ArrayList;

/**
 * Staff is an Abstract class for different Staff user types.
 * @author Danny Sullivan
 * @version %I%, %G%
 *
 */
public abstract class Staff extends User {
    protected Staff(String username, String password){
        super(username, password);
    }
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
