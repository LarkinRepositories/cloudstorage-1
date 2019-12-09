package lesson02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

public class MainApp {
    public static void main(String[] args) throws Exception {
        CountDownLatch serverStarted = new CountDownLatch(1);
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8189)) {
                System.out.println("Server started");
                serverStarted.countDown();
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                FilePackage inputPackage = (FilePackage) in.readObject();
                Files.write(Paths.get("server_" + inputPackage.getFilePath()), inputPackage.getData(), StandardOpenOption.CREATE);
                in.close();
                socket.close();
                System.out.println("File accepted, shutting down");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        serverStarted.await();
        try {
            Socket socket = new Socket("localhost", 8189);
            ObjectOutputStream out;
            out = new ObjectOutputStream(socket.getOutputStream());
            FilePackage filePackage = new FilePackage("/2.txt");
            out.writeObject(filePackage);
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
