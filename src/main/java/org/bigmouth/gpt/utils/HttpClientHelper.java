package org.bigmouth.gpt.utils;

import com.bxm.warcar.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.bigmouth.gpt.ai.entity.OpenApiRequest;
import org.bigmouth.gpt.exceptions.AiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author huxiao
 * @date 2023/10/10
 * @since 1.0.0
 */
@Slf4j
public class HttpClientHelper {

    private HttpClientHelper() {}

    public static HttpClient createHttpClient(int maxTotal, int defaultMaxPerRoute, int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
        return createHttpClient(maxTotal, defaultMaxPerRoute, connectionRequestTimeout, connectTimeout, socketTimeout, null);
    }

    public static HttpClient createHttpClient(int maxTotal, int defaultMaxPerRoute, int connectionRequestTimeout, int connectTimeout, int socketTimeout, HttpHost proxy) {
        PoolingHttpClientConnectionManager connManager = createConnectionManager(maxTotal, defaultMaxPerRoute);
        return createHttpClient(connectionRequestTimeout, connectTimeout, socketTimeout, connManager, proxy);
    }

    public static CloseableHttpClient createHttpClient(int connectionRequestTimeout, int connectTimeout, int socketTimeout,
                                                        PoolingHttpClientConnectionManager connManager) {
        return createHttpClient(connectionRequestTimeout, connectTimeout, socketTimeout, connManager, null);
    }

    public static CloseableHttpClient createHttpClient(int connectionRequestTimeout, int connectTimeout, int socketTimeout,
                                                        PoolingHttpClientConnectionManager connManager, HttpHost proxy) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setRedirectsEnabled(false)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setExpectContinueEnabled(false)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setProxy(proxy)
                .build();

        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static PoolingHttpClientConnectionManager createConnectionManager(int maxTotal, int defaultMaxPerRoute) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return connManager;
    }

    public static String get(HttpClient client, String url) {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("", e);
            return null;
        } finally {
            get.releaseConnection();
        }
    }
    public static byte[] getBytes(HttpClient client, String url) {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            log.error("", e);
            return null;
        } finally {
            get.releaseConnection();
        }
    }

    public static String chat(HttpClient client, String url, OpenApiRequest openApiRequest,
                              String accessKey, String organizationId) throws AiException {
        try {
            HttpPost request = new HttpPost(url);
            request.setEntity(new StringEntity(JsonHelper.convert(openApiRequest), StandardCharsets.UTF_8));
            request.addHeader("Accept", "text/event-stream");
            request.addHeader("Content-Type", "application/json;charset=utf-8");
            request.addHeader("Authorization", "Bearer " + accessKey);
            request.addHeader("User-Agent", "TalkX-HttpClient/4.5.13");
            if (StringUtils.isNotBlank(organizationId)) {
                request.addHeader("OpenAI-Organization", organizationId);
            }
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (statusCode != HttpStatus.SC_OK) {
                throw new AiException("statusCode: " + statusCode + ", body=" + body);
            }
            return body;
        } catch (IOException e) {
            throw new AiException(e);
        }
    }
}
