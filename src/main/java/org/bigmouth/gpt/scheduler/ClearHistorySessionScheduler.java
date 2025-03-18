package org.bigmouth.gpt.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.service.ISessionService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * 自动删除历史消息
 *
 * @author Allen Hu
 * @date 2024/10/30
 */
@Slf4j
public class ClearHistorySessionScheduler {

    private final ApplicationConfig applicationConfig;
    private final ISessionService sessionService;

    public ClearHistorySessionScheduler(ApplicationConfig applicationConfig, ISessionService sessionService) {
        this.applicationConfig = applicationConfig;
        this.sessionService = sessionService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void schedule() {
        int clearHistorySessionBeforeDays = applicationConfig.getClearHistorySessionBeforeDays();

        IPage<Session> page = new Page<>(1, 100);
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(Math.abs(clearHistorySessionBeforeDays));
        log.info("Deleting sessions older than {}...", thirtyDaysAgo);

        QueryWrapper<Session> queryWrapper = new QueryWrapper<Session>()
                .lt(Session.CREATE_TIME, thirtyDaysAgo)
                .orderByAsc(Session.ID);

        boolean hasNextPage;
        do {
            sessionService.page(page, queryWrapper);

            for (Session session : page.getRecords()) {
                log.info("Deleting session: {} - {}", session.getCreateTime(), session.getId());
                sessionService.deleteWithMessages(session.getId());
            }
            hasNextPage = page.getPages() > page.getCurrent();
            page.setCurrent(page.getCurrent() + 1);
        } while (hasNextPage);
    }
}
