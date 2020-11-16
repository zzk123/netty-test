package nia.chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: netty-test
 * @description: 处理出站异常
 * @author: zzk
 * @create: 2020-09-28
 */
public class ChannelFutures {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    private static final ByteBuf SOME_MSG_FROM_SOMEWHERE = Unpooled.buffer(1024);

    public static void addingChannelFutureListener(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBuf someMessage = SOME_MSG_FROM_SOMEWHERE;
        //....
        io.netty.channel.ChannelFuture future = channel.write(someMessage);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    future.cause().printStackTrace();
                    future.channel().close();
                }
            }
        });
    }
}
