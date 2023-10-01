package javanoio.netty.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @ClassName FileServerHandler
 * @Description TODO
 * @Author zzk
 * @Date 2023/9/3 17:28
 **/
public class FileServerHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        File file = new File(msg);
        if(file.exists()) {
            if (!file.isFile()) {
                ctx.writeAndFlush("not a file:" + file);
                return;
            }
            ctx.write(file + " " + file.length());
            RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
            FileRegion region = new DefaultFileRegion(
                    randomAccessFile.getChannel(), 0, randomAccessFile.length()
            );
            ctx.writeAndFlush(region);
            randomAccessFile.close();
        }else {
            ctx.writeAndFlush("File not found: " + file);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
