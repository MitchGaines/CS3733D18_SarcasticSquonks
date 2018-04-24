package edu.wpi.cs3733d18.teamS.arduino;

import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.cs3733d18.teamS.controller.Main;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class SensorPolling implements Runnable{
    private Thread t;
    private SerialPort sp;
    private String line;
    private volatile boolean exit;

    @Override
    public void run(){
        exit = false;
        Scanner scanner = new Scanner(sp.getInputStream());
        System.out.println("Sensor is connected...");
        while(!exit){
            line = scanner.nextLine();
            if(Integer.parseInt(line)== 0 ) {
                try{
                    System.out.println(line);
                }catch(Exception e){}
            }
            else if(Integer.parseInt(line)== 1){
                try{
                    System.out.println(line);
                    this.stop();
                    Platform.runLater(() -> Main.switchScenes("Brigham and Women's", "/HomePage.fxml"));
            }catch(Exception e){}
            }

        }
        scanner.close();
        sp.closePort();
        System.out.println("Sensor has stopped");
    }


    public void start(SerialPort chosen_port){
        sp = chosen_port;
        t = new Thread(this, "motion sensing");
        t.start();

    }
    public void stop(){
        exit = true;
    }

    public static SensorPolling getInstance() {
        return PollingHolder.instance;
    }

    private static class PollingHolder {
        private static final SensorPolling instance = new SensorPolling();
    }

}
