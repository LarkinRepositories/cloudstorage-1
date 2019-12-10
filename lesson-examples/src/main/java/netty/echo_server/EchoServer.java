package netty.echo_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Простой echo-сервер
 */

public class EchoServer {
    public void run() throws Exception {
        /**
         * Создаем два пула потоков:
         * bossGroup - отвечает за подключение новых клиентов  к серверу
         * workerGroup - отвечает за обработку подключений
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        /**
         * Запускаем сервер с определенными настройками
         * Сначала задаются настройки, и только потом стартует сервер
         */
        try {
            //настройки сервера
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //задаем севреру пул потоков
            serverBootstrap.group(bossGroup, workerGroup)
                    //сервер будет работать с каналом NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //добавляем соответствующий обработчик для работы с каждым входящим подключением, в данном случае RecieveMessagesHandelr
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RecieveMessagesHandler());
                        }
                    })
                    //добавляем опцию работы с дочерними каналами (входящими подключениями) - поддерживать подключение активным - да
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //запускаем сервер по адресу localhost и порту 8189. который будет слушать этот порт и ожидать входящих подключений
            ChannelFuture channelFuture = serverBootstrap.bind(8189).sync();
            //отслеживаем событие шатдауна сервера
            channelFuture.channel().closeFuture().sync();
        } finally {
            //завершаем работу потоков
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        //стартуем сервак
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
