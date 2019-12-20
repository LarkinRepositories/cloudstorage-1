package handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import sun.nio.cs.StandardCharsets;

/**
 * Инициализатор канала сервера
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final StringDecoder STRING_DECODER = new StringDecoder();
    private final StringEncoder STRING_ENCODER = new StringEncoder();
    private final ChunkedWriteHandler CHUNKED_WRITE_HANDLER = new ChunkedWriteHandler();
    private final CommandDecoder COMMAND_DECODER = new CommandDecoder();
    private final FileServerHandler FILE_SERVER_HANDLER = new FileServerHandler();



    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        socketChannel.pipeline().addLast(STRING_DECODER);
        socketChannel.pipeline().addLast(STRING_ENCODER);
        socketChannel.pipeline().addLast(COMMAND_DECODER);
        socketChannel.pipeline().addLast(CHUNKED_WRITE_HANDLER);
        socketChannel.pipeline().addLast(FILE_SERVER_HANDLER);

    }
}