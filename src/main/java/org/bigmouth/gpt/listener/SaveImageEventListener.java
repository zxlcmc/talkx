package org.bigmouth.gpt.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import org.bigmouth.gpt.ai.entity.DalleApiRequest;
import org.bigmouth.gpt.entity.AigcImages;
import org.bigmouth.gpt.event.CreatedImageEvent;
import org.bigmouth.gpt.service.IAigcImagesService;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/12/6
 * @since 1.0.0
 */
@Configuration
public class SaveImageEventListener implements EventListener<CreatedImageEvent> {

    private final IAigcImagesService aigcImagesService;

    public SaveImageEventListener(IAigcImagesService aigcImagesService) {
        this.aigcImagesService = aigcImagesService;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(CreatedImageEvent event) {
        DalleApiRequest request = event.getDalleApiRequest();
        AigcImages images = new AigcImages()
                .setModel(request.getModel())
                .setUserId(event.getUserId())
                .setImageUrl(event.getUrl())
                .setUserPrompt(request.getPrompt())
                .setRevisedPrompt(event.getRevisedPrompt())
                .setWidth(request.getWidth())
                .setHeight(request.getHeight());
        aigcImagesService.save(images);
    }
}
