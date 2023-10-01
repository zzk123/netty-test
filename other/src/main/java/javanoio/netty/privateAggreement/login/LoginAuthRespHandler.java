package javanoio.netty.privateAggreement.login;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javanoio.netty.privateAggreement.vo.Header;
import javanoio.netty.privateAggreement.vo.MessageType;
import javanoio.netty.privateAggreement.vo.NettyMessage;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginAuthRespHeader
 * @Description TODO
 * @Author zzk
 * @Date 2023/9/17 11:26
 **/
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter  {

    private Map<String, Boolean> nodeCheck = new HashMap<>();

    private String[] whitekList = {"127.0.0.1", "192.168.1.104"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //如果是握手请求消息，处理，其他消息透传
        if(message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            //重复登陆拒绝
            if(nodeCheck.containsKey(nodeIndex)){
                loginResp = buildResponse((byte) -1);
            }else{
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOk = false;
                for(String WIP : whitekList){
                    if(WIP.equals(ip)){
                        isOk = true;
                        break;
                    }
                }
                loginResp = isOk ? buildResponse((byte)0) : buildResponse((byte)-1);
                if(isOk){
                    nodeCheck.put(nodeIndex, true);
                }
                System.out.println("The login response is :" + loginResp + " body [" + loginResp.getBody() + "]");
                ctx.writeAndFlush(loginResp);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常, 将客户端信息从登录注册表去除
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
