package nia.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import static io.netty.channel.DummyChannelPipeline.DUMMY_INSTANCE;

/**
 * @program: netty-test
 * @description: 修改ChannelPipeline
 * @author: zzk
 * @create: 2020-09-28
 */
public class ModifyChannelPipeline {

    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DUMMY_INSTANCE;

    public static void modifyPipeline(){
        ChannelPipeline pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE;
        FirstHandler firstHandler = new FirstHandler();
        pipeline.addLast("handler1", firstHandler);
        pipeline.addFirst("handler2", firstHandler);
        pipeline.addLast("handler3", new ThirdHandler());
        //...
        pipeline.remove("handler3");
        pipeline.remove(firstHandler);
        pipeline.replace("handler2", "handler4", new FourthHandler());
    }

    private static final class FirstHandler extends ChannelHandlerAdapter {}

    private static final class SecondHandler extends ChannelHandlerAdapter{}

    private static final class ThirdHandler extends ChannelHandlerAdapter{}

    private static final class FourthHandler extends ChannelHandlerAdapter{}

}
