package org.bigmouth.gpt.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bxm.warcar.utils.JsonHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.bigmouth.gpt.ai.entity.Model;
import org.bigmouth.gpt.entity.Friend;
import org.bigmouth.gpt.entity.PromptConfig;
import org.bigmouth.gpt.scheduler.gpts.*;
import org.bigmouth.gpt.service.IFriendService;
import org.bigmouth.gpt.service.IPromptConfigService;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.utils.HttpClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class GptsFetcher {

    public static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJwd2RfYXV0aF90aW1lIjoxNzE1MjQxNDczNjc3LCJzZXNzaW9uX2lkIjoidU9QTmZuemVDeTB0UzA4Z05pR080Rk9WTVhGU2R3TVgiLCJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJqYXZhaHV4aWFvQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlfSwiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS9hdXRoIjp7InBvaWQiOiJvcmctaFJ0RVhDREhpR21VN1FRSFBvT0NaaGh0IiwidXNlcl9pZCI6InVzZXItd1dKMzR1S2UwZ1F5c3dFa1F1ZkpENlZoIn0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJhdXRoMHw2MzkxMzYzZGU4YzhhZmMwNGJmYTY1YzYiLCJhdWQiOlsiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS92MSIsImh0dHBzOi8vb3BlbmFpLm9wZW5haS5hdXRoMGFwcC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNzE1MjQxNDc3LCJleHAiOjE3MTYxMDU0NzcsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgbW9kZWwucmVhZCBtb2RlbC5yZXF1ZXN0IG9yZ2FuaXphdGlvbi5yZWFkIG9yZ2FuaXphdGlvbi53cml0ZSBvZmZsaW5lX2FjY2VzcyIsImF6cCI6IlRkSkljYmUxNldvVEh0Tjk1bnl5d2g1RTR5T282SXRHIn0.qQL2bcybtOEZkC0tK3FTwSVQA-8g6pmoc3lE7PuUmnPJcLNG6Pe6MqENZaYjLr2OZidHo8AqU5KRXY1JQ6H8JA_vSuI-_degnAc57tjYMzZS29rTwHTs88AuBenNjjJVp0cDk-8igWx6buaGdmej9wmgwMtENjpjjf-M8Ctx6jJDmajws_808zz3r3_-YGni7vI-5zzCbcBcxHpjVOJFtM5pdGOgkBTHTfcPe-bQl9pM4MxJbmBJZefgLQhCQl8lP4xwnhM19Tg0S4dxxdMbyZAY3mBjjAXw8r_byM7S5SRc_G_O2p-TZegcTnsBcsdlqvg8i-NCZxHd3dTSW5I2ww";

    private final String host = "https://chat.openai.com";

    private final HttpClient client;
    private IFriendService friendService;
    private IPromptConfigService promptConfigService;

    public GptsFetcher() {
        this.client = HttpClientHelper.createHttpClient(10, 10,
                1000, 5000, 30000);
    }

    @Autowired(required = false)
    public void setFriendService(IFriendService friendService) {
        this.friendService = friendService;
    }

    @Autowired(required = false)
    public void setPromptConfigService(IPromptConfigService promptConfigService) {
        this.promptConfigService = promptConfigService;
    }

    public Response discovery() {
        HttpGet get = new HttpGet(host + "/public-api/gizmos/discovery_anon?locale=global");
        try {
            addHeaders(get);
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.valueOf(statusCode));
            }
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);
            return JsonHelper.convert(json, Response.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            get.releaseConnection();
        }
    }

    public Cut discovery(String infoId, int cursor, int limit) {
        HttpGet get = new HttpGet(String.format(host + "/public-api/gizmos/discovery/%s?cursor=%d&limit=%d&locale=global",
                infoId, cursor, limit));
        try {
            addHeaders(get);
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.valueOf(statusCode));
            }
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);
            return JsonHelper.convert(json, Cut.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            get.releaseConnection();
        }
    }
    public Resource discovery(String gizmoId) {
        HttpGet get = new HttpGet(String.format(host + "/backend-api/gizmos/%s", gizmoId));
        try {
            addHeaders(get);
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.valueOf(statusCode));
            }
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);
            return JsonHelper.convert(json, Resource.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            get.releaseConnection();
        }
    }

    private static void addHeaders(HttpGet get) {
        get.addHeader("Host", "chat.openai.com");
        get.addHeader("Content-Type", "application/json;charset=utf-8");
        get.addHeader("Referer", "https://chat.openai.com/gpts");
        get.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        get.addHeader("Authorization", String.format("Bearer %s", TOKEN));
    }

    public PromptConfig toPromptConfig(Gizmo gizmo) {
        return new PromptConfig()
                .setRoleType(gizmo.getId())
                .setGptsId(gizmo.getId())
                .setFixedKey(Constants.NO)
                .setCoinCostPer(BigDecimal.valueOf(0.05))
                .setMessageContextSize(64);
    }

    public Friend toFriend(Gizmo gizmo, String tag) {
        Display display = gizmo.getDisplay();
        List<String> promptStarters = Optional.ofNullable(display.getPromptStarters())
                .orElse(Lists.newArrayList())
                .stream()
                .map(s -> StringUtils.replace(s, ",", "，"))
                .collect(Collectors.toList());
        String profilePictureUrl = Optional.ofNullable(display.getProfilePictureUrl())
                .filter(StringUtils::isNotBlank)
                .orElse(Constants.DEFAULT_AVATAR_URL);
        String welcome = Optional.ofNullable(display.getWelcomeMessage()).filter(StringUtils::isNotBlank).orElse(display.getDescription());
        String finalTag = convertTag(gizmo, tag);
        return new Friend()
                .setOrdered(1000)
                .setAvatar(profilePictureUrl)
                .setName(display.getName())
                .setRoleType(gizmo.getId())
                .setFriendType(Constants.Friend.FRIEND_TYPE_GPTS)
                .setFixedModel(Model.GPT_4_GIZMO.getName())
                .setCommentTags("[{\"name\":\"GPTs\",\"bgColor\":\"#007ACC\",\"color\":\"#FFFFFF\"},{\"name\":\"收费0.05粒/次\",\"bgColor\":\"#CC6400\",\"color\":\"#FFFFFF\"}]")
                .setConversactionStart(StringUtils.substring(StringUtils.join(promptStarters, ","), 0, 1024))
                .setVoiceChat(Constants.NO)
                .setWelcome(welcome)
                .setIntro(display.getDescription())
                .setIsDefaultFriend(Constants.NO)
                .setIsPublic(Constants.YES)
                .setTag(finalTag);
    }

    private String convertTag(Gizmo gizmo, String defaultTagName) {
        String tag = StringUtils.defaultIfBlank(defaultTagName, "其他");
        List<String> categories = gizmo.getDisplay().getCategories();
        if (CollectionUtils.isEmpty(categories)) {
            return tag;
        }
        for (String category : categories) {
            String tagOfCategories = CategoriesTag.getTagOfCategories(category);
            if (StringUtils.isNotBlank(tagOfCategories)) {
                return tagOfCategories;
            }
        }
        return tag;
    }

    @Transactional(rollbackFor = Exception.class)
    public void readFilesAndToDB(String dir) throws IOException {
        File file = new File(dir);
        Collection<File> files = FileUtils.listFiles(file, new PrefixFileFilter("g-"), null);
        for (File f : files) {
            log.info("处理文件:{}", f);
            String json = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
            Resource resource = JsonHelper.convert(json, Resource.class);
            autoFetchAndStoreToDB(resource, null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoFetchAndStoreToDB(String gizmoId, String tagName) {
        Resource discovery = this.discovery(gizmoId);
        Gizmo gizmo = discovery.getGizmo();
        saveFriend(tagName, gizmo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoFetchAndStoreToDB(Resource discovery, String tagName) {
        Gizmo gizmo = discovery.getGizmo();
        saveFriend(tagName, gizmo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoFetchAndStoreToDB() {
        Response response = this.discovery();
        List<Cut> cuts = response.getCuts();
        for (Cut cut : cuts) {
            Info info = cut.getInfo();
            for (int i = 0; i < 2; i++) {
                int pageSize = 10;
                Cut discovery = this.discovery(info.getId(), i * pageSize, (i + 1) * pageSize);
                ListContainer list = discovery.getList();
                List<Item> items = list.getItems();
                for (Item item : items) {
                    Gizmo gizmo = item.getResource().getGizmo();
                    saveFriend(GroupEnum.of(info.getTitle()).getTagName(), gizmo);
                }
            }
        }
    }

    private void saveFriend(String tagName, Gizmo gizmo) {
        PromptConfig promptConfig = this.toPromptConfig(gizmo);
        Friend friend = this.toFriend(gizmo, tagName);
        log.info("Created: {}", friend);
        log.info("Created: {}", promptConfig);
        if (Objects.nonNull(friendService) && Objects.nonNull(promptConfigService)) {
            Friend exists = friendService.getByRoleType(friend.getRoleType());
            if (Objects.isNull(exists)) {
                friendService.save(friend);
            }
            PromptConfig config = promptConfigService.getOne(Wrappers.query(new PromptConfig().setRoleType(friend.getRoleType())));
            if (Objects.isNull(config)) {
                promptConfigService.save(promptConfig);
            }
        }
    }

    public enum CategoriesTag {
        /**
         *
         */
        Dalle("dalle", "DALL·E"),
        Writing("writing", "文案"),
        Productivity("productivity", "效率"),
        Research("research", "研究分析"),
        Programming("programming", "编程"),
        Education("education", "教育"),
        Lifestyle("lifestyle", "生活"),
        Other("other", "其他");

        private final String categories;
        private final String tag;

        CategoriesTag(String categories, String tag) {
            this.categories = categories;
            this.tag = tag;
        }

        public static String getTagOfCategories(String categories) {
            for (CategoriesTag value : values()) {
                if (StringUtils.equalsIgnoreCase(categories, value.categories)) {
                    return value.tag;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        // （需要魔法）
        // 1、打开 https://chatgpt.com/gpts，并登录，获取到 Authorization
        // 2、修改 TOKEN 变量值
        // 3、执行这个函数，程序将自动下载 GPTs 信息到本地

        // （切换VPC网络）
        // 4、启动 Application（vpc）
        // 5、POST请求 request-gpts-sync-dir.http
        // 6、完成

        GptsFetcher gpTsFetcher = new GptsFetcher();
        Response response = gpTsFetcher.discovery();
        List<Cut> cuts = response.getCuts();
        for (Cut cut : cuts) {
            Info info = cut.getInfo();
            for (int i = 0; i < 2; i++) {
                int pageSize = 10;
                Cut discovery = gpTsFetcher.discovery(info.getId(), i * pageSize, (i + 1) * pageSize);
                ListContainer list = discovery.getList();
                List<Item> items = list.getItems();
                for (Item item : items) {
                    Resource resource = item.getResource();
                    File file = new File("/Users/huxiao/Documents/gpts/" + resource.getGizmo().getId());
                    try {
                        FileUtils.writeStringToFile(file, JsonHelper.convert(resource), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                    Gizmo gizmo = resource.getGizmo();
//                    gpTsFetcher.autoFetchAndStoreToDB(gizmo.getId(), "其他");
//                    PromptConfig promptConfig = gpTsFetcher.toPromptConfig(gizmo);
//                    Friend friend = gpTsFetcher.toFriend(gizmo, GroupEnum.of(info.getTitle()).getTagName());
//                    log.info("{}", friend);
//                    log.info("{}", promptConfig);
//                    log.info("======================================================");
                }
            }
        }
    }
}
