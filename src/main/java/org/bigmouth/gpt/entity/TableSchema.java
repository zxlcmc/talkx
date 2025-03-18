package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("table_schema")
public class TableSchema implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 表结构名称
     */
    private String name;

    /**
     * SQL语言类型
     */
    private String sqlType;

    /**
     * 表结构信息
     */
    @TableField("`schema`")
    private String schema;

    /**
     * 快速开始
     */
    private String conversactionStart;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    /**
     * 删除标识
     */
    private Integer deleted;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String NAME = "name";

    public static final String SQL_TYPE = "sql_type";

    public static final String SCHEMA = "schema";

    public static final String CONVERSACTION_START = "conversaction_start";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

}
