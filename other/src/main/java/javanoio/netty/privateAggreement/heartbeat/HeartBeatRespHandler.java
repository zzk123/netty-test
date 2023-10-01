package javanoio.netty.privateAggreement.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javanoio.netty.privateAggreement.vo.Header;
import javanoio.netty.privateAggreement.vo.MessageType;
import javanoio.netty.privateAggreement.vo.NettyMessage;

/**
 * @ClassName HeartBeatRespHandler
 * @Description 服务端处理心跳请求
 * @Author zzk
 * @Date 2023/9/17 21:48
 **/
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter  {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if(message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_REQ){
            System.out.println("Receive client heart beat message: -->" + message);
            NettyMessage heartBeat = buildHeatBeat();
            System.out.println("Send heart beat response message to client: -->" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeatBeat(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP);
        message.setHeader(header);
        return message;
    }
}
