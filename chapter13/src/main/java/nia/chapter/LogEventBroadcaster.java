package nia.chapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-11-09
 */
public class LogEventBroadcaster {

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file){
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new L)
    }
}
