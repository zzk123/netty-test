package javanoio.netty.udpdemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName ChineseProverbServerHandler
 * @Description TODO
 * @Author zzk
 * @Date 2023/9/3 16:40
 **/
public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] DICTIONARY = {"test1", "test2", "test3", "test4"};

    private String nextQuote(){
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

        if("字典查询".equals(req)){
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("查询结果:" + nextQuote(), CharsetUtil.UTF_8), msg.sender() ));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
