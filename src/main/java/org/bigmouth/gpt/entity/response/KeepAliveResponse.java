package org.bigmouth.gpt.entity.response;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static org.bigmouth.gpt.utils.Constants.TAGS;

/**
 * @author huxiao
 * @date 2023/10/23
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class KeepAliveResponse {

    private List<String> intro;
    private NotificationVo notification;
    private VersionUpdateVo ideVersion;
    private VersionUpdateVo webVersion;
    private Integer gray = 0;
    private FriendPlaza friendPlaza = new FriendPlaza();
    private Plugin plugin = new Plugin();

    @Data
    public static class FriendPlaza {
        @Deprecated
        private List<String> tags = TAGS;
        private List<Tag> newTags = Lists.newArrayList();
        private int totalFriend = 0;

        public void increaseBy(int count) {
            this.totalFriend += count;
        }
    }

    @Data
    public static class Plugin {
        private String avatar = "https://plugin-web.talkx.cn/images/friend/avatar/3.png";
        private String welcome = "欢迎使用编程助手！我可以帮您解决编程方面的疑问、提供代码编写支持、代码优化建议，分享学习新技术的经验。请问您遇到什么问题了？";
    }

    @Data
    public static class Tag {
        private String name;
        private int count;
    }
}
