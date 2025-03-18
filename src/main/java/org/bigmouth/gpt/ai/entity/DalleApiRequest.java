package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.bxm.warcar.utils.TypeHelper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @date 2023/5/25
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class DalleApiRequest {

    public static final String STYLE_VIVID = "vivid";
    public static final String STYLE_NATURAL = "natural";
    public static final String QUALITY_STANDARD = "standard";
    public static final String QUALITY_HD = "hd";

    /**
     * model
     */
    private String model;

    /**
     * A text description of the desired image(s). The maximum length is 1000 characters for dall-e-2 and 4000 characters for dall-e-3.
     */
    private String prompt;

    /**
     * The number of images to generate. Must be between 1 and 10.
     */
    private int n = 1;

    /**
     * The quality of the image that will be generated. hd creates images with finer details
     * and greater consistency across the image. This param is only supported for dall-e-3.
     */
    private String quality = QUALITY_STANDARD;

    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024 for dall-e-2.
     * Must be one of 1024x1024, 1792x1024, or 1024x1792 for dall-e-3 models.
     */
    private String size = "1024x1024";

    /**
     * The style of the generated images. Must be one of vivid or natural.
     * Vivid causes the model to lean towards generating hyper-real and dramatic images.
     * Natural causes the model to produce more natural, less hyper-real looking images.
     * This param is only supported for dall-e-3.
     */
    private String style = STYLE_VIVID;

    /**
     * The format in which the generated images are returned. Must be one of url or b64_json.
     */
    @JSONField(name = "response_format")
    private String responseFormat = "url";

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse. Learn more.
     */
    private String user;

    @JSONField(deserialize = false, serialize = false)
    public Integer getWidth() {
        return TypeHelper.castToInt(StringUtils.split(size, "x")[0]);
    }

    @JSONField(deserialize = false, serialize = false)
    public Integer getHeight() {
        String[] xes = StringUtils.split(size, "x");
        if (xes.length == 1) {
            return null;
        }
        return TypeHelper.castToInt(xes[1]);
    }
}
