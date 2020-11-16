import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-10-27
 */
public class WebSocketConvertHandler
        extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {


    /**
     * 讲MyWebSocketFrame编码成指定的WebSocketFrame子类型
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();
        switch (msg.getType()){
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(payload));
                break;
            case PING:
                out.add(new PingWebSocketFrame(payload));
                break;
            default:
                throw new IllegalStateException( "Unsupported websocket msg " + msg);
        }
    }

    /**
     * 将WebSocketFrame解码成MyWebSocketFrame，并设置FrameType
     * @param channelHandlerContext
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.content().duplicate().retain();
        if(msg instanceof BinaryWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.BINARY, payload));
        }else if(msg instanceof CloseWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.CLOSE, payload));
        }else if(msg instanceof PingWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.PING, payload));
        }else if(msg instanceof PongWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.PONG, payload));
        }else if(msg instanceof TextWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.TEXT, payload));
        }else if(msg instanceof ContinuationWebSocketFrame){
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.CONTINUATION, payload));
        }else{
            throw new IllegalStateException(
                    "Unsupported websocket msg " + msg);
        }
    }

    public static final class MyWebSocketFrame{
        public enum FrameType{
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }
        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data){
            this.type = type;
            this.data = data;
        }
        public FrameType getType(){
            return type;
        }
        public ByteBuf getData(){
            return data;
        }
    }
}
