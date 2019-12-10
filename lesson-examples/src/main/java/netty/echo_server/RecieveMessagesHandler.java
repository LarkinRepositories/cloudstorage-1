package netty.echo_server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Обработчик входящего сообщения, читающий его и выводящий в консоль
 */
public class RecieveMessagesHandler extends ChannelInboundHandlerAdapter { //наследуемся от ChannelInboundHandlerAdapter - > обрабатываем входящий поток от клиентов
    /**
     * Метод, который сработает при получени входящего сообщения от клиента
     * @param ctx контекст канала (все управление каналом)
     * @param msg входящее сообщение
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * Преобразуем полученное сообщение в ByteBuf.
         * В зависимости от положения обработчика в конвеере, здесь может быть строка или другой тип данных
         */
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { //пока мы можем читать ByteBuf
                System.out.print((char) in.readByte()); //печатаем в консоль счинанные байты, приведенные к типу char
            }
        } finally {
            ReferenceCountUtil.release(msg); //как только сообщение закончилось, мы его освободили
        }
    }

    /**
     * Метод обрабатывает исключения, которые могут возникнуть во время работы channelRead
     * @param ctx контекст канала
     * @param cause причина возникновения исключения
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
