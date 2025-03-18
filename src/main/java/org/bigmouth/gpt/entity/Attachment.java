package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author allen
 * @since 2024-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件路径
     */
    private String url;


    public static final String ID = "id";

    public static final String MIME_TYPE = "mime_type";

    public static final String NAME = "name";

    public static final String SIZE = "size";

    public static final String URL = "url";

}
