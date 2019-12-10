package netty.decoding_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.decoding_server.handlers.DecodeServerHandler;
import netty.decoding_server.handlers.FourByteDecoder;
import netty.decoding_server.handlers.ThirdHandler;

/**
 * Сервер, который будет работать с нарезанными сообщениями
 */
public class DecodeServer {
    public void run() throws Exception {
        /**
         * Создаем два пула потоков:
         * bossGroup - отвечает за входящие подключения
         * workerGroup - отвечает за обработку входящих подключений
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        /**
         * Настраиваем сервер
         */
        try {
            ServerBootstrap server = new ServerBootstrap()
                    //говорим, что сервер должен работать со созданными нами выше группами потоков
                    .group(bossGroup, workerGroup)
                    //сервер будет работать с каналом NioSeverSocketChannel.class
                    .channel(NioServerSocketChannel.class)
                    //каждое входящее подключение будет иметь следующие обработчики FourByteDecoder, DecodeServerHandler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new FourByteDecoder(),
                                    new DecodeServerHandler(),
                                    new ThirdHandler()
                            );
                        }
                    })
                    //каждое входящее подключение будет иметь следующие опции: ChannelOption.SO_KEEPALIVE - поддерживать соединение живым, да
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //запускаем сервак
            ChannelFuture serverStart = server.bind(8189).sync();
            //отслеживаем событие шатдаунта сервера
            serverStart.channel().closeFuture().sync();
        } finally {
            //завершаем выплонением групп потоков
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        //запускаем сервак, вызовом метода run()
        try {
            new DecodeServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
