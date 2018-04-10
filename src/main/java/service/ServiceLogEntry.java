package service;

import user.User;

import java.util.ArrayList;
import java.util.Date;

public class ServiceLogEntry {
    private ServiceRequest service_request;
    private boolean completed;
    Date time = new Date();

    private ServiceLogEntry(ServiceRequest service_request, boolean completed) {
        this.service_request = service_request;
        this.completed = completed;
    }

    public ServiceRequest getService_request() {
        return service_request;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Date getTime() {
        return time;
    }

    private static ArrayList<ServiceLogEntry> log = new ArrayList<>();

    public static void log(ServiceRequest service_request, boolean completed) {
        log.add(new ServiceLogEntry(service_request, completed));
    }

    public static ArrayList<ServiceLogEntry> getOverallLog() {
        return log;
    }

    public static ArrayList<ServiceLogEntry> getLogForUser(User user) {
        ArrayList<ServiceLogEntry> to_return = new ArrayList<>();
        for (ServiceLogEntry e : log) {
            if (userInvolved(user, e.getService_request())) {
                to_return.add(e);
            }
        }
        return to_return;
    }

    private static boolean userInvolved(User user, ServiceRequest service_request) {
        return (service_request.getServiceType().getFulfillers().contains(user)) ||
                (service_request.getRequester() != null && service_request.getRequester() == user);
    }

}
