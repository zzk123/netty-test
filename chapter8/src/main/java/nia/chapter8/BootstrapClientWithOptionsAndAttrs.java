package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * @program: netty-test
 * @description: 使用属性
 * @author: zzk
 * @create: 2020-10-10
 */
public class BootstrapClientWithOptionsAndAttrs {

    public void bootstrap(){
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(
                        new SimpleChannelInboundHandler<ByteBuf>() {

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                Integer idValue = ctx.channel().attr(id).get();
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                System.out.println("Received data");
                            }
                        }
                );
        //设置ChannelOption。其将在connect()或者bind()方法被调用时被设置到已经创建的Channel上
        bootstrap.option(ChannelOption.ALLOCATOR.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80)
        );
        future.syncUninterruptibly();
    }
}
