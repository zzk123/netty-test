package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-22
 */
public class ConnectExample {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    public static void connect(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //异步连接到远程节点
        final ChannelFuture future = channel.connect( new InetSocketAddress("192.168.0.1",25));
        //注册监听以便在操作完成时获取通知
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(future.isSuccess()){
                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
                }else{
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}
