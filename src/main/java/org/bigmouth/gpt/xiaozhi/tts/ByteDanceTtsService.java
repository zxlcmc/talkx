package org.bigmouth.gpt.xiaozhi.tts;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.audio.PcmToWavUtil;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class ByteDanceTtsService implements TtsService {
    private final String url;
    private final String appId;
    private final String accessToken;
    private final String voiceType;

    private TtsWebsocketClient websocketClient;

    public ByteDanceTtsService(String url, String appId, String accessToken, String voiceType) {
        this.url = url;
        this.appId = appId;
        this.accessToken = accessToken;
        this.voiceType = voiceType;
    }

    @Override
    public TtsPlatformType of() {
        return TtsPlatformType.ByteDance;
    }

    @Override
    public void stream(String text, Consumer<byte[]> frameHandler) {
        this.websocketClient.setFrameHandler(frameHandler);

        TtsRequest ttsRequest = TtsRequest.builder()
                .app(TtsRequest.App.builder()
                        .appid(appId)
                        .cluster("volcano_tts")
                        .build())
                .user(TtsRequest.User.builder()
                        .uid("uid")
                        .build())
                .audio(TtsRequest.Audio.builder()
                        .encoding("pcm")
                        .rate(16000)
                        .voiceType(voiceType)
                        .build())
                .request(TtsRequest.Request.builder()
                        .reqID(UUID.randomUUID().toString())
                        .operation("query")
                        .text(text)
                        .build())
                .build();
        try {
            websocketClient.submit(ttsRequest);
        } catch (InterruptedException e) {
            log.error("stream: ", e);
        }
    }

    @Override
    public void init() {
        websocketClient = new TtsWebsocketClient(url, accessToken);
        websocketClient.doConnect();
    }

    @Override
    public void destroy() {
        websocketClient.doClose();
    }

    public static class TtsWebsocketClient extends WebSocketClient {

        private Consumer<byte[]> frameHandler;

        public TtsWebsocketClient(String url, String accessToken) {
            super(URI.create(url), Collections.singletonMap("Authorization", "Bearer; " + accessToken));
        }

        public TtsWebsocketClient setFrameHandler(Consumer<byte[]> frameHandler) {
            this.frameHandler = frameHandler;
            return this;
        }

        public void submit(TtsRequest ttsRequest) throws InterruptedException {
            String json = JSON.toJSONString(ttsRequest);
            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
            byte[] header = {0x11, 0x10, 0x10, 0x00};
            ByteBuffer requestByte = ByteBuffer.allocate(8 + jsonBytes.length);
            requestByte.put(header).putInt(jsonBytes.length).put(jsonBytes);
            synchronized (this) {
                this.doSend(requestByte);
                wait();
            }
        }

        public void doSend(ByteBuffer requestByte) {
            byte[] array = requestByte.array();
            try {
                this.send(array);
            } catch (WebsocketNotConnectedException e) {
                if (doReconnect()) {
                    this.send(array);
                } else {
                    log.error("doSend: ", e);
                }
            }
        }

        public boolean doReconnect() {
            int times = 0;
            while (times++ < 3) {
                try {
                    this.reconnectBlocking();
                    return true;
                } catch (InterruptedException e) {
                    log.error("reconnect: ", e);
                }
            }
            return false;
        }

        public boolean doConnect() {
            int times = 0;
            while (times++ < 3) {
                try {
                    this.connectBlocking();
                    return true;
                } catch (InterruptedException e) {
                    log.error("reconnect: ", e);
                }
            }
            return false;
        }

        public void doClose() {
            this.close(CloseFrame.NORMAL, "received all audio data.");
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            int protocolVersion = (bytes.get(0) & 0xff) >> 4;
            int headerSize = bytes.get(0) & 0x0f;
            int messageType = (bytes.get(1) & 0xff) >> 4;
            int messageTypeSpecificFlags = bytes.get(1) & 0x0f;
            int serializationMethod = (bytes.get(2) & 0xff) >> 4;
            int messageCompression = bytes.get(2) & 0x0f;
            int reserved = bytes.get(3) & 0xff;
            bytes.position(headerSize * 4);
            byte[] fourByte = new byte[4];
            if (messageType == 11) {
                // Audio-only server response
                log.debug("received audio-only response.");
                if (messageTypeSpecificFlags == 0) {
                    // Ack without audio data
                } else {
                    bytes.get(fourByte, 0, 4);
                    int sequenceNumber = new BigInteger(fourByte).intValue();
                    bytes.get(fourByte, 0, 4);
                    int payloadSize = new BigInteger(fourByte).intValue();
                    byte[] payload = new byte[payloadSize];
                    bytes.get(payload, 0, payloadSize);
                    frameHandler.accept(payload);
                    if (sequenceNumber < 0) {
                        // received the last segment
                        notify0();
                    }
                }
            } else if (messageType == 15) {
                // Error message from server
                bytes.get(fourByte, 0, 4);
                int code = new BigInteger(fourByte).intValue();
                bytes.get(fourByte, 0, 4);
                int messageSize = new BigInteger(fourByte).intValue();
                byte[] messageBytes = new byte[messageSize];
                bytes.get(messageBytes, 0, messageSize);
                String message = new String(messageBytes, StandardCharsets.UTF_8);
                throw new TtsException(code, message);
            } else {
                log.warn("Received unknown response message type: {}", messageType);
            }
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            log.debug("opened connection");
        }

        @Override
        public void onMessage(String message) {
            log.debug("received message: " + message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            log.debug("Connection closed by {}, Code: {}, Reason: {}", (remote ? "remote" : "us"), code, reason);
            this.notify0();
        }

        @Override
        public void onError(Exception e) {
            close(CloseFrame.NORMAL, e.toString());
        }

        private void notify0() {
            synchronized (this) {
                notify();
            }
        }
    }

    @Getter
    public static class TtsException extends RuntimeException {
        private final int code;
        private final String message;

        public TtsException(int code, String message) {
            super("code=" + code + ", message=" + message);
            this.code = code;
            this.message = message;
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "wss://openspeech.bytedance.com/api/v1/tts/ws_binary";
        String appId = System.getenv("BYTEDANCE_TTS_APP_ID");
        String accessToken = System.getenv("BYTEDANCE_TTS_ACCESS_TOKEN");
        String voiceType = "zh_female_wanwanxiaohe_moon_bigtts";
        ByteDanceTtsService websocket = new ByteDanceTtsService(url, appId, accessToken, voiceType);
        websocket.init();

        List<byte[]> pcm = new ArrayList<>();
        websocket.stream("哎呀，师傅，您别生气，我这不是一时没想起来嘛。", new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                pcm.add(bytes);
            }
        });
        IoUtil.write(new FileOutputStream("/Users/huxiao/Downloads/" + System.currentTimeMillis() + ".wav"), true, PcmToWavUtil.pcmToWav(UdpClientContext.mergeByteArrays(pcm)));
//        websocket.destroy();

//        websocket.init();
        List<byte[]> pcm2 = new ArrayList<>();

        websocket.stream("从前有一只小狐狸，居住在森林里。它非常好奇，喜欢四处探险。有一天，它发现了一条美丽的小河。小狐狸决定沿着河流探险，结果遇到了一只友善的小鸟。小鸟告诉它，河的另一边有个神奇的花园。于是，小狐狸和小鸟一起穿越河流，发现了五颜六色的花朵和甜美的果实。从此，它们成了最好的朋友，每天一起去探索新奇的地方。这让小狐狸明白了，友谊是最珍贵的冒险。", new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                pcm2.add(bytes);
            }
        });
        IoUtil.write(new FileOutputStream("/Users/huxiao/Downloads/" + System.currentTimeMillis() + ".wav"), true, PcmToWavUtil.pcmToWav(UdpClientContext.mergeByteArrays(pcm2)));

        websocket.destroy();
    }
}