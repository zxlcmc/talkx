package org.bigmouth.gpt.entity.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author tangxiao
 * @date 2023/7/24
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class ModelResponse {

    private String model;
    /**
     * 对话一次消耗的蒜粒
     */
    private BigDecimal costCoin;
    /**
     * 模型的展示图标
     */
    private String icon;
    private String inputCoins;
    private String outputCoins;

    private boolean canSelection = true;
    private boolean allowUpload;
    private boolean supportTool;

    /**
     * 提示标签
     */
    private List<Tag> commentTags;

    public ModelResponse setCommentTags(String commentTags) {
        List<Tag> tags = Optional.ofNullable(commentTags)
                .map(s -> JSONObject.parseArray(s, Tag.class)).orElse(null);
        this.commentTags = tags;
        return this;
    }

    public ModelResponse setInputCoins(BigDecimal inputCoins) {
        this.inputCoins = Optional.ofNullable(inputCoins)
                .orElse(BigDecimal.ZERO)
                .stripTrailingZeros()
                .toPlainString();
        return this;
    }

    public ModelResponse setOutputCoins(BigDecimal outputCoins) {
        this.outputCoins = Optional.ofNullable(outputCoins)
                .orElse(BigDecimal.ZERO)
                .stripTrailingZeros()
                .toPlainString();
        return this;
    }

    @Data
    public static class Tag {
        private String name;
        private String bgColor;
        private String color;
    }
}
