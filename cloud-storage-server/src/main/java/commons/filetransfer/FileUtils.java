package commons.filetransfer;

import commons.commands.UploadCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileUtils {


    public static void copy(UploadCommand command) throws IOException {
        if (StringUtils.isEmpty(command.getFilePath())) {
            throw new IllegalArgumentException("filePath required");
        }
        final String fileName = getFileName(command);
        try (FileChannel from = (FileChannel.open(Paths.get(command.getFilePath()), StandardOpenOption.READ));
             FileChannel to = (FileChannel.open(Paths.get("c:\\1\\uploads\\" + fileName), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))) {
            transfer(from, to, 0l, from.size());
        }
    }

    public static String getFileName(UploadCommand command) {
        assert StringUtils.isNotEmpty(command.getFilePath());
        final File file = new File(command.getFilePath());
        if (file.isFile()) {
            return file.getName();
        } else {
            throw new RuntimeException("file is invalid");
        }
    }

    //local copy
    private static void transfer(final FileChannel from, FileChannel to, long position, long size) throws IOException {
        assert !Objects.isNull(from) && Objects.isNull(to);
        while (position < size) {
            position += from.transferTo(position, 32768, to);
        }
    }
}
