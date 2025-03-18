package org.bigmouth.gpt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2024/1/18
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class AttachVo {
    private String name;
    private String mimeType;
    private int size;
    private String url;

    public static AttachVo of(Attachment e) {
        return new AttachVo()
                .setName(e.getName())
                .setMimeType(e.getMimeType())
                .setSize(e.getSize())
                .setUrl(e.getUrl())
                ;
    }
}
