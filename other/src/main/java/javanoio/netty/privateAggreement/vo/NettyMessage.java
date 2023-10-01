package javanoio.netty.privateAggreement.vo;

import javanoio.netty.privateAggreement.vo.Header;
import lombok.Data;

/**
 * @ClassName NettyMessage
 * @Description 消息结构
 * @Author zzk
 * @Date 2023/9/12 21:04
 **/
@Data
public final class NettyMessage {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private Object body;
}
