package lesson02;

import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
public class FilePackage implements Serializable {
    private String filePath;
    private byte[] data;

    public FilePackage(String filePath) throws IOException {
        this.filePath = filePath;
        this.data = Files.readAllBytes(Paths.get(filePath));
    }



}
