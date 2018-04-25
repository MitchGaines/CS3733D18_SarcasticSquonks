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
    public void run() {
        exit = false;
        Scanner scanner = null;
        try {
            scanner = new Scanner(sp.getInputStream());
            line = scanner.nextLine();
        } catch (NullPointerException e) {
            System.out.println("Input Stream not found");
            line = "0";

        }catch (NumberFormatException num){
            System.out.println("Input Stream format error");
            line = "0";
        }

        System.out.println("Sensor is connected...");
        while(!exit){
            try{
                line = scanner.nextLine();
            }catch (NullPointerException e) {
                System.out.println("Input Stream not found");
                line = "1";

            }catch (NumberFormatException num){
                System.out.println("Input Stream format error");
                line = "1";
            }

            //System.out.println(line);
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
