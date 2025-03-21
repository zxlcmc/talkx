package org.bigmouth.gpt.xiaozhi.udp;

import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.integration.eventbus.EventPark;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.Device;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.entity.UserFriend;
import org.bigmouth.gpt.entity.UserFriendMediaConfig;
import org.bigmouth.gpt.entity.response.AudioConfigVo;
import org.bigmouth.gpt.service.IDeviceService;
import org.bigmouth.gpt.service.ISessionService;
import org.bigmouth.gpt.service.IUserFriendMediaConfigService;
import org.bigmouth.gpt.service.IUserFriendService;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiSileroConfig;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.tts.TtsPlatformType;
import org.bigmouth.gpt.xiaozhi.tts.TtsService;
import org.bigmouth.gpt.xiaozhi.tts.TtsServiceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Allen Hu
 * @date 2025/3/5
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class UdpClientContextBuilder {

    private final EventPark eventPark;
    private final UdpClientContextHolder udpClientContextHolder;
    private final XiaozhiSileroConfig xiaozhiSileroConfig;
    private final IDeviceService deviceService;
    private final IUserFriendMediaConfigService userFriendMediaConfigService;
    private final TtsServiceFactory ttsServiceFactory;
    private final ISessionService sessionService;
    private final IUserFriendService userFriendService;
    private final Counter counter;

    public UdpClientContext createIfAbsent(UdpHello udpHello, @Nullable Consumer<String> ifAbsentHandler) {
        String sessionId = udpHello.getSessionId();
        return udpClientContextHolder.computeIfAbsent(sessionId,
                s -> {
                    log.info("{} - Started new UdpClientContext", sessionId);
                    AudioVadListeningConsumer audioBufferConsumer = new AudioVadListeningConsumer(xiaozhiSileroConfig, eventPark, udpHello);

                    UdpClientContext.Builder builder = new UdpClientContext.Builder(udpHello)
                            .eventPark(eventPark)
                            .encoderUtils()
                            .decoderUtils()
                            .audioBufferQueue(1024, audioBufferConsumer)
                            .sileroVadListener(xiaozhiSileroConfig)
                            .audioResponseSequence(new AudioResponseSequence(counter, sessionId));

                    String clientId = udpHello.getRequest().getClientId();
                    Device device = deviceService.findByClientId(clientId);
                    if (Objects.nonNull(device)) {
                        // 创建 Session 话题
                        long userId = device.getUserId();
                        long friendId = device.getFriendId();
                        String roleType = device.getRoleType();
                        int productId = 0;
                        Session session = sessionService.createKeepSessionIfNecessary(sessionId, userId, friendId, productId, Duration.ofHours(8));
                        builder.session(session);

                        UserFriend userFriend = userFriendService.getById(device.getUserFriendId());
                        builder.userFriend(userFriend);

                        // 创建 TTS 服务
                        TtsPlatformType platformType = TtsPlatformType.TalkX;
                        String model = null, voice = null;
                        UserFriendMediaConfig friendMediaConfig = userFriendMediaConfigService.getByUserFriendId(device.getUserFriendId());
                        if (null != friendMediaConfig) {
                            platformType = TtsPlatformType.of(friendMediaConfig.getAudioPlatformType(), platformType);
                            model = friendMediaConfig.getAudioModel();
                            voice = friendMediaConfig.getAudioRole();

                            builder.userFriendMediaConfig(friendMediaConfig);
                        } else {
                            AudioConfigVo defConfig = userFriendMediaConfigService.getDefConfig(roleType);
                            if (null != defConfig) {
                                platformType = TtsPlatformType.of(defConfig.getPlatformType().name());
                                model = defConfig.getModel();
                                voice = defConfig.getRole();
                            }
                        }

                        TtsService ttsService = ttsServiceFactory.createInstance(sessionId, platformType, model, voice);
                        ttsService.init();
                        builder.ttsService(ttsService);
                    }

                    if (null != ifAbsentHandler) {
                        ifAbsentHandler.accept(sessionId);
                    }
                    return builder.build();
                });
    }
}
