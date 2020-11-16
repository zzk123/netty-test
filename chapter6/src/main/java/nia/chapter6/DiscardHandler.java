package nia.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @program: netty-test
 * @description: 当从 Channel 读取数据时被调用
 * @author: zzk
 * @create: 2020-09-28
 */
public class DiscardHandler extends ChannelInboundHandlerAdapter {

    /**
     *  当从 Channel 读取数据时被调用, 显式地释放与池化的 ByteBuf 实例相关的内存
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg);
    }
}
