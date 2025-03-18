package org.bigmouth.gpt.ai.service;

import com.bxm.warcar.utils.JsonHelper;
import com.bxm.warcar.utils.StringHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.bigmouth.gpt.Application;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.ai.Speech2TextService;
import org.bigmouth.gpt.ai.entity.ApiKey;
import org.bigmouth.gpt.ai.entity.voice.Speech2TextArgument;
import org.bigmouth.gpt.exceptions.AiAccountException;
import org.bigmouth.gpt.exceptions.AiLimitException;
import org.bigmouth.gpt.exceptions.AiNetworkException;
import org.bigmouth.gpt.exceptions.AiStatusException;
import org.bigmouth.gpt.utils.AudioUtils;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.utils.HttpClientHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author huxiao
 * @date 2023/11/10
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class OpenAiSpeech2TextServiceImpl implements Speech2TextService {

    private final PoolingHttpClientConnectionManager connectionManager;
    private final HttpClient client;
    private final ApplicationConfig config;

    public OpenAiSpeech2TextServiceImpl(ApplicationConfig config) {
        this.config = config;
        this.connectionManager = HttpClientHelper.createConnectionManager(200, 40);
        this.client = HttpClientHelper.createHttpClient(1000, 5000, 0, connectionManager);
    }

    @Override
    public String transcriptions(Speech2TextArgument argument) {
        String reqId = argument.getId();
        ApiKey apiKey = argument.getApiKey();

        String url = UriComponentsBuilder.fromUriString(config.getLlmApiHost())
                .path("/v1/audio/transcriptions")
                .build()
                .toString();
        String requestUri = Application.getRequestUri(null, url);
        HttpPost request = new HttpPost(requestUri);
        try {
            request.addHeader("Authorization", "Bearer " + config.getLlmApiKey());

            String contentType = argument.getContentType();
            log.info("{}, {}", contentType, argument.getOriginalFilename());

            // 如果不是webm，统一转成webm格式。
            String webmContentTypeMime = "audio/webm";
            byte[] webmBytes = argument.getBytes();
            if (!StringUtils.equals(contentType, webmContentTypeMime)) {
                webmBytes = AudioUtils.convertAudio(config.getFfmpegCommand(), webmBytes);
            }
            if (ArrayUtils.isEmpty(webmBytes)) {
                return null;
            }
            String format = ".webm";

            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS_")) + StringHelper.random(8) + "." + format;

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addPart("file", new ByteArrayBody(webmBytes, ContentType.create(webmContentTypeMime), filename));
            multipartEntityBuilder.addTextBody("model", "whisper-1", ContentType.TEXT_PLAIN);

            HttpEntity httpEntity = multipartEntityBuilder.build();

            request.setEntity(httpEntity);

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (log.isDebugEnabled()) {
                log.debug("{} - statusCode: {}, headers: {}", reqId, statusCode, response.getAllHeaders());
            }

            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            if (statusCode != HttpStatus.OK.value()) {
                if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                    throw new AiStatusException(content);
                }
                // 429
                if (content.contains(Constants.Regex.LIMIT)) {
                    throw new AiLimitException(content, apiKey);
                }
                AiAccountException exception = new AiAccountException(content, apiKey);
                exception.setSc(statusCode);
                throw exception;
            }

            EntityUtils.consume(entity);

            if (StringUtils.isNotBlank(content)) {
                return JsonHelper.convert(content, Res.class).getText();
            }

            return content;

        } catch (IOException e) {
            throw new AiNetworkException(e);
        } finally {
            request.releaseConnection();
        }
    }

    @Data
    public static class Res {
        private String text;
    }
}
