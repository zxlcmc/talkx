package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.entity.*;
import org.bigmouth.gpt.entity.request.FriendCreateRequest;
import org.bigmouth.gpt.entity.request.FriendUpdateMemoryConfigRequest;
import org.bigmouth.gpt.entity.request.FriendUpdateRequest;
import org.bigmouth.gpt.entity.response.FriendPlazaVo;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.scheduler.GptsFetcher;
import org.bigmouth.gpt.scheduler.gpts.Resource;
import org.bigmouth.gpt.service.IFriendService;
import org.bigmouth.gpt.service.IUserFriendService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 好友接口
 *
 * @author allen
 * @date 2023/5/6
 * @since 1.0.0
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    private final IFriendService friendService;
    private final IUserFriendService userFriendService;
    private final GptsFetcher gptsFetcher;

    public FriendController(IFriendService friendService, IUserFriendService userFriendService, GptsFetcher gptsFetcher) {
        this.friendService = friendService;
        this.userFriendService = userFriendService;
        this.gptsFetcher = gptsFetcher;
    }

    @PostMapping("/set_top")
    public ResponseEntity<Object> setTop(@Validated @RequestBody SetTop req) {
        User user = ContextFactory.getLoginUser();
        userFriendService.setTop(user, req.getFriendId(), req.ifTop());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/follow")
    public ResponseEntity<FriendVo> follow(@Validated @RequestBody SetReq req) {
        User user = ContextFactory.getLoginUser();
        FriendVo follow = userFriendService.follow(user, req.getFriendId());
        return ResponseEntity.ok(follow);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Validated @RequestBody SetReq req) {
        User user = ContextFactory.getLoginUser();
        userFriendService.delete(user, req.getFriendId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/query")
    public ResponseEntity<Object> query(@RequestParam(value = "productType", defaultValue = "" + Constants.PRODUCT_TYPE_WEB) Integer productType,
                                        @RequestParam Long friendId) {
        User user = ContextFactory.getLoginUser();
        Long userId = user.getId();
        UserFriend userFriend = userFriendService.getOne(Wrappers.query(new UserFriend().setUserId(userId).setProductType(productType).setFriendId(friendId)));
        if (Objects.isNull(userFriend)) {
            return ResponseEntity.notFound().build();
        }
        Friend friend = friendService.getCacheById(userFriend.getFriendId());
        if (Objects.isNull(friend)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserFriendVo.of(friend, userFriend));
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<FriendVo>> list(@RequestParam(value = "productType", defaultValue = Constants.PRODUCT_TYPE_WEB + "") Integer productType) {
        List<FriendVo> friends = userFriendService.getMyFriends(productType, ContextFactory.getLoginUser());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/plaza")
    public ResponseEntity<List<FriendPlazaVo>> plaza(String tag) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(Friend.IS_PUBLIC, Constants.YES)
                .eq(Friend.DELETED, Constants.NO_DELETE)
                .orderByAsc(Friend.ORDERED, Friend.CREATE_TIME, Friend.ID);
        if (StringUtils.isNotBlank(tag)) {
            queryWrapper.eq(Friend.TAG, tag);
        }
        List<Friend> friends = friendService.list(queryWrapper);

        List<FriendVo> myFriends;
        User user = ContextFactory.getLoginUser();
        if (Objects.nonNull(user)) {
            myFriends = userFriendService.getMyFriends(Constants.PRODUCT_TYPE_WEB, user);
        } else {
            myFriends = userFriendService.getFriendsForGuest();
        }
        Set<Long> myFriendIds = (myFriends.stream().map(FriendVo::getFriendId).collect(Collectors.toSet()));
        List<FriendPlazaVo> res = Lists.newArrayList();
        friends.forEach(e -> {
            FriendPlazaVo vo = FriendPlazaVo.of(e);
            if (myFriendIds.contains(e.getId())) {
                vo.setFollowed(Constants.YES);
            }
            res.add(vo);
        });
        return ResponseEntity.ok(res);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Validated @RequestBody FriendCreateRequest request) {
        FriendVo friendVo = userFriendService.createFriend(request, ContextFactory.getLoginUser());
        return ResponseEntity.ok(friendVo);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@Validated @RequestBody FriendUpdateRequest request) {
        FriendVo friendVo = userFriendService.updateFriend(request, ContextFactory.getLoginUser());
        return ResponseEntity.ok(friendVo);
    }

    @GetMapping("/query_by_user_friend_id")
    public ResponseEntity<Object> queryById(@RequestParam Long userFriendId) {
        User user = ContextFactory.getLoginUser();
        Long userId = user.getId();
        UserFriend userFriend = userFriendService.getById(userFriendId);
        if (Objects.isNull(userFriend) || !userFriend.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        Friend friend = friendService.getCacheById(userFriend.getFriendId());
        if (Objects.isNull(friend)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserFriendVo.of(friend, userFriend));
    }

    @PostMapping("/update_memory_config")
    public ResponseEntity<Object> updateMemoryConfig(@Validated @RequestBody FriendUpdateMemoryConfigRequest request) {
        FriendVo friendVo = userFriendService.updateMemoryConfig(request, ContextFactory.getLoginUser());
        return ResponseEntity.ok(friendVo);
    }

    // ------------ 下面是一些GPTs的接口 ------------ //

    @PostMapping("/syncGptsStore")
    public ResponseEntity<Object> syncGptsStore() {
        gptsFetcher.autoFetchAndStoreToDB();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/syncGptsStoreByGizmoId")
    public ResponseEntity<Object> syncGptsStore(@RequestParam String gizmoId, @RequestParam(required = false) String tagName) {
        gptsFetcher.autoFetchAndStoreToDB(gizmoId, tagName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/syncGptsStoreByGizmoResource")
    public ResponseEntity<Object> syncGptsStore(@RequestBody Resource resource, @RequestParam(required = false) String tagName) {
        gptsFetcher.autoFetchAndStoreToDB(resource, tagName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/readFilesAndToDB")
    public ResponseEntity<Object> readFilesAndToDB(String dir) throws IOException {
        gptsFetcher.readFilesAndToDB(dir);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class SetReq {
        @NotNull(message = "好友ID不能为空") private Long friendId;
    }

    @Data
    public static class SetTop {
        @NotNull(message = "好友ID不能为空") private Long friendId;
        @NotNull(message = "置顶不能为空") private Integer isTop;

        public boolean ifTop() {
            return Objects.equals(Constants.YES, isTop);
        }
    }

}
