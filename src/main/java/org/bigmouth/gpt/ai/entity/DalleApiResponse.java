package org.bigmouth.gpt.ai.entity;

import lombok.Data;

import java.util.List;

/**
 * @author allen
 * @date 2023/5/25
 * @since 1.0.0
 */
@Data
public class DalleApiResponse {

    private Long created;
    private List<Image> data;

    @Data
    public static class Image {
        private String url;
        private String b64_json;
        private String revised_prompt;
    }
}
