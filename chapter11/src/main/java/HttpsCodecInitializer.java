import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @program: netty-test
 * @description: 使用HTTPS，只需要添加SslHandler
 * @author: zzk
 * @create: 2020-11-05
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;

    private final boolean isClient;

    public HttpsCodecInitializer(SslContext context, boolean isClient){
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        SSLEngine engine = context.newEngine(channel.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));

        if(isClient){
            pipeline.addLast("codec", new HttpClientCodec());
        }else{
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
