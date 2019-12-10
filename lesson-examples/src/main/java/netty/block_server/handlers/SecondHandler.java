package netty.block_server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class SecondHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //точно знаем, что предыдущий обработчик отдаем нам массив байт
        byte[] arr = (byte[]) msg; //спокойно кастуем к массиву байт
        //каждый элемент массива увеличиваем на 1
        for (int i = 0; i < 3; i++) {
            arr[i]++;
        }
        System.out.println("Второй шаг: "+ Arrays.toString(arr));
        //кидаем массив дальше по конвееру
        ctx.fireChannelRead(arr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
}
