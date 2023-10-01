package javanoio.netty.privateAggreement.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Header
 * @Description 协议数据的头部
 * @Author zzk
 * @Date 2023/9/12 21:06
 **/
@Data
public final class Header {

    private int crcCode = 0xabef0101;
    private int length;//消息长度
    private long sessionID;//会话ID
    private byte type;//消息类型
    private byte priority; //消息优先级
    private Map<String, Object> attachment = new HashMap<>(); //扩展
}
