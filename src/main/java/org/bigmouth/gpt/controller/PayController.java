package org.bigmouth.gpt.controller;

import org.bigmouth.gpt.service.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    private final IOrderService orderService;

    public PayController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/notify")
    public ResponseEntity<Object> notify(String orderNum, String tradeNum) {
        orderService.delivery(orderNum, tradeNum);
        return ResponseEntity.ok().build();
    }
}
