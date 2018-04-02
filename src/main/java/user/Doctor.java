package user;
import service.Emergency;
import java.util.ArrayList;

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
