package handlers;

import commons.commands.Command;
import commons.commands.CommandFactory;
import commons.commands.CommandTypes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChannelHandler.Sharable
public class CommandDecoder extends MessageToMessageDecoder<String> {

    private final Map<String, Command> commandsMap = new HashMap<>();

    CommandDecoder() {
        CommandFactory commandFactory = new CommandFactory();
        commandsMap.put("/login", commandFactory.createCommand(CommandTypes.LOGIN));
        commandsMap.put("/logout", commandFactory.createCommand(CommandTypes.LOGOUT));
        commandsMap.put("/upload", commandFactory.createCommand(CommandTypes.UPLOAD));
        commandsMap.put("/download", commandFactory.createCommand(CommandTypes.DOWNLOAD));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        Command command;
        if (s.startsWith("/")) {
            String[] parts = s.trim().split("\\s");
            String prefix = parts[0];
            String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
            if (commandsMap.containsKey(prefix)) {
                command = commandsMap.get(prefix);
                command.operate(args);
                list.add(command);
            } else  throw new IllegalArgumentException("invalid command");
        }
    }


}
