package javanoio.netty.privateAggreement.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javanoio.netty.privateAggreement.vo.Header;
import javanoio.netty.privateAggreement.vo.MessageType;
import javanoio.netty.privateAggreement.vo.NettyMessage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HeartBeatReqHandler
 * @Description 心跳检测机制
 * @Author zzk
 * @Date 2023/9/17 11:51
 **/
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //握手成功，主动发送心跳信息
        if(message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP){
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 50000,
                    TimeUnit.MILLISECONDS);
        }else if(message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_RESP){
            System.out.println("Client receive server heart beat message: --> " + message);
        }else{
            ctx.fireChannelRead(message);
        }
    }


    private class HeartBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            System.out.println("Client send heart beat message to server: --> " + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildHeatBeat(){
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ);
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
