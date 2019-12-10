package netty.serialization;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class SerializationClient {
    public static void main(String[] args) {
        ObjectEncoderOutputStream objectEncoderOutputStream = null;
        ObjectDecoderInputStream objectDecoderInputStream = null;

        try (Socket socket = new Socket("localhost", 8189)) {
            objectEncoderOutputStream = new ObjectEncoderOutputStream(socket.getOutputStream());
            MyMessage textMessage = new MyMessage("Hello Server");
            objectEncoderOutputStream.writeObject(textMessage);
            objectEncoderOutputStream.flush();
            objectDecoderInputStream = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);
            MyMessage msgFromServer = (MyMessage) objectDecoderInputStream.readObject();
            System.out.println("SERVER: " +msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                objectEncoderOutputStream.close();
                objectDecoderInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
