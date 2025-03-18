package org.bigmouth.gpt.entity.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author huxiao
 * @date 2023/10/23
 * @since 1.0.0
 */
@Data
public class KeepAliveRequest {

    private Integer ideProductType;
    private String ideVersion;
    private Integer webProductType;
    private String webVersion;

    /**
     * 截取 - 之前的版本号。
     * @return 0.0.1-212-233，最终返回 0.0.1
     */
    public String getIdeVersionFixProductAdapt() {
        return Optional.ofNullable(ideVersion)
                .filter(StringUtils::isNotBlank)
                .map(e -> StringUtils.split(e, "-")[0])
                .orElse(null);
    }

    /**
     * 截取 - 之前的版本号。
     * @return 0.0.1-212-233，最终返回 0.0.1
     */
    public String getWebVersionFixProductAdapt() {
        return Optional.ofNullable(webVersion)
                .filter(StringUtils::isNotBlank)
                .map(e -> StringUtils.split(e, "-")[0])
                .orElse(null);
    }
}
