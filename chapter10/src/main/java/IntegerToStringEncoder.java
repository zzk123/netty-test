import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @program: netty-test
 * @description:将出站消息Integer转换成String
 * @author: zzk
 * @create: 2020-10-27
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
