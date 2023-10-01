package javanoio.netty.privateAggreement.login;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javanoio.netty.privateAggreement.vo.Header;
import javanoio.netty.privateAggreement.vo.MessageType;
import javanoio.netty.privateAggreement.vo.NettyMessage;

/**
 * @ClassName LoginAuthReqHandler
 * @Description 握手请求处理
 * @Author zzk
 * @Date 2023/9/12 22:05
 **/
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter  {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage nettyMessage = buildLoginReq();
        ctx.writeAndFlush(nettyMessage);
        System.out.println("客户端首次登陆连接, Client send heart beat message: " + nettyMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if(message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP){
            byte loginResult = (byte) message.getBody();
            //握手失败，关闭连接
            if(loginResult != (byte) 0){
                ctx.close();
            }else{
                System.out.println("Login is ok:" + message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private NettyMessage buildLoginReq(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ);
        message.setHeader(header);
        return message;
    }
}
