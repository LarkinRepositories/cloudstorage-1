package netty.dynamic_pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

public class DynamicServer implements Runnable {
    private static class AuthHandler extends ChannelInboundHandlerAdapter {
        private boolean authOk = false;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String input = (String) msg;
            if (authOk) {
                ctx.fireChannelRead(input);
                return;
            }
            if (input.split(" ")[0].equalsIgnoreCase("/auth")) {
                String username = input.split(" ")[1];
                authOk = true;
                ctx.pipeline().addLast(new MainHandler(username));
                ctx.pipeline().remove(this); //удаляем из конвеера авторизатор
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    @AllArgsConstructor
    private static class MainHandler extends ChannelInboundHandlerAdapter {
        private String username = null;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String input = (String) msg;
            System.out.println("username: " + input);

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new AuthHandler()
                            );
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture serverStart = server.bind(8189).sync();
            serverStart.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Thread(new DynamicServer()).start();
    }
}
