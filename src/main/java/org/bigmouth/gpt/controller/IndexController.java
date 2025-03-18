package org.bigmouth.gpt.controller;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.NotificationDto;
import org.bigmouth.gpt.entity.ProductVersion;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.request.KeepAliveRequest;
import org.bigmouth.gpt.entity.response.KeepAliveResponse;
import org.bigmouth.gpt.entity.response.NotificationVo;
import org.bigmouth.gpt.entity.response.VersionUpdateVo;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.IFriendService;
import org.bigmouth.gpt.service.INotificationService;
import org.bigmouth.gpt.service.IProductVersionService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author allen
 * @since 1.0.0
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    private final ApplicationConfig config;
    private final INotificationService notificationService;
    private final IProductVersionService productVersionService;
    private final IFriendService friendService;

    public IndexController(ApplicationConfig config, INotificationService notificationService, IProductVersionService productVersionService, IFriendService friendService) {
        this.config = config;
        this.notificationService = notificationService;
        this.productVersionService = productVersionService;
        this.friendService = friendService;
    }

    /**
     * @return
     * @see IndexController#keepAlive(KeepAliveRequest)
     */
    @Deprecated
    @GetMapping("/intro")
    public ResponseEntity<List<String>> intro() {
        return ResponseEntity.ok(config.getSystemIntro());
    }

    @GetMapping("/keep_alive")
    public ResponseEntity<KeepAliveResponse> keepAlive(@Validated KeepAliveRequest request) {
        User user = ContextFactory.getLoginUser();

        KeepAliveResponse response = new KeepAliveResponse();

        // 设置产品介绍词
        response.setIntro(config.getSystemIntro());

        // 查询置顶未读通知
        if (Objects.nonNull(user)) {
            NotificationDto one = notificationService.selectPinnedOne(user.getId());
            response.setNotification(NotificationVo.of(one));
        }

        // 查询IDE新版本
        ProductVersion latestIdeVersionByProduct = productVersionService.getLatestVersionByProduct(request.getIdeProductType());
        if (Objects.nonNull(latestIdeVersionByProduct)) {
            String standardVersion = request.getIdeVersionFixProductAdapt();
            String onlineVersion = latestIdeVersionByProduct.getVersion();
            boolean isNewVersion = isNewVersion(standardVersion, onlineVersion);
            if (isNewVersion) {
                response.setIdeVersion(new VersionUpdateVo(latestIdeVersionByProduct));
            }
        }
        // 查询Web新版本
        ProductVersion latestWebVersionByProduct = productVersionService.getLatestVersionByProduct(request.getWebProductType());
        if (Objects.nonNull(latestWebVersionByProduct)) {
            String standardVersion = request.getWebVersionFixProductAdapt();
            String onlineVersion = latestWebVersionByProduct.getVersion();
            boolean isNewVersion = isNewVersion(standardVersion, onlineVersion);
            if (isNewVersion) {
                response.setWebVersion(new VersionUpdateVo(latestWebVersionByProduct));
            }
        }

        // Friend Plaza
        Map<String, Integer> counted = friendService.countFriendTag();
        KeepAliveResponse.FriendPlaza friendPlaza = response.getFriendPlaza();
        List<String> tags = Constants.TAGS;
        List<KeepAliveResponse.Tag> newTags = Lists.newArrayList();
        for (String e : tags) {
            KeepAliveResponse.Tag tag = new KeepAliveResponse.Tag();
            Integer count = counted.getOrDefault(e, 0);
            tag.setName(e);
            tag.setCount(count);
            newTags.add(tag);
            friendPlaza.increaseBy(count);
        }
        friendPlaza.setNewTags(newTags);

        return ResponseEntity.ok(response);
    }

    public static boolean isNewVersion(String standardVersion, String onlineVersion) {
        if (StringUtils.isEmpty(onlineVersion)) {
            return Boolean.FALSE;
        }
        if (StringUtils.isEmpty(standardVersion)) {
            return Boolean.TRUE;
        }
        //过滤非数字和非字符点，并分割版本号数字段
        String[] standardArray = standardVersion.replaceAll("[^0-9.]", "").split("[.]");
        String[] onlineArray = onlineVersion.replaceAll("[^0-9.]", "").split("[.]");
        //获取数字段最小长度
        int length = Math.min(standardArray.length, onlineArray.length);
        for (int i = 0; i < length; i++) {
            if (StringUtils.isEmpty(onlineArray[i])) {
                return Boolean.FALSE;
            }
            if (StringUtils.isEmpty(standardArray[i])) {
                return Boolean.TRUE;
            }

            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(standardArray[i])) {
                return Boolean.TRUE;
            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(standardArray[i])) {
                return Boolean.FALSE;
            }
        }
        return onlineArray.length > standardArray.length;
    }
}
