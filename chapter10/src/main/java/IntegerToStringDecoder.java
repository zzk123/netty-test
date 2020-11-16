import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @program: netty-test
 * @description: 入栈消息解码Integer成String
 * @author: zzk
 * @create: 2020-10-27
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
