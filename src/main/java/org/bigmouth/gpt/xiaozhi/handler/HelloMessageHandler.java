package org.bigmouth.gpt.xiaozhi.handler;

import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.utils.KeyBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiUdpConfig;
import org.bigmouth.gpt.xiaozhi.entity.*;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class HelloMessageHandler implements MessageHandler {

    private final XiaozhiUdpConfig xiaozhiUdpConfig;
    private final Fetcher fetcher;
    private final Updater updater;

    @Override
    public MessageType onType() {
        return MessageType.HELLO;
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        String nonceRandomString = createNonceRandomString();
        String nonce = createNonce(nonceRandomString);
        String key = generateRandomHex(32);
        String sessionId = createSessionId();

        AudioParams audioParams = AudioParams.builder()
                .format("opus")
                .sampleRate(16000)
                .channels(1)
                .frameDuration(60)
                .build();
        Udp udp = Udp.builder()
                .server(xiaozhiUdpConfig.getUdpServerHost())
                .port(xiaozhiUdpConfig.getUdpServerPort())
                .encryption("aes-128-ctr")
                .key(key)
                .nonce(nonce)
                .build();
        DataPacket response = DataPacket.builder()
                .type(MessageType.HELLO.getValue())
                .version(3)
                .transport("udp")
                .sessionId(sessionId)
                .audioParams(audioParams)
                .udp(udp)
                .build();

        UdpHello udpHello = new UdpHello();
        udpHello.setRequest(dataPacket);
        udpHello.setResponse(response);

        this.saveUdpHelloByNonceRandomString(nonceRandomString, udpHello);
        this.saveUdpHelloBySessionId(sessionId, udpHello);
        return response;
    }

    private static String createSessionId() {
        return generateRandomHex(8);
    }

    public static String createNonce(String nonceRandomString) {
        // 8 + 16 + 8
        return String.format("01000000%s00000000", nonceRandomString);
    }

    public static String createNonceRandomString() {
        return generateRandomHex(16);
    }

    public static String generateRandomHex(int length) {
        // 使用RandomStringUtils生成随机的十六进制字符串
        return RandomStringUtils.random(length, "0123456789abcdef");
    }

    /**
     * @param nonce 32位的nonce
     * @return DataPacket
     */
    public UdpHello getUdpHelloByNonce(String nonce) {
        String nonceRandomString = parseNonceRandomString(nonce);
        return fetcher.fetch(stringRandomString(nonceRandomString), UdpHello.class);
    }

    public UdpHello getUdpHelloBySessionId(String sessionId) {
        return fetcher.fetch(stringSessionId(sessionId), UdpHello.class);
    }

    public void saveUdpHelloByNonceRandomString(String nonceRandomString, UdpHello udpHello) {
        updater.update(stringRandomString(nonceRandomString), udpHello, 30 * 60);
    }

    public void saveUdpHelloBySessionId(String sessionId, UdpHello udpHello) {
        updater.update(stringSessionId(sessionId), udpHello, 30 * 60);
    }

    public void refreshByNonceRandomString(String nonceRandomString) {
        updater.expire(stringRandomString(nonceRandomString), 30 * 60);
    }

    public void refreshBySessionId(String sessionId) {
        updater.expire(stringSessionId(sessionId), 30 * 60);
    }

    private KeyGenerator stringRandomString(String nonceRandomString) {
        return () -> KeyBuilder.build("talkx", "udp", "nonce", nonceRandomString);
    }

    private KeyGenerator stringSessionId(String sessionId) {
        return () -> KeyBuilder.build("talkx", "udp", "sessionId", sessionId);
    }

    /**
     * @param nonce 01000000a085e3e1553434eb00000000
     * @return a085e3e1553434eb
     */
    public static String parseNonceRandomString(String nonce) {
        return StringUtils.substring(nonce, 8, 24);
    }
}
