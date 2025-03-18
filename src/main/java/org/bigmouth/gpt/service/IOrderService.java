package org.bigmouth.gpt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.Order;
import org.bigmouth.gpt.entity.PrepayDto;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author allen
 * @since 2023-11-03
 */
public interface IOrderService extends IService<Order> {

    /**
     * 查询订单
     * @param orderId 订单号
     * @return 可能null
     */
    Order query(String orderId);

    /**
     * 提交订单
     *
     * @param userId 用户
     * @param catalogId 下单的商品
     * @return 订单号
     */
    String createOrder(Long userId, Long catalogId) throws IllegalStateException;

    /**
     * 开始支付订单，生成支付需要的签名信息
     *
     * @param prepayDto 支付信息
     * @return 签名信息
     */
    Object prepay(PrepayDto prepayDto);

    /**
     * 订单支付成功，交货
     * @param orderId 订单号
     * @param paymentId 支付平台订单号
     */
    void delivery(String orderId, String paymentId);

    /**
     * 计算合计金额
     * @param queryWrapper 查询条件
     * @return 合计金额
     */
    BigDecimal sum(QueryWrapper<Order> queryWrapper);
}
