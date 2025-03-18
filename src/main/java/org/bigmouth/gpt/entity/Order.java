package org.bigmouth.gpt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 商品类型：1.蒜粒
     */
    private Integer productType;

    /**
     * 商品数量
     */
    private BigDecimal productAmount;

    /**
     * 订单金额
     */
    private BigDecimal totalPrice;

    /**
     * 0- 待支付、1- 支付中、2- 支付成功、3- 已关闭、4- 支付失败
     */
    private Integer status;

    /**
     * 支付方式：1.支付宝 2.微信
     */
    private String payType;

    /**
     * 支付平台订单号
     */
    private String paymentId;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String ORDER_ID = "order_id";

    public static final String USER_ID = "user_id";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String PRODUCT_AMOUNT = "product_amount";

    public static final String TOTAL_PRICE = "total_price";

    public static final String STATUS = "status";

    public static final String PAY_TYPE = "pay_type";

    public static final String PAYMENT_ID = "payment_id";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
