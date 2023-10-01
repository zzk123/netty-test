package javanoio.netty.privateAggreement;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import javanoio.netty.privateAggreement.endecoder.NettyMessageDecoder;
import javanoio.netty.privateAggreement.endecoder.NettyMessageEncoder;
import javanoio.netty.privateAggreement.heartbeat.HeartBeatRespHandler;
import javanoio.netty.privateAggreement.login.LoginAuthRespHandler;
import javanoio.netty.privateAggreement.vo.NettyConstant;

/**
 * @ClassName NettyServer
 * @Description 服务端
 * @Author zzk
 * @Date 2023/9/17 15:24
 **/
public class NettyServer {

    public void bind() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4))
                        .addLast(new NettyMessageEncoder())
                        .addLast(new ReadTimeoutHandler(50))
                        .addLast(new LoginAuthRespHandler())
                        .addLast(new HeartBeatRespHandler());
                    }
                });

        ChannelFuture future = b.bind(NettyConstant.REMOTEIP,NettyConstant.REMOTE_PORT).sync();
        System.out.println("Netty server start ok : "+(NettyConstant.REMOTEIP+":"+NettyConstant.REMOTE_PORT));

        future.channel().closeFuture().sync();
    }
    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind();
    }
}
