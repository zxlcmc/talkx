package org.bigmouth.gpt.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.entity.AigcImages;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.entity.SessionMessage;
import org.bigmouth.gpt.service.IAigcImagesService;
import org.bigmouth.gpt.service.ISessionMessageService;
import org.bigmouth.gpt.service.ISessionService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huxiao
 * @date 2023/12/6
 * @since 1.0.0
 */
@Slf4j
public class FoundHistoryImageInitializer {

    private final ISessionService sessionService;
    private final ISessionMessageService sessionMessageService;
    private final IAigcImagesService aigcImagesService;

    public FoundHistoryImageInitializer(ISessionService sessionService, ISessionMessageService sessionMessageService, IAigcImagesService aigcImagesService) {
        this.sessionService = sessionService;
        this.sessionMessageService = sessionMessageService;
        this.aigcImagesService = aigcImagesService;
    }

    @PostConstruct
    public void schedule() {
        // 正则表达式模式
        String pattern = "(https?://[\\w.-]+(/\\S*)?)";

        // 创建 Pattern 对象
        Pattern regexPattern = Pattern.compile(pattern);

        LocalDateTime atBefore = LocalDateTime.of(2023, 12, 6, 14, 44);
        QueryWrapper<SessionMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(SessionMessage.CONTENT, "![Image]");
        queryWrapper.le(SessionMessage.CREATE_TIME, atBefore);
        queryWrapper.orderByAsc(SessionMessage.CREATE_TIME);
        List<SessionMessage> messages = sessionMessageService.list(queryWrapper);
        int size = messages.size();
        log.info("Found messages: {}", size);
        AtomicLong index = new AtomicLong(1);
        for (SessionMessage message : messages) {
            long andIncrement = index.getAndIncrement();
            String content = message.getContent();
            if (content.contains("plugin-web.talkx.cn")) {
                Session session = sessionService.getById(message.getSessionId());
                String sessionId = message.getSessionId();
                Long id = message.getId();
                SessionMessage one = findLatestMsg(sessionId, id);
                String userPrompt = Optional.ofNullable(one).map(SessionMessage::getContent).orElse(null);
                String revisedPrompt = null;
                String url = null;

                String[] strings = content.split("\n");
                Matcher matcher = regexPattern.matcher(strings[0]);
                if (matcher.find()) {
                    url = StringUtils.removeEnd(matcher.group(1), ")");
                }
                if (strings.length > 1) {
                    // Is ![Image] \n
                    revisedPrompt = strings[1].trim();
                }

                AigcImages images = new AigcImages()
                        .setId(andIncrement)
                        .setModel("dall-e-3")
                        .setUserId(session.getUserId())
                        .setUserPrompt(userPrompt)
                        .setImageUrl(url)
                        .setRevisedPrompt(revisedPrompt)
                        .setCreateTime(message.getCreateTime());
                log.info("{} - {}", andIncrement, images);

                aigcImagesService.save(images);
            }
        }
    }

    private SessionMessage findLatestMsg(String sessionId, Long id) {
        QueryWrapper<SessionMessage> subQuery = new QueryWrapper<>();
        subQuery.eq(SessionMessage.SESSION_ID, sessionId);
        subQuery.lt(SessionMessage.ID, id);
        subQuery.orderByDesc(SessionMessage.ID);
        IPage<SessionMessage> page = new Page<>();
        page.setSize(1);
        IPage<SessionMessage> messageIPage = sessionMessageService.page(page, subQuery);
        List<SessionMessage> records = messageIPage.getRecords();
        return CollectionUtils.isNotEmpty(records) ? records.get(0) : null;
    }
}
