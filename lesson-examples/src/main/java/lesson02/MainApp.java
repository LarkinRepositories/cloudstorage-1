package lesson02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.RandomAccess;
import java.util.concurrent.CountDownLatch;

public class MainApp {
    public static void main(String[] args) throws Exception {
        //чтение файлов в java.io
//        try (FileInputStream in = new FileInputStream("2.txt")) {
//            int x;
//            while ((x = in.read()) != -1) {
//                System.out.println((char) x);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //чтение файлов в java.nio
//        RandomAccessFile raf = new RandomAccessFile("2.txt", "rw");
//        FileChannel fileChannel = raf.getChannel();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
//        int bytesRead = fileChannel.read(byteBuffer);
//        while (bytesRead != -1)  {
//            byteBuffer.flip();
//            while (byteBuffer.hasRemaining()) {
//                System.out.println((char) byteBuffer.get());
//            }
//            byteBuffer.clear();
//            bytesRead = fileChannel.read(byteBuffer);
//        }



//        transferFileEx();
        RandomAccessFile src = new RandomAccessFile("2.txt", "rw");
        FileChannel srcChannel = src.getChannel();
        RandomAccessFile dst  = new RandomAccessFile("out_2.txt","rw" );
        FileChannel dstChannel = dst.getChannel();

        long position = 0;
        long count = srcChannel.size();

        dstChannel.transferFrom(srcChannel, position, count);
//        srcChannel.transferTo(position, count, dstChannel);


    }
    //передача файлов сериализацией
    private static void transferFileEx() throws InterruptedException {
        CountDownLatch serverStarted = new CountDownLatch(1);
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8189)) {
                System.out.println("Server started");
                serverStarted.countDown();
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                FilePackage inputPackage = (FilePackage) in.readObject();
                Files.write(
                        Paths.get(inputPackage.getFilePath(), "server_" + inputPackage.getFileName()),
                        inputPackage.getData(),
                        StandardOpenOption.CREATE
                );
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
//            FilePackage filePackage = new FilePackage("lesson-examples/src/main/resources/2.txt");
            FilePackage filePackage = new FilePackage("lesson-examples/src/main/resources", "2.txt");
            out.writeObject(filePackage);
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
