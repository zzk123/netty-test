package javanoio.netty.privateAggreement;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import javanoio.netty.privateAggreement.endecoder.NettyMessageDecoder;
import javanoio.netty.privateAggreement.endecoder.NettyMessageEncoder;
import javanoio.netty.privateAggreement.heartbeat.HeartBeatReqHandler;
import javanoio.netty.privateAggreement.login.LoginAuthReqHandler;
import javanoio.netty.privateAggreement.vo.NettyConstant;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyClient
 * @Description 客户端
 * @Author zzk
 * @Date 2023/9/17 12:30
 **/
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    public void connect(int port,String host) throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4))
                            .addLast(new NettyMessageEncoder())
                            .addLast(new ReadTimeoutHandler(50))
                            .addLast(new LoginAuthReqHandler())
                            .addLast(new HeartBeatReqHandler());

                        }

                    });
            // 发起异步连接操作
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
            System.out.println("Netty client start ok . remoteAddress( "+host+":"+port+"),localAddress("+NettyConstant.LOCALIP+":"+NettyConstant.LOCAL_PORT+")");

            future.channel().closeFuture().sync();

        } finally {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            // 发起重连操作
                            connect(NettyConstant.REMOTE_PORT, NettyConstant.REMOTEIP);
                        } catch (Exception e) {
                            System.out.println("NettyClient 发起重连操作异常");
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(NettyConstant.REMOTE_PORT, NettyConstant.REMOTEIP);
    }
}
