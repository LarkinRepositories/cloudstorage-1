package commons.commands;

import lombok.Getter;

import java.nio.file.Path;

public class UploadCommand implements Command {
    @Getter
//    private String filePath = "";
    private String filePath;

    @Override
    public void operate(String[] input) {
        if (input.length != 1) throw new IllegalArgumentException("invalid path to file on upload command");
        filePath = input[0];
    }
}
