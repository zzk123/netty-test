package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-28
 */
public class DummyChannelHandlerContext extends AbstractChannelHandlerContext {

    public static ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(
            null,
            null,
            null,
            true,
            true
    );

    public DummyChannelHandlerContext(DefaultChannelPipeline pipeline,
                                      EventExecutor executor,
                                      String name, boolean inbound, boolean outbound) {
        super(pipeline, executor, name, inbound, outbound);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
