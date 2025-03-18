package com.bxm.warcar.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author allen
 * @date 2021-11-02
 * @since 1.0
 */
public class UrlHelper {

    public static String getFirstValueOfParamName(String url, String paramName) {
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUriString(url).build().getQueryParams();
        return params.getFirst(paramName);
    }

    public static String urlDecode(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    public static String urlEncode(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
