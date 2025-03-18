package org.bigmouth.gpt.facade;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>类的基本描述</h3>
 *
 * @author hcmony
 * @since V1.0.0, 2019/10/15 11:13
 */
@Data
@Accessors(chain = true)
public class PayOrder implements Serializable {
    private static final long serialVersionUID = 5898421344661777984L;
    /**
     * 状态：-1未找到，0未支付，1已支付,3已关闭,已支付待收款，5已完成,6退款成功,7退款已申请,8退款异常
     */
    public static final int STATUS_PRE_NULL = -1 ;
    public static final int STATUS_PRE_PAY = 0 ;
    public static final int STATUS_SUCCESS_PAY = 1 ;
    public static final int STATUS_CLOSE_PAY = 3 ;
    public static final int STATUS_PENDING_PAY = 4 ;
    public static final int STATUS_FINISHED_PAY = 5 ;
    public static final int STATUS_REFUND_PAY = 6;
    public static final int STATUE_REFUND_APPLY = 7;
    public static final int STATUE_REFUND_ERROR = 8;

    /**
     * 客户端类型，0：h5，1：app，2：微信，3：QQ, 4:PC , 5:支付宝
     */
    public static final int CLIENT_TYPE_H5 = 0 ;
    public static final int CLIENT_TYPE_APP = 1 ;
    public static final int CLIENT_TYPE_WECHAT = 2 ;
    public static final int CLIENT_TYPE_QQ = 3 ;
    public static final int CLIENT_TYPE_PC = 4 ;
    public static final int CLIENT_TYPE_ALI = 5;

    /**
     * 支付渠道 1支付宝支付，2微信支付
     */
    public static final int PAY_TYPE_ALI = 1 ;
    public static final int PAY_TYPE_WECHAT = 2 ;

    /**
     * 订单号 调用业务生成
     */
    private String orderNum;

    /**
     * 订单id 支付项目生成
     */
    private String orderId;

    /**
     * 交易号 支付商交易号
     */
    private String tradeNum;

    /**
     * 金额（单位：元）
     */
    private BigDecimal amount;

    /**
     * 支付渠道 1支付宝支付，2微信支付
     */
    private int payType;

    /**
     * 状态：0未支付，1已完成,3已关闭
     */
    private Integer status;

    /**
     * 业务 88 TalkX
     */
    private Integer business = 88;

    /**
     * 主题 如:广告-互动业务
     */
    private String subject;

    /**
     *微信id
     */
    private String openId;

    /**
     * 说明
     */
    private String body;

    /**
     * 通知地址
     */
    private String notifyUrl;

    private Integer payConfigId;

    /**
     * 客户端类型，0：h5，1：app，2：微信，3：QQ, 4:PC
     */
    private Integer clientType;

    /**
     * 支付客户端ip
     */
    private String clientIp;

    private String requestHost;
}
