package netty.discard_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    public void run() throws Exception {
        //создается два пула потоков
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //отвечает за подключение новых клиентов к серверу
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //отвечает за обработку
        //запускаем сервер, с определенными настройками. При этом, сначала задаются настройки, а только потом стартует сервер
        try {
            ServerBootstrap b = new ServerBootstrap(); //настройка сервера
            b.group(bossGroup, workerGroup) //для работы сервера понадобятся пулы потоков: bossGroup, workerGroup
                    .channel(NioServerSocketChannel.class) //при работе сервера используй канал NioServerSocketChannel
                    /**
                     * каждый раз, при подключении будет настроен некий "конвеер общения"
                     * в этот конвейер мы будем складывать обработчик DiscardServerHandler
                     *
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            /**
             * запускаем сервер на 8189 порту и говорим, что он должен слушать этот порт и ожидать подключения клиента
             */
            ChannelFuture f = b.bind(8189).sync();
            f.channel().closeFuture().sync(); //если сервер закрвается - передаем управление блоку finally
        } finally {
            workerGroup.shutdownGracefully(); //завершаем рабочую группу (обработчик клиентских соединений)
            bossGroup.shutdownGracefully(); // завершаем боссгруппу (подключение новых клиентов)
        }
    }

    public static void main(String[] args) {
        try {
            new DiscardServer().run(); //запускаем сервер
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
