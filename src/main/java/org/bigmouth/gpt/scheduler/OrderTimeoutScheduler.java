package org.bigmouth.gpt.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.Order;
import org.bigmouth.gpt.service.IOrderService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class OrderTimeoutScheduler {

    private final IOrderService orderService;

    public OrderTimeoutScheduler(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void schedule() {
        // 查询所有待支付、支付中的订单
        try {
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Order.STATUS, Constants.Order.WAIT_PAY);

            LocalDateTime now = LocalDateTime.now();
            List<Order> orders = orderService.list(queryWrapper);
            for (Order order : orders) {
                LocalDateTime createTime = order.getCreateTime();
                if (now.isAfter(createTime.plusMinutes(30))) {
                    // 超过30分钟
                    order.setStatus(Constants.Order.CLOSED)
                            .setModifyTime(now);
                    orderService.updateById(order);
                    log.info("{} 订单超时", order.getOrderId());
                }
            }
        } catch (Exception e) {
            log.error("schedule: ", e);
        }
    }
}
