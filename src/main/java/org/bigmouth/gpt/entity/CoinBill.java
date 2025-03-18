package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 蒜粒账单
 * </p>
 *
 * @author allen
 * @since 2023-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coin_bill")
public class CoinBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 蒜粒账单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账单类型。1 消耗，2 充值，3 奖励
     */
    private Integer type;

    /**
     * 账单蒜粒数量，消耗时是负数
     */
    private BigDecimal value;

    /**
     * 账单标题。比如：使用的模型、奖励的类型
     */
    private String billTitle;

    /**
     * 账单说明或描述。
     */
    private String billDesc;

    /**
     * 账单后的最新蒜粒数量
     */
    private BigDecimal coin;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String TYPE = "type";

    public static final String VALUE = "value";

    public static final String BILL_TITLE = "bill_title";
    public static final String BILL_DESC = "bill_desc";

    public static final String COIN = "coin";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
