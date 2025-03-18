package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bxm.warcar.utils.UUIDHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.AiModel;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.request.UserUpdateRequest;
import org.bigmouth.gpt.entity.response.ModelResponse;
import org.bigmouth.gpt.entity.response.UserInfoResponse;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.IAiModelService;
import org.bigmouth.gpt.service.IUserInfoService;
import org.bigmouth.gpt.service.IUserService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tangxiao
 * @date 2023/7/24
 * @since 1.0
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    private final IUserService userService;
    private final IUserInfoService userInfoService;
    private final IAiModelService aiModelService;
    private final ApplicationConfig applicationConfig;

    public UserInfoController(IUserService userService, IUserInfoService userInfoService, IAiModelService aiModelService, ApplicationConfig applicationConfig) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.aiModelService = aiModelService;
        this.applicationConfig = applicationConfig;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Void> update(@Validated @RequestBody UserUpdateRequest request) {
        userInfoService.update(ContextFactory.getLoginUser(), request);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<UserInfoResponse> get() {
        User user = ContextFactory.getLoginUser();
        String websocketApiKey = user.getWebsocketApiKey();
        if (StringUtils.isBlank(websocketApiKey)) {
            websocketApiKey = "wbsk-" + UUIDHelper.generate();
            UpdateWrapper<User> updateWrapper = Wrappers.update();
            updateWrapper.set(User.WEBSOCKET_API_KEY, websocketApiKey);
            updateWrapper.eq(User.ID, user.getId());
            userService.update(updateWrapper);
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse()
                .setId(user.getId())
                .setName(user.getName())
                .setPhoneNum(user.getPhoneNum())
                .setEmail(user.getEmail())
                .setModel(user.getModel())
                .setProxyBaseUrl(user.getProxyBaseUrl())
                .setApiKey(user.getApiKey())
                .setAvatar(user.getAvatar())
                .setInviteCode(user.getInviteCode())
                .setCoins(user.getCoin())
                .setWebsocketApiKey(websocketApiKey);
        AiModel aiModel = aiModelService.getOne(Wrappers.query(new AiModel().setModelName(user.getModel())));
        if (Objects.nonNull(aiModel)) {
            userInfoResponse.setModelIcon(aiModel.getIcon());
            boolean allowUpload = Objects.equals(aiModel.getIsAllowUpload(), Constants.YES);
            userInfoResponse.setAllowUpload(allowUpload);
        }
        String inviteUrl = UriComponentsBuilder.fromUriString(applicationConfig.getTalkxHost())
                .fragment("/?inviteCode=" + user.getInviteCode())
                .build()
                .toString();
        userInfoResponse.setInviteUrl(inviteUrl);
        return ResponseEntity.ok(userInfoResponse);
    }

    @RequestMapping(value = "/list_model", method = RequestMethod.GET)
    public ResponseEntity<List<ModelResponse>> listModel() {
        List<AiModel> aiModels = aiModelService.getValidityList();
        List<ModelResponse> modelResponses = aiModels.stream().map((i) -> {
            boolean allowSelection = Objects.equals(i.getCanSelection(), Constants.YES);
            boolean allowUpload = Objects.equals(i.getIsAllowUpload(), Constants.YES);
            boolean supportTool = Objects.equals(i.getIsSupportTool(), Constants.YES);
            return new ModelResponse()
                    .setModel(i.getModelName())
                    .setCostCoin(i.getCoinCostPer())
                    .setIcon(i.getIcon())
                    .setInputCoins(i.getInputCoins())
                    .setOutputCoins(i.getOutputCoins())
                    .setCanSelection(allowSelection)
                    .setAllowUpload(allowUpload)
                    .setSupportTool(supportTool)
                    .setCommentTags(i.getCommentTags());
        }).collect(Collectors.toList());
        User user = ContextFactory.getLoginUser();
        if (Objects.nonNull(user)) {
            List<ModelResponse> myModelResponse = aiModelService.getMyModelResponse(user.getId());
            if (!CollectionUtils.isEmpty(myModelResponse)) {
                modelResponses.addAll(myModelResponse);
            }
        }
        return ResponseEntity.ok(modelResponses);
    }

    @RequestMapping(value = "/check_phone", method = RequestMethod.POST)
    public ResponseEntity<Void> checkPhoneNum(@Validated @RequestBody CheckPhoneRequest request) {
        boolean isUsed = userService.count(Wrappers.query(new User().setPhoneNum(request.getPhoneNum()))) > 0;
        if (isUsed) {
            throw new IllegalStateException("手机号已被使用");
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CheckPhoneRequest {
        @NotBlank
        private String phoneNum;
    }

}
