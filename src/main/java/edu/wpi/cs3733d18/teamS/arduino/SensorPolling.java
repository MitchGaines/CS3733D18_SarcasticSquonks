package edu.wpi.cs3733d18.teamS.arduino;

import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.cs3733d18.teamS.controller.Main;
import javafx.application.Platform;

import java.util.Scanner;

public class SensorPolling implements Runnable{
    private Thread t;
    private SerialPort sp;
    private String line;

    @Override
    public void run(){
        Scanner scanner = new Scanner(sp.getInputStream());
        while (scanner.hasNextLine()){
            try{
                line = scanner.nextLine();
                System.out.println(line);
                if(Integer.parseInt(line)== 1){
                    sp.closePort();
                    Platform.runLater(() -> Main.switchScenes("Brigham and Women's", "/HomePage.fxml"));
                }


            }catch(Exception e){}
        }
        scanner.close();
    }


    public void start(SerialPort chosen_port){
        sp = chosen_port;
        if(t == null){
            t = new Thread(this, "motion sensing");
            t.start();
        }

    }
}
