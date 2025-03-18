//package org.bigmouth.gpt.xiaozhi.tts;
//
//import cn.hutool.core.io.IoUtil;
//import com.google.common.base.Preconditions;
//import com.google.gson.JsonObject;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import okio.ByteString;
//import org.bigmouth.gpt.xiaozhi.audio.PcmToWavUtil;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Consumer;
//
///**
// * @author Allen Hu
// * @date 2025/2/26
// */
//@Slf4j
//public class ByteDanceTtsService implements TtsService {
//
//    private static final int PROTOCOL_VERSION = 0b0001;
//    private static final int DEFAULT_HEADER_SIZE = 0b0001;
//
//    // Message Type:
//    private static final int FULL_CLIENT_REQUEST = 0b0001;
//    private static final int AUDIO_ONLY_RESPONSE = 0b1011;
//    private static final int FULL_SERVER_RESPONSE = 0b1001;
//    private static final int ERROR_INFORMATION = 0b1111;
//
//    // Message Type Specific Flags
//    private static final int MsgTypeFlagNoSeq = 0b0000; // Non-terminal packet with no sequence
//    private static final int MsgTypeFlagPositiveSeq = 0b1;// Non-terminal packet with sequence > 0
//    private static final int MsgTypeFlagLastNoSeq = 0b10;// last packet with no sequence
//    private static final int MsgTypeFlagNegativeSeq = 0b11; // Payload contains event number (int32)
//    private static final int MsgTypeFlagWithEvent = 0b100;
//    // Message Serialization
//    private static final int NO_SERIALIZATION = 0b0000;
//    private static final int JSON = 0b0001;
//    // Message Compression
//    private static final int COMPRESSION_NO = 0b0000;
//    private static final int COMPRESSION_GZIP = 0b0001;
//
//    // event
//
//
//    // 默认事件,对于使用事件的方案，可以通过非0值来校验事件的合法性
//    public static final int EVENT_NONE = 0;
//
//    public static final int EVENT_Start_Connection = 1;
//
//
//    // 上行Connection事件
//    public static final int EVENT_FinishConnection = 2;
//
//    // 下行Connection事件
//    public static final int EVENT_ConnectionStarted = 50; // 成功建连
//
//    public static final int EVENT_ConnectionFailed = 51; // 建连失败（可能是无法通过权限认证）
//
//    public static final int EVENT_ConnectionFinished = 52; // 连接结束
//
//    // 上行Session事件
//    public static final int EVENT_StartSession = 100;
//
//    public static final int EVENT_FinishSession = 102;
//
//    // 下行Session事件
//    public static final int EVENT_SessionStarted = 150;
//
//    public static final int EVENT_SessionFinished = 152;
//
//    public static final int EVENT_SessionFailed = 153;
//
//    // 上行通用事件
//    public static final int EVENT_TaskRequest = 200;
//
//    // 下行TTS事件
//    public static final int EVENT_TTSSentenceStart = 350;
//
//    public static final int EVENT_TTSSentenceEnd = 351;
//
//    public static final int EVENT_TTSResponse = 352;
//
//    public static final String API_URL = "wss://openspeech.bytedance.com/api/v3/tts/bidirection";
//    private static final String appid = "1136998193";
//    private static final String accessToken = "SHArLMgmwKX0yEUMKKAIUdIrjjKUU6Jq";
//    private static final String speaker = "zh_female_wanwanxiaohe_moon_bigtts";
//
//    private final String sessionId = UUID.randomUUID().toString().replace("-", "");
//    private WebSocket webSocket;
//    private Consumer<byte[]> frameHandler;
//    private Consumer<byte[]> finishHandler;
//
//    @Override
//    public TtsPlatformType of() {
//        return TtsPlatformType.ByteDance;
//    }
//
//    @Override
//    public void init(Consumer<byte[]> frameHandler, Consumer<byte[]> finishHandler) {
//        Preconditions.checkNotNull(frameHandler);
//        this.frameHandler = frameHandler;
//        this.finishHandler = finishHandler;
//        try {
//            CountDownLatch latch = new CountDownLatch(1);
//            start0(latch);
//            boolean await = latch.await(10, TimeUnit.SECONDS);
//            if (!await) {
//                throw new IOException("time out on connect");
//            }
//        } catch (Exception e) {
//            log.error("===>start0 error:", e);
//        }
//    }
//
//    @Override
//    public void stream(String text) {
//        log.info(text);
//        sendTTSMessage(webSocket, speaker, sessionId, text);
//    }
//
//    @Override
//    public void finish() {
//        finishSession(webSocket, sessionId);
//    }
//
//    private void start0(CountDownLatch countDownLatch) {
//        final Request request = new Request.Builder()
//                .url(API_URL)
//                .header("X-Api-App-Key", appid)
//                .header("X-Api-Access-Key", accessToken)
//                .header("X-Api-Resource-Id", "volc.service_type.10029")
//                .header("X-Api-Connect-Id", UUID.randomUUID().toString())
//                .build();
//
//        final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS)
//                .readTimeout(100, TimeUnit.SECONDS)
//                .writeTimeout(100, TimeUnit.SECONDS)
//                .build();
//
//        this.webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
//
//            @Override
//            public void onOpen(WebSocket webSocket, Response response) {
//                log.debug("===>onOpen: X-Tt-Logid:" + response.header("X-Tt-Logid"));
//                startConnection(webSocket);
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, ByteString bytes) {
//                TTSResponse response = parserResponse(bytes.toByteArray());
//
//                switch (response.optional.event) {
//                    case EVENT_ConnectionFailed:
//                    case EVENT_SessionFailed: {
//                        log.error("===>response error:" + response.optional.event);
//                    }
//                    case EVENT_ConnectionStarted:
//                        startTTSSession(webSocket, sessionId, speaker);
//                        break;
//                    case EVENT_SessionStarted:
//                        countDownLatch.countDown();
//                        break;
//                    case EVENT_TTSSentenceStart:
//                        log.debug("===>response TTSSentenceStart:" + response.optional.event);
//                        break;
//                    case EVENT_TTSResponse: {
//                        log.debug("===>response TTSResponse:" + response.optional.event);
//                        if (response.payload == null) {
//                            return;
//                        }
//                        // 输出结果
//                        if (response.header.message_type == AUDIO_ONLY_RESPONSE) {
//                            frameHandler.accept(response.payload);
//                        } else if (response.header.message_type == FULL_SERVER_RESPONSE) {
//                            log.debug("===> payload:" + new String(response.payload));
//                        }
//                        break;
//                    }
//                    case EVENT_TTSSentenceEnd:
//                        log.debug("===>response TTSSentenceEnd:" + response.optional.event);
//                        break;
//                    case EVENT_SessionFinished:
//                        log.debug("===>response SessionFinished:" + response.optional.event);
//                        finishConnection(webSocket);
//                        if (null != finishHandler) {
//                            finishHandler.accept(null);
//                        }
//                        break;
//                    case EVENT_ConnectionFinished:
//                        log.debug("===>response ConnectionFinished:" + response.optional.event);
//                    default:
//                        log.debug("===>response default:" + response.optional.event);
//                        break;
//                }
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//            }
//
//            public void onClosing(WebSocket webSocket, int code, String reason) {
//            }
//
//            @Override
//            public void onClosed(WebSocket webSocket, int code, String reason) {
//            }
//
//            @Override
//            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                log.error("onFailure", t);
//            }
//        });
//    }
//
//    @Override
//    public void destroy() {
//        if (null != webSocket) {
//            webSocket.close(0, "");
//        }
//    }
//
//    static int bytesToInt(byte[] src) {
//        if (src == null || (src.length != 4)) {
//            throw new IllegalArgumentException("");
//        }
//        return ((src[0] & 0xFF) << 24)
//                | ((src[1] & 0xff) << 16)
//                | ((src[2] & 0xff) << 8)
//                | ((src[3] & 0xff));
//    }
//
//    static byte[] intToBytes(int a) {
//        return new byte[]{
//                (byte) ((a >> 24) & 0xFF),
//                (byte) ((a >> 16) & 0xFF),
//                (byte) ((a >> 8) & 0xFF),
//                (byte) (a & 0xFF)
//
//        };
//    }
//
//    public static class Header {
//
//        public int protocol_version = PROTOCOL_VERSION;
//        public int header_size = DEFAULT_HEADER_SIZE;
//        public int message_type;
//        public int message_type_specific_flags = MsgTypeFlagWithEvent;
//        public int serialization_method = NO_SERIALIZATION;
//        public int message_compression = COMPRESSION_NO;
//        public int reserved = 0;
//
//        public Header() {
//        }
//
//        public Header(int protocol_version, int header_size, int message_type, int message_type_specific_flags,
//                      int serialization_method, int message_compression, int reserved) {
//            this.protocol_version = protocol_version;
//            this.header_size = header_size;
//            this.message_type = message_type;
//            this.message_type_specific_flags = message_type_specific_flags;
//            this.serialization_method = serialization_method;
//            this.message_compression = message_compression;
//            this.reserved = reserved;
//        }
//
//        /**
//         * 转成 byte 数组
//         *
//         * @return
//         */
//        public byte[] getBytes() {
//            return new byte[]{
//                    // Protocol version | Header size (4x)
//                    (byte) ((protocol_version << 4) | header_size),
//                    // Message type | Message type specific flags
//                    (byte) (message_type << 4 | message_type_specific_flags),
//                    // Serialization method | Compression method
//                    (byte) ((serialization_method << 4) | message_compression),
//                    (byte) reserved
//            };
//        }
//    }
//
//    public static class Optional {
//
//        public int size;
//        public int event = EVENT_NONE;
//        public String sessionId;
//
//        public int errorCode;
//        public int connectionSize;
//        public String connectionId;
//
//        public String response_meta_json;
//        public int sequence = -1;
//
//        public Optional(int event, String sessionId, int sequence) {
//            this.event = event;
//            this.sessionId = sessionId;
//            this.sequence = sequence;
//        }
//
//        public Optional() {
//        }
//
//        public byte[] getBytes() {
//            byte[] bytes = new byte[0];
//            if (event != EVENT_NONE) {
//                bytes = intToBytes(event);
//            }
//            if (sessionId != null) {
//                byte[] sessionIdSize = intToBytes(sessionId.getBytes().length);
//                final byte[] temp = bytes;
//                int desPos = 0;
//                bytes = new byte[temp.length + sessionIdSize.length + sessionId.getBytes().length];
//                System.arraycopy(temp, 0, bytes, desPos, temp.length);
//                desPos += temp.length;
//                System.arraycopy(sessionIdSize, 0, bytes, desPos, sessionIdSize.length);
//                desPos += sessionIdSize.length;
//                System.arraycopy(sessionId.getBytes(), 0, bytes, desPos, sessionId.getBytes().length);
//
//            }
//            if (sequence >= 0) {
//                byte[] sequenceBytes = intToBytes(sequence);
//                final byte[] temp = bytes;
//                bytes = new byte[temp.length + sequenceBytes.length];
//                System.arraycopy(temp, 0, bytes, 0, temp.length);
//                System.arraycopy(sequenceBytes, 0, bytes, temp.length, sequenceBytes.length);
//            }
//            return bytes;
//        }
//    }
//
//    public static class TTSResponse {
//
//        public Header header;
//        public Optional optional;
//        public int payloadSize;
//        transient public byte[] payload;
//
//
//    }
//
//
//    /**
//     * 解析响应包
//     *
//     * @param res
//     * @return
//     */
//    static TTSResponse parserResponse(byte[] res) {
//        if (res == null || res.length == 0) {
//            return null;
//        }
//        final TTSResponse response = new TTSResponse();
//        Header header = new Header();
//        response.header = header;
//
//        // 当符号位为1时进行 >> 运算后高位补1（预期是补0），导致结果错误，所以增加个数再与其& 运算，目的是确保高位是补0.
//        final byte num = 0b00001111;
//        // header 32 bit=4 byte
//        header.protocol_version = (res[0] >> 4) & num;
//        header.header_size = res[0] & 0x0f;
//        header.message_type = (res[1] >> 4) & num;
//        header.message_type_specific_flags = res[1] & 0x0f;
//        header.serialization_method = res[2] >> num;
//        header.message_compression = res[2] & 0x0f;
//        header.reserved = res[3];
//
//        int offset = 4;
//        response.optional = new Optional();
////        System.out.println("===>parserResponse:header:" + new Gson().toJson(header));
//        // 正常Response
//        if (header.message_type == FULL_SERVER_RESPONSE || header.message_type == AUDIO_ONLY_RESPONSE) {
//            // 如果有event
//            offset += readEvent(res, header.message_type_specific_flags, response);
//            final int event = response.optional.event;
//            // 根据 event 类型解析
//            switch (event) {
//                case EVENT_NONE:
//                    break;
//                case EVENT_ConnectionStarted:
//                    readConnectStarted(res, response, offset);
//                    break;
//                case EVENT_ConnectionFailed:
//                    readConnectFailed(res, response, offset);
//                    break;
//                case EVENT_SessionStarted:
//                case EVENT_SessionFailed:
//                case EVENT_SessionFinished:
//                    offset += readSessionId(res, response, offset);
//                    readMetaJson(res, response, offset);
//                    break;
//                default:
//                    offset += readSessionId(res, response, offset);
//                    offset += readSequence(res, response, offset);
//                    readPayload(res, response, offset);
//                    break;
//            }
//        }
//        // 错误
//        else if (header.message_type == ERROR_INFORMATION) {
//            offset += readErrorCode(res, response, offset);
//            readPayload(res, response, offset);
//        }
//        return response;
//    }
//
//    static void readConnectStarted(byte[] res, TTSResponse response, int start) {
//        // 8--11: connection id size
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        start += b.length;
//        response.optional.size += b.length;
//        response.optional.connectionSize = bytesToInt(b);
////        System.out.println("===>readConnectStarted connectionSize:" + response.optional.connectionSize);
//        b = new byte[response.optional.connectionSize];
//        System.arraycopy(res, start, b, 0, b.length);
//        start += b.length;
//        response.optional.size += b.length;
//        // 12--18: connection id size
//        response.optional.connectionId = new String(b);
////        System.out.println("===>readConnectStarted connectionId:" + response.optional.connectionId);
//        readPayload(res, response, start);
//    }
//
//    static void readConnectFailed(byte[] res, TTSResponse response, int start) {
//        // 8--11: connection id size
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        response.optional.size += b.length;
//        start += b.length;
//        response.optional.connectionSize = bytesToInt(b);
////        System.out.println("===>connectionSize:" + response.optional.connectionSize);
//        readMetaJson(res, response, start);
//    }
//
//
//    static int readSequence(byte[] res, TTSResponse response, int start) {
//        final Header header = response.header;
//        final Optional optional = response.optional;
//        if (header.message_type_specific_flags == MsgTypeFlagNegativeSeq
//                || header.message_type_specific_flags == MsgTypeFlagPositiveSeq) {
//            byte[] temp = new byte[4];
//            System.arraycopy(res, start, temp, 0, temp.length);
//            optional.sequence = bytesToInt(temp);
//            optional.size += temp.length;
////            System.out.println("===>sequence:" + optional.sequence);
//            return temp.length;
//        }
//        return 0;
//    }
//
//    static void readMetaJson(byte[] res, TTSResponse response, int start) {
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        start += b.length;
//        response.optional.size += b.length;
//        int size = bytesToInt(b);
//        b = new byte[size];
//        System.arraycopy(res, start, b, 0, b.length);
//        response.optional.size += b.length;
//        response.optional.response_meta_json = new String(b);
////        System.out.println("===> response_meta_json:" + response.optional.response_meta_json);
//    }
//
//    static int readPayload(byte[] res, TTSResponse response, int start) {
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        start += b.length;
//        int size = bytesToInt(b);
//        response.payloadSize += size;
//        b = new byte[size];
//        System.arraycopy(res, start, b, 0, b.length);
//        response.payload = b;
//        if (response.optional.event == FULL_SERVER_RESPONSE) {
//            log.debug("===> payload:" + new String(b));
//        }
//        return 4 + size;
//    }
//
//    static int readErrorCode(byte[] res, TTSResponse response, int start) {
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        response.optional.errorCode = bytesToInt(b);
//        response.optional.size += b.length;
//        return b.length;
//    }
//
//
//    static int readEvent(byte[] res, int masTypeFlag, TTSResponse response) {
//        if (masTypeFlag == MsgTypeFlagWithEvent) {
//            byte[] temp = new byte[4];
//            System.arraycopy(res, 4, temp, 0, temp.length);
//            int event = bytesToInt(temp);
//            response.optional.event = event;
//            response.optional.size += 4;
//            log.debug("===>event:" + event);
//            return temp.length;
//        }
//        return 0;
//    }
//
//
//    static int readSessionId(byte[] res, TTSResponse response, int start) {
//        byte[] b = new byte[4];
//        System.arraycopy(res, start, b, 0, b.length);
//        start += b.length;
//        final int size = bytesToInt(b);
//        byte[] sessionIdBytes = new byte[size];
//        System.arraycopy(res, start, sessionIdBytes, 0, sessionIdBytes.length);
//        response.optional.sessionId = new String(sessionIdBytes);
//        log.debug("===>sessionId:" + response.optional.sessionId);
//        return b.length + size;
//    }
//
//
//    static boolean startConnection(WebSocket webSocket) {
//        byte[] header = new Header(
//                PROTOCOL_VERSION,
//                FULL_CLIENT_REQUEST,
//                DEFAULT_HEADER_SIZE,
//                MsgTypeFlagWithEvent,
//                JSON,
//                COMPRESSION_NO,
//                0).getBytes();
//        byte[] optional = new Optional(EVENT_Start_Connection, null, -1).getBytes();
//        byte[] payload = "{}".getBytes();
//        return sendEvent(webSocket, header, optional, payload);
//    }
//
//    static boolean finishConnection(WebSocket webSocket) {
//        byte[] header = new Header(
//                PROTOCOL_VERSION,
//                FULL_CLIENT_REQUEST,
//                DEFAULT_HEADER_SIZE,
//                MsgTypeFlagWithEvent,
//                JSON,
//                COMPRESSION_NO,
//                0).getBytes();
//        byte[] optional = new Optional(EVENT_FinishConnection, null, -1).getBytes();
//        byte[] payload = "{}".getBytes();
//        return sendEvent(webSocket, header, optional, payload);
//    }
//
//    static boolean finishSession(WebSocket webSocket, String sessionId) {
//        byte[] header = new Header(
//                PROTOCOL_VERSION,
//                FULL_CLIENT_REQUEST,
//                DEFAULT_HEADER_SIZE,
//                MsgTypeFlagWithEvent,
//                JSON,
//                COMPRESSION_NO,
//                0).getBytes();
//        byte[] optional = new Optional(EVENT_FinishSession, sessionId, -1).getBytes();
//        byte[] payload = "{}".getBytes();
//        return sendEvent(webSocket, header, optional, payload);
//    }
//
//    static boolean startTTSSession(WebSocket webSocket, String sessionId, String speaker) {
//        byte[] header = new Header(
//                PROTOCOL_VERSION,
//                FULL_CLIENT_REQUEST,
//                DEFAULT_HEADER_SIZE,
//                MsgTypeFlagWithEvent,
//                JSON,
//                COMPRESSION_NO,
//                0).getBytes();
//
//        final int event = EVENT_StartSession;
//        byte[] optional = new Optional(event, sessionId, -1).getBytes();
//        JsonObject payloadJObj = new JsonObject();
//        JsonObject user = new JsonObject();
//        user.addProperty("uid", "123456");
//        payloadJObj.add("user", user);
//        payloadJObj.addProperty("event", event);
//        payloadJObj.addProperty("namespace", "BidirectionalTTS");
//
//        JsonObject req_params = new JsonObject();
//        JsonObject audio_params = new JsonObject();
//        audio_params.addProperty("format", "mp3");
//        audio_params.addProperty("sample_rate", 24000);
//        req_params.addProperty("speaker", speaker);
//        req_params.add("audio_params", audio_params);
//        payloadJObj.add("req_params", req_params);
//        byte[] payload = payloadJObj.toString().getBytes();
//        return sendEvent(webSocket, header, optional, payload);
//    }
//
//    static boolean sendTTSMessage(WebSocket webSocket, String speaker, String sessionId, String text) {
//        return sendMessageWithSeq(webSocket, speaker, sessionId, text, -1);
//    }
//
//    /**
//     * 分段合成音频
//     *
//     * @param webSocket
//     * @param speaker
//     * @param sessionId
//     * @param text
//     * @param seq
//     * @return
//     */
//    static boolean sendMessageWithSeq(WebSocket webSocket, String speaker, String sessionId, String text, int seq) {
//        byte[] header = new Header(
//                PROTOCOL_VERSION,
//                FULL_CLIENT_REQUEST,
//                DEFAULT_HEADER_SIZE,
//                MsgTypeFlagWithEvent,
//                JSON,
//                COMPRESSION_NO,
//                0).getBytes();
//
//        byte[] optional = new Optional(EVENT_TaskRequest, sessionId, seq).getBytes();
//
//        JsonObject payloadJObj = new JsonObject();
//
//        payloadJObj.addProperty("event", EVENT_TaskRequest);
//        payloadJObj.addProperty("namespace", "BidirectionalTTS");
//
//        JsonObject req_params = new JsonObject();
//        req_params.addProperty("text", text);
//        req_params.addProperty("speaker", speaker);
//        JsonObject audio_params = new JsonObject();
//        audio_params.addProperty("format", "wav");
//        audio_params.addProperty("sample_rate", 16000);
//        req_params.add("audio_params", audio_params);
//        payloadJObj.add("req_params", req_params);
//        byte[] payload = payloadJObj.toString().getBytes();
//        return sendEvent(webSocket, header, optional, payload);
//    }
//
//    static boolean sendEvent(WebSocket webSocket, byte[] header, byte[] optional, byte[] payload) {
//        assert webSocket != null;
//        assert header != null;
//        assert payload != null;
//        final byte[] payloadSizeBytes = intToBytes(payload.length);
//        byte[] requestBytes = new byte[
//                header.length
//                        + (optional == null ? 0 : optional.length)
//                        + payloadSizeBytes.length + payload.length];
//        int desPos = 0;
//        System.arraycopy(header, 0, requestBytes, desPos, header.length);
//        desPos += header.length;
//        if (optional != null) {
//            System.arraycopy(optional, 0, requestBytes, desPos, optional.length);
//            desPos += optional.length;
//        }
//        System.arraycopy(payloadSizeBytes, 0, requestBytes, desPos, payloadSizeBytes.length);
//        desPos += payloadSizeBytes.length;
//        System.arraycopy(payload, 0, requestBytes, desPos, payload.length);
//        return webSocket.send(ByteString.of(requestBytes));
//    }
//
//    public static void main(String[] args) throws Exception {
//        String[] text = new String[] {
//                "从前有一只小狐狸，居住在森林里。它非常好奇，喜欢四处探险。有一天，它发现了一条美丽的小河。",
//                "小狐狸决定沿着河流探险，结果遇到了一只友善的小鸟。小鸟告诉它，河的另一边有个神奇的花园。",
//                "于是，小狐狸和小鸟一起穿越河流，发现了五颜六色的花朵和甜美的果实。",
//                "从此，它们成了最好的朋友，每天一起去探索新奇的地方。这让小狐狸明白了，友谊是最珍贵的冒险。"
//        };
//
//        ByteDanceTtsService service = new ByteDanceTtsService();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Consumer<byte[]> consumer = new Consumer<byte[]>() {
//            @Override
//            public void accept(byte[] bytes) {
//                try {
//                    log.debug("---> receive: {}", bytes.length);
//                    outputStream.write(bytes);
//                } catch (IOException e) {
//                }
//            }
//        };
//        Consumer<byte[]> finishHandler = new Consumer<byte[]>() {
//            @Override
//            public void accept(byte[] bytes) {
//                try {
//                    byte[] pcm = (outputStream.toByteArray());
////                    byte[] byteArray = outputStream.toByteArray();
////                    byte[] removeHeader = PcmToWavUtil.removeHeader(byteArray);
////                    byte[] pcm = PcmToWavUtil.pcmToWav(removeHeader);
//                    IoUtil.write(new FileOutputStream("/Users/huxiao/Downloads/" + System.currentTimeMillis() + ".wav"), true, pcm);
//                } catch (FileNotFoundException e) {
//                }
//            }
//        };
//        service.init(consumer, finishHandler);
//
//        for (String s : text) {
//            log.debug("send text: {}", s);
//            service.stream(s);
//        }
//        service.finish();
//
//        Thread.sleep(Integer.MAX_VALUE);
//        service.destroy();
//    }
//}
