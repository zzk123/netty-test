import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @program: netty-test
 * @description: 扩展ReplayingDecoder，将字节码转为特定格式
 * @author: zzk
 * @create: 2020-10-27
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
