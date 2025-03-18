package org.bigmouth.gpt.integration.wechat;

import com.alibaba.fastjson.JSONObject;
import com.bxm.warcar.cache.Fetcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.bigmouth.gpt.utils.HttpClientHelper;
import org.bigmouth.gpt.utils.RedisKeys;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author allen
 */
@Configuration
@Slf4j
public class WechatAuthIntegration extends WeixinSupport {

    private final WeChatConfig wechatConfig;
    private final HttpClient httpClient;
    private final Fetcher fetcher;

    public WechatAuthIntegration(WeChatConfig wechatConfig, Fetcher fetcher) {
        this.wechatConfig = wechatConfig;
        this.fetcher = fetcher;
        this.httpClient = HttpClientHelper.createHttpClient(10, 10, 5000, 5000, 10000);
    }

    /**
     * 获取微信用户OpenId
     *
     * @param code
     * @return
     * @throws WeChatException
     */
    public String getOpenId(String code) throws WeChatException {
        return fetcher.fetch(RedisKeys.WeChat.stringAccessToken(code), () -> {
            try {
                SnsAccessToken snsOAuth2AccessToken = getSnsOAuth2AccessToken(code);
                return snsOAuth2AccessToken.getOpenid();
            } catch (WeChatException e) {
                log.error("", e);
                return null;
            }
        }, String.class, 72 * 60 * 60);
    }

    /**
     * 获取网页授权AccessToken
     *
     * @param code
     * @return
     * @throws WeChatException
     */
    public SnsAccessToken getSnsOAuth2AccessToken(String code) throws WeChatException {
        try {
            if (StringUtils.isEmpty(code)) {
                throw new IllegalArgumentException("code can't be null or empty");
            }
            //拼接参数
            String param = "?appid=" + wechatConfig.getAppId() + "&secret=" + wechatConfig.getSecret() + "&code=" + code + "&grant_type=authorization_code";
            //创建请求对象
            //调用获取access_token接口
            HttpResponse response = httpClient.execute(new HttpGet(wechatConfig.getAccessTokenUrl() + param));

            String jsonStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            //根据请求结果判定，是否验证成功
            JSONObject jsonObj = JSONObject.parseObject(jsonStr);
            if (jsonObj == null) {
                return null;
            }

            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //返回异常信息
                throw new WeChatException(getCause(jsonObj.getIntValue("errcode")));
            }
            return new SnsAccessToken(jsonObj);
        } catch (IOException e) {
            throw new WeChatException(e);
        }
    }

    public WeChatConfig getWechatConfig() {
        return wechatConfig;
    }
}
