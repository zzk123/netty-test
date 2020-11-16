package io.netty.channel;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-28
 */
public class DummyChannelPipeline extends DefaultChannelPipeline {

    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);

    protected DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
