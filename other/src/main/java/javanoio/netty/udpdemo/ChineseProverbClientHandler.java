package javanoio.netty.udpdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @ClassName ChineseProverbClientHandler
 * @Description TODO
 * @Author zzk
 * @Date 2023/9/3 17:05
 **/
public class ChineseProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String respose = msg.content().toString(CharsetUtil.UTF_8);
        if(respose.startsWith("查询结果:")){
            System.out.println("查询结果:");
            System.out.println(respose);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
