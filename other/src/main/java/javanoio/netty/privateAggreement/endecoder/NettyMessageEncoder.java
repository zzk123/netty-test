package javanoio.netty.privateAggreement.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import javanoio.netty.privateAggreement.utils.GsonUtil;
import javanoio.netty.privateAggreement.vo.NettyMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NettyMessageEncoder
 * @Description Netty消息编码类
 * @Author zzk
 * @Date 2023/9/12 21:10
 **/
public final class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (null == msg || msg.getHeader() == null) {
            throw new Exception("encode message is null!");
        }

        ByteBuf sendBuf = Unpooled.buffer();
        //请求头写入
        sendBuf.writeInt(msg.getHeader().getCrcCode())
                .writeInt(msg.getHeader().getLength())
                .writeLong(msg.getHeader().getSessionID())
                .writeByte(msg.getHeader().getType())
                .writeByte(msg.getHeader().getPriority())
                .writeInt(msg.getHeader().getAttachment().size());

        byte[] keyArray;
        byte[] valueArray;
        for (Map.Entry<String, Object> entry : msg.getHeader().getAttachment().entrySet()) {
            //写入key，先写大小，再写值，值用Gson转成String，同时把值的类型也写入
            keyArray = entry.getKey().getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            //写值的数据，先写大小，再写内容
            valueArray = GsonUtil.toJson(entry.getValue()).getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(valueArray.length);
            sendBuf.writeBytes(valueArray);
            //写值的类型名称
            byte[] clazzArray = entry.getValue().getClass().getName().getBytes();
            sendBuf.writeInt(clazzArray.length);
            sendBuf.writeBytes(clazzArray);
        }
        //消息写入
        if (msg.getBody() != null) {
            byte[] bodyArray = GsonUtil.toJson(msg.getBody()).getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(bodyArray.length);
            sendBuf.writeBytes(bodyArray);
            byte[] clazzArray = msg.getBody().getClass().getName().getBytes();
            sendBuf.writeInt(clazzArray.length);
            sendBuf.writeBytes(clazzArray);
        }
        //sendBuf.writeInt(0);
        sendBuf.setInt(4, sendBuf.readableBytes());
        out.add(sendBuf);
    }
}
