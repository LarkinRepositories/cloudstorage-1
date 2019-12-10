package netty.protocol;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ProtocolClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8189)) {
            DataOutputStream out = new DataOutputStream((socket.getOutputStream()));
            out.writeByte(16);
            byte[] filenameBytes = "java.txt".getBytes();
            out.writeInt(filenameBytes.length);
            out.write(filenameBytes);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
