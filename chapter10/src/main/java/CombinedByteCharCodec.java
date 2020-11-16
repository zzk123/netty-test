import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @program: netty-test
 * @description: 结合ByteToCharDecoder，CharToByteEncoder 实现 参数化 CombinedByteCharCodec
 * @author: zzk
 * @create: 2020-10-27
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
