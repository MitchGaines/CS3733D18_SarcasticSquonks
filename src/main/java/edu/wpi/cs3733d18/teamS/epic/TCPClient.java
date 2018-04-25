package edu.wpi.cs3733d18.teamS.epic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {

    public static void sendTcpPacket(byte[] data){
        Socket socket = new Socket();
        try {
            socket = new Socket("127.0.0.1", 5000);

            OutputStream out = socket.getOutputStream();
            DataOutputStream os = new DataOutputStream(out);

            os.writeInt(data.length);
            if (data.length > 0) {
                os.write(data, 0, data.length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
