package netty.serialization.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import netty.serialization.MyMessage;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("Connection sucessful");
        System.out.println("Client connected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) { return; }
            System.out.println(msg.getClass());
            if (msg instanceof MyMessage) {
                System.out.println("Client text message: " + ((MyMessage) msg).getText());
                ctx.writeAndFlush(new MyMessage("Hello Client!"));
            } else {
                System.out.printf("Server recieved wrond object!");
                return;
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
