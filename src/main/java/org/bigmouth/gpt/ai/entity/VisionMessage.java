package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author allen
 * @date 2023-04-20
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class VisionMessage {

    private String role;
    private String name;
    private List<? extends Type> content;

    @Data
    public abstract static class Type {
        public static final String TYPE_TEXT = "text";
        public static final String TYPE_IMAGE_URL = "image_url";
        private String type;

        @JSONField(deserialize = false, serialize = false)
        public boolean isText() {
            return StringUtils.equals(TYPE_TEXT, type);
        }

        @JSONField(deserialize = false, serialize = false)
        public boolean isImageUrl() {
            return StringUtils.equals(TYPE_IMAGE_URL, type);
        }
    }

    @Data
    public static class Text extends Type {
        private String text;
    }

    @Data
    public static class ImageUrl extends Type {
        private UrlOnImageUrl image_url;
    }

    @Data
    public static class UrlOnImageUrl {
        public static final String AUTO = "auto";
        public static final String LOW = "low";
        public static final String HIGH = "high";
        private String url;
        private String detail = LOW;
    }
}
