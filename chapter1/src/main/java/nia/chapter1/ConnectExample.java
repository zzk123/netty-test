package nia.chapter1;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ConnectHandler());
        //异步连接到远程节点
        final ChannelFuture future = b.connect( new InetSocketAddress("127.0.0.1",25));
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

    public static void main(String[] args) throws Exception {
        ConnectExample.connect();
    }
}
