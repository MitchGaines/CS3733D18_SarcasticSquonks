package edu.wpi.cs3733d18.teamS.arduino;

import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.cs3733d18.teamS.controller.AdminPageController;
import edu.wpi.cs3733d18.teamS.controller.AdminSpecialOptionsController;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;

public class MotionSensor  {


    public boolean getSwitchStatus() {
        return AdminSpecialOptionsController.includeMotion();
    }

    public void connect(){
        SerialPort[] port_names = SerialPort.getCommPorts();
        try{
            SerialPort chosen_port = SerialPort.getCommPort(port_names[0].getSystemPortName().toString());
            chosen_port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
            chosen_port.openPort();
            SensorPolling poll = new SensorPolling();
            poll.start(chosen_port);
        }catch (NullPointerException npe){
            System.out.println("No Arduino found");
        }



    }

}
