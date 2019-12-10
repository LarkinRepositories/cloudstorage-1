package netty.decoding_server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Наследуемся от ChannelInboundHandlerAdapter
 * ChannelInboundHandlerAdapter - обработчик входящих сооединений
 */
public class DecodeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Метод считывающий входящий поток, переданный обработчиком FourByteDecoder
     * @param ctx контекст канала
     * @param msg входящее сообщение
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //спокойно кастим к строке, т.к знаем, что предыдущий обработчик преобразовал данные в строку
        String str = (String) msg;
        System.out.println("GOT IT!");
//        System.out.println(str);
        ctx.fireChannelRead(str); //передача str следующему обработчику
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
