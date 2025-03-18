package org.bigmouth.gpt.entity.response;

import lombok.Data;
import org.bigmouth.gpt.entity.ProductVersion;

import java.time.LocalDateTime;

/**
 * @author huxiao
 * @date 2023/10/23
 * @since 1.0.0
 */
@Data
public class VersionUpdateVo {

    /**
     * 版本ID
     */
    private Long id;

    /**
     * 产品类型。1 Web、2 JetBrains、3 VS Code、4 HBuilderX
     */
    private Integer productType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否强烈建议升级到这个版本
     */
    private Integer required;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    public VersionUpdateVo(ProductVersion e) {
        if (null == e) {
            return;
        }
        this.id = e.getId();
        this.productType = e.getProductType();
        this.version = e.getVersion();
        this.required = e.getRequired();
        this.releaseTime = e.getReleaseTime();
    }
}
