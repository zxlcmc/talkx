package org.bigmouth.gpt.controller;


import org.bigmouth.gpt.entity.UserFeedback;
import org.bigmouth.gpt.entity.request.FeedbackRequest;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.IUserFeedbackService;
import org.bigmouth.gpt.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户反馈 前端控制器
 * </p>
 *
 * @author tangxiao
 * @since 2023-07-21
 */
@RestController
@RequestMapping("/feedback")
public class UserFeedbackController {

    private final IUserFeedbackService userFeedbackService;

    private final IUserService userService;

    public UserFeedbackController(IUserFeedbackService userFeedbackService, IUserService userService) {
        this.userFeedbackService = userFeedbackService;
        this.userService = userService;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseEntity<Object> submit(@RequestBody @Validated FeedbackRequest request) {
        Long userId = ContextFactory.getLoginUser().getId();
        UserFeedback userFeedback = new UserFeedback()
                .setUserId(userId)
                .setImage(request.getImage())
                .setContent(request.getContent())
                .setContactPhone(request.getContactPhone());
        userFeedbackService.save(userFeedback);
        return ResponseEntity.ok().build();
    }

}
