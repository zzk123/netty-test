package javanoio.netty.privateAggreement.endecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import javanoio.netty.privateAggreement.utils.GsonUtil;
import javanoio.netty.privateAggreement.vo.Header;
import javanoio.netty.privateAggreement.vo.NettyMessage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NettyMessageDecoder
 * @Description Netty消息解码器
 * @Author zzk
 * @Date 2023/9/12 21:37
 **/
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        //        if (null == frame) {
        //            return null;
        //        }
        //读取请求头数据
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = in.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<>();
            int keySize;
            int valueSize;
            byte[] keyArray;
            byte[] valueArray;
            String key;
            String value;
            for (int i = 0; i < size; i++) {
                //读取长度值
                keySize = in.readInt();
                keyArray = new byte[keySize];
                //读取内容
                in.readBytes(keyArray);
                key = new String(keyArray, StandardCharsets.UTF_8);

                valueSize = in.readInt();
                valueArray = new byte[valueSize];
                in.readBytes(valueArray);
                value = new String(valueArray, StandardCharsets.UTF_8);
                int clazzSize = in.readInt();
                byte[] clazzArray = new byte[clazzSize];
                in.readBytes(clazzArray);
                String clazz = new String(clazzArray, StandardCharsets.UTF_8);
                attch.put(key, GsonUtil.parse(value, Class.forName(clazz)));
            }
            header.setAttachment(attch);
        }
        //读取消息
        //4代表一个int，超过表示还有数据
        if (in.readableBytes() > 4) {
            //body的长度
            int bodyLength = in.readInt();
            byte[] bodyArray = new byte[bodyLength];
            //body的内容并转成String
            in.readBytes(bodyArray);
            String bodyJson = new String(bodyArray, StandardCharsets.UTF_8);
            int clazzSize = in.readInt();
            byte[] clazzArray = new byte[clazzSize];
            in.readBytes(clazzArray);
            String clazz = new String(clazzArray, StandardCharsets.UTF_8);
            //根据body的内容json和类型名称，转换成对象
            Object body = GsonUtil.parse(bodyJson, Class.forName(clazz));
            message.setBody(body);
        }
        message.setHeader(header);
        return message;
    }
}
