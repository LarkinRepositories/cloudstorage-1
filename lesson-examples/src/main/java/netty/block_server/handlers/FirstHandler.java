package netty.block_server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

/**
 * Задача этого хендлера передать следующему обработчику набор байт = 3, содержащимся в сообщении Object msg
 */
public class FirstHandler extends ChannelInboundHandlerAdapter {
    private  byte[] data = new byte[3];
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Поскольку этот хендлер стоит первым в конвеере, то он с 100% вероятностью получит ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        //ждем получения трех байт
        if (buf.readableBytes() < 3) {
            return;
        }
        //как только получили три байта, готовим массив, чтобы их туда закинуть
        // перекидываем данные из буфера в массив
        buf.readBytes(data);
        // освобождаем буфер
        buf.release();
        // распечатываем что за массив у нас получился
        System.out.println(Arrays.toString(data));
        // перекидываем массив дальше по конвееру
        ctx.fireChannelRead(data);
    }

    // стаднартный обработчик исключений
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
