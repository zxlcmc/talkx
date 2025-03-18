package org.bigmouth.gpt.entity.response;

import lombok.Data;
import org.bigmouth.gpt.entity.Order;
import org.bigmouth.gpt.utils.Constants;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@Data
public class OrderVo {

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 商品名称
     */
    private String productName;

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
     * 状态：0- 待支付、1- 已支付、2- 已成交、3- 已关闭、4- 支付失败
     */
    private Integer status;

    /**
     * 支付方式：1.支付宝 2.微信
     */
    private String payType;

    /**
     * 订单查询页面地址二维码
     */
    private String orderPageQrcode;

    /**
     * 剩余支付时间，单位：秒
     */
    private Long timeoutForPay;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    public OrderVo() {
    }

    public OrderVo(Order e) {
        if (Objects.nonNull(e)) {
            this.orderId = e.getOrderId();
            this.userId = e.getUserId();
            this.productType = e.getProductType();
            this.productAmount = e.getProductAmount();
            this.totalPrice = e.getTotalPrice();
            this.status = e.getStatus();
            this.payType = e.getPayType();
            this.createTime = e.getCreateTime();
            this.modifyTime = e.getModifyTime();
        }
    }

    public String getProductName() {
        if (Objects.equals(productType, Constants.Order.PRODUCT_TYPE_COIN)) {
            return "蒜粒 " + getProductAmount();
        }
        return null;
    }

    public Long getTimeoutForPay() {
        LocalDateTime ct = getCreateTime();
        if (Objects.nonNull(ct)) {
            LocalDateTime timeout = ct.plusMinutes(30);
            this.timeoutForPay = Duration.between(LocalDateTime.now(), timeout).getSeconds();
        }
        return timeoutForPay;
    }
}
