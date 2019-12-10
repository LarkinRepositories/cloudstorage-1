package netty.decoding_server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Наследуемся от ByteToMessageDecoder
 * ByteToMessageDecoder - модифицированный ChannelInboundHandlerAdapter, его задача получить ByteBuf
 * и преобразовать его в какое-то количество объектов.
 */
public class FourByteDecoder extends ByteToMessageDecoder {
    //переодпределяем метд decode

    /**
     * Метод, преобразующий входящий ByteBuf в список объектов
     * Метод делает длину посылки равной 4 байта
     * @param ctx  контекст канала
     * @param in байт буффер (т.к текущий класс стоит первым в конвеере, он гарантированно получит байт буффер
     * @param out список объектов, переданных методом дальше по конвееру (для меня сразу не очевидно - т.к void, а не List)
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) { //если в буфере недостаточно байт, не делаем ничего
            return;
        }
        byte[] data = new byte[4]; //если посылка 4 байта, кладем ее в массив
        in.readBytes(data); //читаем данные из массива
        String str = new String(data); //создаем из них строку
        out.add(str);  //добаляем строку в список объектов out -> out отправляется дальше по конвееру
    }

    /**
     * Метод, которые сработает, если decode выбросит исключение
     * @param ctx контекст канала (все управление каналом)
     * @param cause причина, по которой возникло исключение
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
