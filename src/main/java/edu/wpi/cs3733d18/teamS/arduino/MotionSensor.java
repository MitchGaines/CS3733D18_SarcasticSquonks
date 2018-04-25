package edu.wpi.cs3733d18.teamS.arduino;

import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.cs3733d18.teamS.controller.AdminSpecialOptionsController;

public class MotionSensor {
    public static SensorPolling poll = new SensorPolling();




    public void connect() {

        try {
            SerialPort[] port_names  = SerialPort.getCommPorts();
            SerialPort chosen_port = SerialPort.getCommPort(port_names[0].getSystemPortName().toString());
            if (!chosen_port.isOpen()) {
                chosen_port.openPort();
            }
            chosen_port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
            SensorPolling.getInstance().start(chosen_port);

        } catch (NullPointerException npe) {
            System.out.println("No Arduino found");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No ports found");
        }

    }

    public static MotionSensor getInstance() {
        return MotionHolder.instance;
    }

    private static class MotionHolder {
        private static final MotionSensor instance = new MotionSensor();
    }

}
