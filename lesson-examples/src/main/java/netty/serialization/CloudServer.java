package netty.serialization;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.serialization.handlers.CloudServerHandler;

public class CloudServer {
    public void run() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                      @Override
                                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                                          socketChannel.pipeline().addLast(
                                                  //декодер сериализует объект - указывается максимальный размер, в данном случае - 100 МБ
                                                  new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                                  new ObjectEncoder(),
                                                  new CloudServerHandler()
                                          );
                                      }
                                  })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture serverStart = server.bind(8189).sync();
            serverStart.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new CloudServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
