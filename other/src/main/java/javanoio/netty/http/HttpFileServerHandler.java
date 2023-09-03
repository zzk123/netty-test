package javanoio.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

/**
 * @ClassName HttpFileServerHandler
 * @Description TODO
 * @Author zzk
 * @Date 2023/8/16 22:00
 **/
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    //文件名称匹配规则，必须是英文、数字下划线、中划线
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    //文件路徑
    private final String url;

    public HttpFileServerHandler(String url){
        this.url = url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // 如果无法解码,返回400
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //只支持GET方法,其他方法返回
        if (msg.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        //获取文件路径
        final String uri = msg.uri();
        // 格式化URL，并且获取文件的磁盘路径
        final String path = sanitizeUri(uri);
        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        // 如果文件隐藏不可访问或者文件不存在
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        //如果是文件目录
        if (file.isDirectory()) {
            //以/结尾时，就列出文件夹下的所有文件
            if (uri.endsWith("/")) {
                sendLisiting(ctx, file);
            } else {
                //否则进行重定向，打开文件夹，继续深入
                sendRedirect(ctx, uri + '/');
            }
            return;
        }
        //既不是文件夹，也不是文件
        if(!file.isFile()){
            sendError(ctx, FORBIDDEN);
            return;
        }
        //读取文件，显示在html页面上
        RandomAccessFile randomAccessFile = null;
        try{
            // 以只读的方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        }catch (Exception e){
            sendError(ctx, NOT_FOUND);
            return;
        }
        //获取文件长度
        long fileLength = randomAccessFile.length();
        //创建一个默认的HTTP响应
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置Content Length
        HttpUtil.setContentLength(response, fileLength);
        //设置Content Type
        setContentTypeHeader(response, file);
        //如果request中有KEEP ALIVE信息
        if (HttpUtil.isKeepAlive(msg)) {
            //保持长连接
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        //通过Netty的ChunkedFile对象直接将文件写入发送到缓冲区中
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
        //文件监听
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                //不知道文件的大小
                if (total < 0) {
                    System.err.println("文件访问进度: " + progress);
                } else {
                    System.err.println("文件访问进度， " + progress + " / " + total + "，总长度：" + total + ",已读取：" + progress);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("文件展示完成。");
            }
        });
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        //如果不支持keep-Alive，服务器端主动关闭请求
        if (!HttpUtil.isKeepAlive(msg)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 格式化uri，返回文件的磁盘路径
     * @param uri
     * @return
     *
     */
    private String sanitizeUri(String uri){
        try{
            uri = URLDecoder.decode(uri, "UTF-8");
        }catch (Exception e){
            try{
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            }catch (Exception e1){
                throw new Error();
            }
        }
        if(!uri.startsWith(url)){
            return null;
        }
        if(!uri.startsWith("/")){
            return null;
        }
        uri = uri.replace('/', File.separatorChar);
        if(uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator)
                || uri.startsWith(".")
                || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()){
            return null;
        }
        return System.getProperty("user.dir") + File.separator + uri;
    }

    /**
     * 展示文件列表
     * @param ctx
     * @param dir
     *
     */
    private void sendLisiting(ChannelHandlerContext ctx, File dir){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append("使用netty做下载文件服务器");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append("当前文件夹位置：");
        buf.append(dirPath);
        buf.append("</h3>\r\n");
        buf.append("<h4>");
        buf.append("文件列表");
        buf.append("</h4>\r\n");
        buf.append("<ul>");
        buf.append("<li>上一级链接：<a href=\"../\">..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            //隐藏文件，不可读文件直接跳过
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            //非英文、数字、下划线、中划线组成的文件，也跳过
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发送重定向
     * @param ctx
     * @param newUri
     *
     */
    private void sendRedirect(ChannelHandlerContext ctx, String uri){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, FOUND);
        response.headers().set(LOCATION, uri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 显示错误信息
     * @param ctx
     * @param status
     *
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 设置文件头部
     * @param response
     * @param file
     *
     */
    private void setContentTypeHeader(HttpResponse response, File file){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file));
    }

}