package edu.wpi.cs3733d18.teamS.service;

public class EpicPolling implements Runnable {
    private Thread t;

    @Override
    public void run() {
        //look for tasks on server and add them to db as service requests
    }

    public void start(){
        if(t == null){
            t.start();
        }
    }
}
