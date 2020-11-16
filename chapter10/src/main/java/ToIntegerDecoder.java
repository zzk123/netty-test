import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @program: netty-test
 * @description: 扩展ByteToMessageDecoder，将字节码转为特定格式
 * @author: zzk
 * @create: 2020-10-27
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= 4){
            out.add(in.readInt());
        }
    }
}
