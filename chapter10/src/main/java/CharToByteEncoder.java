import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-10-27
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Character msg, ByteBuf out) throws Exception {
        out.writeChar(msg);
    }
}
