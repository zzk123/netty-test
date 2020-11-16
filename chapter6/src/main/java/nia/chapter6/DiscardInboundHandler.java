package nia.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-28
 */
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter {

    /**
     * 消费并释放入站消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg);
    }
}
