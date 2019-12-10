package netty.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ProtocolServer {
     public void run() throws Exception {
         EventLoopGroup bossGroup  = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
             ServerBootstrap server = new ServerBootstrap();
             server.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline().addLast(new ProtocolHandler());
                         }
                     })
                     .childOption(ChannelOption.SO_KEEPALIVE, true);
             ChannelFuture serverStart = server.bind(8189).sync();
             serverStart.channel().closeFuture().sync();
         } finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }

    public static void main(String[] args) {
        try {
            new ProtocolServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
