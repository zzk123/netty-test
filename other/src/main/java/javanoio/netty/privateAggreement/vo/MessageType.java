package javanoio.netty.privateAggreement.vo;

/**
 * @ClassName MessageType
 * @Description 消息类型定义
 * @Author zzk
 * @Date 2023/9/12 22:10
 **/
public class MessageType {

    /**
     * 握手请求消息
     */
    public static final byte LOGIN_REQ = 3;

    /**
     * 握手应答消息
     */
    public static final byte LOGIN_RESP = 4;

    /**
     * 心跳消息
     */
    public static final byte HEARTBEAT_REQ = 5;

    /**
     * 心跳应答
     */
    public static final byte HEARTBEAT_RESP = 6;
}


