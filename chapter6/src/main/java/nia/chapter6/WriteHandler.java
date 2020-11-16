package nia.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: netty-test
 * @description: 缓存到ChannelHandlerContext的引用
 * @author: zzk
 * @create: 2020-09-28
 */
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    /**
     * 使用之前存储到的ChannelHandlerContext的引用来发送消息
     * @param msg
     */
    public void send(String msg){
        ctx.writeAndFlush(msg);
    }
}
