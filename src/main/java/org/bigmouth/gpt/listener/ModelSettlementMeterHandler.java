package org.bigmouth.gpt.listener;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import com.bxm.warcar.utils.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ai.entity.Usage;
import org.bigmouth.gpt.entity.AiModel;
import org.bigmouth.gpt.entity.Prompt;
import org.bigmouth.gpt.event.ChatCompletionEvent;
import org.bigmouth.gpt.utils.RedisKeys;
import org.bigmouth.gpt.utils.TikTokensUtils;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 聊天完成结算成本处理器。
 * TODO 如果是 dall-e-3 这类模型，计费是有问题的。1、不应该结算基础模型；2、没有按dall-e-3分组统计。
 * @author huxiao
 * @date 2023/11/17
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class ModelSettlementMeterHandler implements EventListener<ChatCompletionEvent> {
    private final Counter counter;
    private final Fetcher fetcher;
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("openai.one.work"));
    private final ConcurrentHashSet<String> registerModels = new ConcurrentHashSet<>();
    private final ConcurrentHashSet<String> newRegisterModels = new ConcurrentHashSet<>();

    public ModelSettlementMeterHandler(Counter counter, Fetcher fetcher) {
        this.counter = counter;
        this.fetcher = fetcher;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(ChatCompletionEvent event) {
        ChatCompletionEvent.Parameter parameter = event.getParameter();

        Prompt prompt = parameter.getPrompt();
        AiModel aiModel = parameter.getAiModel();
        Usage usage = parameter.getUsage();
        if (null == usage) {
            log.warn("Usage is Empty!");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        // 计算Input
        BigDecimal inputPrice = aiModel.getInputPrice();
        if (isGreaterThanZero(inputPrice)) {
            int requestTokens = Optional.ofNullable(usage).map(Usage::getPromptTokens).orElse(0);
            BigDecimal inputCost = TikTokensUtils.computePrice(requestTokens, inputPrice);
            totalPrice = totalPrice.add(inputCost);
        }

        // 计算Output
        BigDecimal outPrice = aiModel.getOutPrice();
        if (isGreaterThanZero(outPrice)) {
            int completionTokens = (null != usage) ? usage.getCompletionTokens() : TikTokensUtils.tokens(aiModel.getModelName(), parameter.getCompletion());
            BigDecimal outputCost = TikTokensUtils.computePrice(completionTokens, outPrice);
            totalPrice = totalPrice.add(outputCost);
        }

        // 计算角色消耗
        BigDecimal promptCost = prompt.getCoinCostPer();
        if (isGreaterThanZero(promptCost)) {
            totalPrice = totalPrice.add(promptCost);
        }

        String field = aiModel.getModelName();
        double value = totalPrice.setScale(8, RoundingMode.HALF_UP).doubleValue();
        int expireTimeInSecond = (int) Duration.ofDays(30).getSeconds();
        counter.hincrFloatByAndGet(RedisKeys.hashPricingOfDate(), field, value, expireTimeInSecond);

        newRegisterModels.add(field);
    }

    private static boolean isGreaterThanZero(BigDecimal value) {
        if (Objects.isNull(value)) {
            return false;
        }
        return value.compareTo(BigDecimal.ZERO) > 0;
    }
}
