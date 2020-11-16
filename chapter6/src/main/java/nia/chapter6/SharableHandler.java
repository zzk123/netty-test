package nia.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @program: netty-test
 * @description:
 *  可共享的ChannelHandler
 *  在多个ChannelPipeline中共享同一个ChannelHandler，对应的ChannelHandler必须要使用 @Sharable注解标识
 *  否则，试图将它添加到多个ChannelPipeline时会触发异常
 * @author: zzk
 * @create: 2020-09-28
 */
@ChannelHandler.Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel read message " + msg);
        ctx.fireChannelRead(msg);
    }
}
