import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program: netty-test
 * @description: 自动聚合HTTP的消息片段
 * @author: zzk
 * @create: 2020-11-05
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient){
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        if(isClient){
            pipeline.addLast("codec", new HttpClientCodec());
        }else{
            pipeline.addLast("codec", new HttpServerCodec());
        }
        //最大消息512KB
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(512 * 1024));
    }
}
