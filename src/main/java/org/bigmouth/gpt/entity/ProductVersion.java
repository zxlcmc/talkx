package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品的版本管理
 * </p>
 *
 * @author allen
 * @since 2023-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_version")
public class ProductVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 版本状态。0 已废弃，1 正常
     */
    private Integer status;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String VERSION = "version";

    public static final String REQUIRED = "required";

    public static final String STATUS = "status";

    public static final String RELEASE_TIME = "release_time";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
