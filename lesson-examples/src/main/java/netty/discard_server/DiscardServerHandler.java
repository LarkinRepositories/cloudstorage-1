package netty.discard_server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class DiscardServerHandler extends ChannelInboundHandlerAdapter { //inbound -> входящий поток на сервер от клиента

    //

    /**
     * Метод, обрабатывающий входящие данные от клиента
     * @param ctx контекст канала (все управление каналом)
     * @param msg входящее сообщение
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Recieved and released");
        ((ByteBuf) msg).release(); //обьект msg будет освобожден
    }

    /**
     * Метод, выбрасывающий исключение, если при работе метода channelRead оно возникнет
     * @param ctx контекст канала
     * @param cause причина
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
