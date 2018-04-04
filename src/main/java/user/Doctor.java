package user;
import service.ServiceRequest;
import java.util.ArrayList;

/**
 * A Class for the specific user type Doctor.
 * @author Danny Sullivan
 * @version %I%, %G%
 */

public class Doctor extends User {
    /**
     * Stores an array list of Service Request.
     */
    private ArrayList<ServiceRequest> doc_emergencies;

    /**
     * Constructs a Doctor user by taking in a password and a username.
     * @param username the username for the doctor.
     * @param password the username for the doctor.
     */
    public Doctor(String username, String password){
        super(username, password);
        type = user_type.DOCTOR;
    }

    /**
     * Returns the array list of emergencies for the current Doctor.
     *
     * @return array list of emergencies for current Doctor.
     */
    public ArrayList<ServiceRequest> getDocEmergencies() {
        return doc_emergencies;
    }
}
