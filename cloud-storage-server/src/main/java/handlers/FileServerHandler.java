package handlers;

import commons.commands.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@ChannelHandler.Sharable
public class FileServerHandler extends SimpleChannelInboundHandler<Command> {

    private final Map<Class<? extends Command>, CommandHandler> commandHandlerMap = new HashMap<>();
    private final CommandFactory commandFactory = new CommandFactory();

    public FileServerHandler() {
        initialize();
    }


    private void initialize() {
        commandHandlerMap.put(commandFactory.createCommand(CommandTypes.LOGIN).getClass(), new LoginHandler());
        commandHandlerMap.put(commandFactory.createCommand(CommandTypes.LOGOUT).getClass(), new LogoutHandler());
        commandHandlerMap.put(commandFactory.createCommand(CommandTypes.UPLOAD).getClass(), new UploadHander());
        commandHandlerMap.put(commandFactory.createCommand(CommandTypes.DOWNLOAD).getClass(), new DownloadHandler());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(" Welcome to Telnet Cloud Storage, please user /login command to start\r\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        if (commandHandlerMap.containsKey(command.getClass())) {
            commandHandlerMap.get(command.getClass()).handle(channelHandlerContext, command);
        } else {
            channelHandlerContext.writeAndFlush("Error: such command doesn't have a handler implementation");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.writeAndFlush("invalid command");
    }

    private class LoginHandler implements CommandHandler<LoginCommand> {

        @Override
        public void handle(ChannelHandlerContext ctx, LoginCommand command) {

        }
    }


    private class LogoutHandler implements CommandHandler<LogoutCommand> {

        @Override
        public void handle(ChannelHandlerContext ctx, LogoutCommand command) throws Exception {

        }
    }


    private class UploadHander implements CommandHandler<UploadCommand> {

        @Override
        public void handle(ChannelHandlerContext ctx, UploadCommand command) throws Exception {
            copy(command);
        }

        private void copy(UploadCommand command) throws IOException {
            if (StringUtils.isEmpty(command.getFilePath())) {
                throw new IllegalArgumentException("filePath required");
            }
            final String fileName = getFileName(command);
            try (FileChannel from = (FileChannel.open(Paths.get(command.getFilePath()), StandardOpenOption.READ));
                 FileChannel to = (FileChannel.open(Paths.get("c:\\1\\uploads\\" + fileName), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))) {
                transfer(from, to, 0l, from.size());
            }
        }

        private void transfer(final FileChannel from, FileChannel to, long position, long size) throws IOException {
            assert !Objects.isNull(from) && Objects.isNull(to);
            while (position < size) {
                position += from.transferTo(position, 32768, to);
            }
        }

        private String getFileName(UploadCommand command) {
            assert StringUtils.isNotEmpty(command.getFilePath());
            final File file = new File(command.getFilePath());
            if (file.isFile()) {
                return file.getName();
            } else {
                throw new RuntimeException("file is invalid");
            }
        }


//            RandomAccessFile raf = null;
//            long length = -1;
//            try {
//                raf = new RandomAccessFile(command.getFilePath(), "r");
//                length = raf.length();
//            } catch (Exception e) {
//                ctx.writeAndFlush("Error:" +e.getClass() +"\r\n");
//                return;
//            } finally {
//                if (length < 0 && raf != null) {
//                    raf.close();
//                }
//            }
//            ctx.writeAndFlush("OK: " + raf.length() + "\r\n");
//            ctx.writeAndFlush(new ChunkedFile(raf));
//            ctx.writeAndFlush("\r\n");
    }



    private class DownloadHandler implements CommandHandler<DownloadCommand> {

        @Override
        public void handle(ChannelHandlerContext ctx, DownloadCommand command) throws Exception {

        }
    }



    private interface CommandHandler<T extends Command> {
        void handle(ChannelHandlerContext ctx, T command) throws Exception;
    }
}
