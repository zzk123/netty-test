import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: netty-test
 * @description:接受一个Short类型的实例作为消息，将他编码成Short的原子类型，并将它写入ByteBuf
 * @author: zzk
 * @create: 2020-10-27
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short msg, ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
