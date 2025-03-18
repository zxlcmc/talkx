package org.bigmouth.gpt.utils;

import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.utils.DateHelper;
import com.bxm.warcar.utils.KeyBuilder;
import org.bigmouth.gpt.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author tangxiao
 * @date 2023/7/25
 * @since 1.0
 */
public class RedisKeys {

    public static KeyGenerator stringVerifyCodeCache(String phoneNum) {
        return () -> KeyBuilder.build("talkx", "user", "verify_code", phoneNum);
    }

    public static KeyGenerator stringUserTokenCache(String token) {
        return () -> KeyBuilder.build("talkx", "user", "token", token);
    }

    public static KeyGenerator stringCodePassCache(String phoneNum) {
        return () -> KeyBuilder.build("talkx", "user", "code_pass", phoneNum);
    }

    public static KeyGenerator stringGuestChatCount(String md5IpUa) {
        return () -> KeyBuilder.build("talkx", "guest", "chat_count", getDate(), md5IpUa);
    }

    public static KeyGenerator hashPricingOfDate() {
        return () -> KeyBuilder.build("talkx", "pricing", getDate());
    }

    public static KeyGenerator stringIpLimitPerMinute(String ip) {
        return () -> KeyBuilder.build("talkx", "limit", "ip", DateHelper.format("yyyyMMddHHmm"), ip);
    }

    public static KeyGenerator setAccessKeys() {
        return () -> KeyBuilder.build("talkx", "access_keys", "available");
    }

    public static KeyGenerator setAccessKeysLimitDay() {
        return () -> KeyBuilder.build("talkx", "access_keys", "isolation");
    }

    public static KeyGenerator stringAccessKeyHourReqCount(String apiKey) {
        return () -> KeyBuilder.build("talkx", "access_key", "hour_req", apiKey);
    }

    public static KeyGenerator stringAccessKeyMinuteReqCount(String apiKey) {
        return () -> KeyBuilder.build("talkx", "access_key", "minute_req", apiKey);
    }

    public static KeyGenerator setInviteCode() {
        return () -> KeyBuilder.build("talkx", "invite_code");
    }

    public static KeyGenerator hashCoinCatalog() {
        return () -> KeyBuilder.build("talkx", "coin_catalog");
    }

    public static KeyGenerator hashInternalServerError() {
        return () -> KeyBuilder.build("talkx", "internal_server_error");
    }

    public static class Cache {

        public static KeyGenerator stringAiModel(String modelName) {
            return () -> KeyBuilder.build("talkx", "cache", "ai_model", modelName);
        }

        public static KeyGenerator stringAiModels() {
            return () -> KeyBuilder.build("talkx", "cache", "ai_models");
        }

        public static KeyGenerator stringProductVersion(Integer productType) {
            return () -> KeyBuilder.build("talkx", "cache", "product_version", productType);
        }

        public static KeyGenerator stringCustomModels() {
            return () -> KeyBuilder.build("talkx", "cache", "custom_models");
        }
    }

    public static class Azure {
        public static KeyGenerator stringStreamSleepTimeInMillis() {
            return () -> KeyBuilder.build("talkx", "azure", "stream_sleep_time");
        }
    }

    public static class WeChat {
        public static KeyGenerator stringAccessToken(String code) {
            return () -> KeyBuilder.build("talkx", "wechat", "access_token", code);
        }
    }

    public static class AboutAds {
        public static KeyGenerator stringClickTracker(String ip) {
            return () -> KeyBuilder.build("talkx", "clickTracker", ip);
        }
    }

    public static class AboutFriend {

        public static KeyGenerator stringFriendList(Integer productType, User user) {
            return stringFriendList(productType, Optional.ofNullable(user).map(User::getId).orElse(-1L));
        }

        public static KeyGenerator stringFriendList(Integer productType, Long userId) {
            return () -> KeyBuilder.build("talkx", "friend_list", productType, userId);
        }

        public static KeyGenerator stringUserFriend(Long userId, Integer productType, String roleType) {
            return () -> KeyBuilder.build("talkx", "user_friend", userId, productType, roleType);
        }

        public static KeyGenerator stringFriendCount() {
            return () -> KeyBuilder.build("talkx", "friend_count");
        }

        public static KeyGenerator stringFriend(Long id) {
            return () -> KeyBuilder.build("talkx", "friend", id);
        }

        public static KeyGenerator stringFriend(String roleType) {
            return () -> KeyBuilder.build("talkx", "friend", roleType);
        }
    }

    public static class Attachment {
        public static KeyGenerator stringAttachment(Long id) {
            return () -> KeyBuilder.build("talkx", "attachment", id);
        }
    }

    public static class AboutSession {
        public static KeyGenerator stringLastSession(Long userId, Long friendId, Integer productId) {
            return () -> KeyBuilder.build("talkx", "last_session", userId, friendId, productId);
        }
    }

    private static String getDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
