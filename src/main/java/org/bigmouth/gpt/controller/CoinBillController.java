package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bxm.warcar.utils.TypeHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.CoinBill;
import org.bigmouth.gpt.entity.PageRequest;
import org.bigmouth.gpt.entity.PageVo;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.CoinService;
import org.bigmouth.gpt.service.ICoinBillService;
import org.bigmouth.gpt.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/coin_bill")
public class CoinBillController {
    private final ICoinBillService coinBillService;
    private final IUserService userService;
    private final CoinService coinService;
    public CoinBillController(ICoinBillService coinBillService, IUserService userService, CoinService coinService) {
        this.coinBillService = coinBillService;
        this.userService = userService;
        this.coinService = coinService;
    }

    @GetMapping("/search")
    public ResponseEntity<PageVo<SearchResponse>> search(@Validated SearchRequest searchRequest) {
        User user = ContextFactory.getLoginUser();

        CoinBill query = new CoinBill();
        query.setUserId(user.getId());
        if (Objects.nonNull(searchRequest.getType())) {
            query.setType(searchRequest.getType());
        }
        Page<CoinBill> coinBillPage = new Page<>();
        coinBillPage.setSize(searchRequest.getSize()).setCurrent(searchRequest.getCurrent());
        IPage<CoinBill> result = coinBillService.page(coinBillPage, Wrappers.query(query).orderByDesc(CoinBill.ID));
        PageVo<SearchResponse> vo = PageVo.of(result, SearchResponse::new);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/add_bill_history")
    public ResponseEntity<Void> addBillHistory(@RequestBody @Validated DefinedPlusRequest request) {
        Set<String> userIdsSet = request.getUserIds();
        BigDecimal value = request.getValue();
        String title = request.getTitle();
        Integer type = TypeHelper.castToInt(request.getType());
        for (String userId : userIdsSet) {
            Long uid = TypeHelper.castToLong(userId);
            if (coinService.plus(uid, type, value, title)) {
                log.info("{} Add bill history: type={}, amount={}, title={}", uid, type, value, title);
            }
            if (request.isSendSms()) {
                String smsContent = request.getSmsContent();
                userService.sendSms(uid, smsContent);
            }
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class DefinedPlusRequest {

        @NotBlank(message = "类型不能为空")
        @Pattern(regexp = "1|2|3")
        private String type;
        @NotBlank(message = "发放奖励的标题不能为空") private String title;
        @NotNull(message = "用户ID不能为空") private Set<String> userIds;
        @NotNull(message = "奖励的数量不能为空") private BigDecimal value;
        /**
         * 是否发送短信通知
         */
        private boolean sendSms = false;
        private String smsContent;
    }

    @Data
    public static class SearchRequest extends PageRequest {
        private Integer type;
    }

    @Data
    public static class SearchResponse {
        /**
         * 蒜粒账单ID
         */
        private Long id;

        /**
         * 账单类型。1 消耗，2 充值，3 奖励
         */
        private Integer type;

        /**
         * 账单蒜粒数量，消耗时是负数
         */
        private String value;

        /**
         * 账单标题。比如：使用的模型、奖励的类型
         */
        private String billTitle;

        private String billDesc;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        public SearchResponse(CoinBill coinBill) {
            this.id = coinBill.getId();
            this.type = coinBill.getType();
            this.value = Optional.ofNullable(coinBill.getValue()).orElse(BigDecimal.ZERO).stripTrailingZeros().toPlainString();
            this.billTitle = coinBill.getBillTitle();
            this.billDesc = coinBill.getBillDesc();
            this.createTime = coinBill.getCreateTime();
        }
    }
}
