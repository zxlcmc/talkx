package org.bigmouth.gpt.xiaozhi.handler;

import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;

/**
 * 消息处理器接口
 * @author Allen Hu
 * @date 2025/2/20
 */
public interface MessageHandler {

    /**
     * 处理消息类型
     * @return 消息类型
     */
    MessageType onType();
    
    /**
     * 处理消息
     * @param dataPacket 消息数据包
     * @return 处理后的数据包，如果为空则不响应
     */
    DataPacket handle(DataPacket dataPacket);
}