package org.bigmouth.gpt.xiaozhi.udp;

import com.bxm.warcar.integration.eventbus.EventPark;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiSileroConfig;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.event.P2pMessageEvent;
import org.bigmouth.gpt.xiaozhi.event.VadEndEvent;
import org.bigmouth.gpt.xiaozhi.event.VadStartEvent;

import java.util.List;
import java.util.Map;

/**
 * 用于 auto 模式下监听用户说话活动
 *
 * @author Allen Hu
 * @date 2025/2/25
 */
@Slf4j
public class AudioVadListeningConsumer implements AudioBufferConsumer {

    private final XiaozhiSileroConfig xiaozhiSileroConfig;
    private final EventPark eventPark;
    private final UdpHello udpHello;
    private UdpClientContext context;

    public AudioVadListeningConsumer(XiaozhiSileroConfig xiaozhiSileroConfig, EventPark eventPark, UdpHello udpHello) {
        this.xiaozhiSileroConfig = xiaozhiSileroConfig;
        this.eventPark = eventPark;
        this.udpHello = udpHello;
    }

    @Override
    public void setContext(UdpClientContext context) {
        this.context = context;
    }

    @Override
    public void accept(List<Byte> bytes) {
        try {
            if (context.getState().get() == UdpClientContext.State.Goodbye) {
                return;
            }

            // 初始化 -> 开始监听
            context.compareAndSet(UdpClientContext.State.Init, UdpClientContext.State.ListenStart);

            // 如果发现已经说完话了，那么不再需要继续检查了。
            if (context.getState().get() == UdpClientContext.State.ListenEnd) {
                context.stopVADListener();
                return;
            }

            // 开始监听 -> 监听中
            if (context.compareAndSet(UdpClientContext.State.ListenStart, UdpClientContext.State.Listening)) {
                context.startVADListener();
            }

            // 只有当开始监听才处理数据。
            if (context.getState().get() == UdpClientContext.State.Listening) {
                byte[] cpm = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); i++) {
                    cpm[i] = bytes.get(i);
                }

                Map<String, Double> result = context.getSileroVadListener().listen(cpm);
                if (MapUtils.isNotEmpty(result)) {
                    log.info("{} - 检测到语音活动事件。{}", result, udpHello);
                    boolean isStart = result.containsKey("start");
                    if (isStart) {
                        eventPark.post(new VadStartEvent(result, context));
                    }
                    boolean isEnd = result.containsKey("end");
                    if (isEnd) {
                        context.set(UdpClientContext.State.ListenEnd);
                        eventPark.post(new VadEndEvent(result, context));
                    }
                }

                if (context.isVadListenTimeout(xiaozhiSileroConfig.getTimeoutSec())) {
                    // 超过 10 秒没有检测到语音，那么就主动 Say goodbye!
                    String sessionId = udpHello.getSessionId();
                    DataPacket dataPacket = DataPacket.builder().type(MessageType.GOODBYE.getValue()).sessionId(sessionId).build();
                    eventPark.post(new P2pMessageEvent(this, context, dataPacket));
                    log.info("{} VAD listen timeout, Say goodbye!", sessionId);

                    // 主动设置状态为 Goodbye
                    context.destroy();
                }
            }
        } catch (Exception e) {
            log.error("accept error: ", e);
        }
    }
}
