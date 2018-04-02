package user;
import service.Emergency;
import java.util.ArrayList;

/**
 * Doctor.java
 * Class for user type Doctor
 * Author: Danny Sullivan
 * Date: March 31, 2018
 */

public class Doctor extends User {
    private ArrayList<Emergency> doc_emergencies;

    /**
     * Returns the array list of emergencies for the current Doctor
     *
     * @return array list of emergencies for current Doctor
     */
    public ArrayList<Emergency> getDocEmergencies() {
        return doc_emergencies;
    }
}
