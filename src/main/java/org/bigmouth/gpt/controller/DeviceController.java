package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.bigmouth.gpt.entity.Device;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.UserFriend;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.IDeviceService;
import org.bigmouth.gpt.service.IUserFriendMediaConfigService;
import org.bigmouth.gpt.service.IUserFriendService;
import org.bigmouth.gpt.xiaozhi.OtaBindInf;
import org.bigmouth.gpt.xiaozhi.OtaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2025/2/27
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    private final IUserFriendService userFriendService;
    private final IDeviceService deviceService;
    private final IUserFriendMediaConfigService userFriendMediaConfigService;

    public DeviceController(IUserFriendService userFriendService, IDeviceService deviceService, IUserFriendMediaConfigService userFriendMediaConfigService) {
        this.userFriendService = userFriendService;
        this.deviceService = deviceService;
        this.userFriendMediaConfigService = userFriendMediaConfigService;
    }

    @PostMapping("/bind")
    public ResponseEntity<Device> bindDevice(@RequestBody @Validated BindDeviceRequest request) {
        User user = ContextFactory.getLoginUser();

        // 验证好友关系
        Long userFriendId = request.getUserFriendId();
        UserFriend userFriend = userFriendService.getById(userFriendId);
        if (userFriend == null || !Objects.equals(userFriend.getUserId(), user.getId())) {
            throw new IllegalStateException("好友关系不存在");
        }

        // 验证验证码
        OtaBindInf otaBindInf = deviceService.verifyBindCode(request.getCode());
        if (Objects.isNull(otaBindInf)) {
            throw new IllegalStateException("验证码不正确，请输入设备提示的验证码");
        }

        OtaRequest otaRequest = otaBindInf.getRequest();

        // 验证设备是否已经绑定
        String macAddress = otaRequest.getMacAddress();
        Device exists = deviceService.find(macAddress);
        if (Objects.nonNull(exists)) {
            throw new IllegalStateException("该设备已绑定");
        }

        // 绑定设备
        Device device = new Device()
                .setUserId(user.getId())
                .setUserFriendId(userFriendId)
                .setFriendId(userFriend.getFriendId())
                .setRoleType(userFriend.getRoleType())
                .setDeviceModel(otaRequest.getBoard().getType())
                .setChipModelName(otaRequest.getChipModelName())
                .setFirmwareVersion(otaRequest.getApplication().getVersion())
                .setMacAddress(macAddress)
                .setBindTime(LocalDateTime.now())
                ;
        deviceService.save(device);

        return ResponseEntity.ok(device);
    }

    @PostMapping("/unbind")
    public ResponseEntity<Void> unbindDevice(@RequestBody @Validated UnbindDeviceRequest request) {
        User user = ContextFactory.getLoginUser();

        // 验证设备是否存在
        Device device = deviceService.getById(request.getDeviceId());
        if (Objects.isNull(device)) {
            throw new IllegalStateException("设备不存在");
        }

        // 验证设备所有权
        if (!Objects.equals(device.getUserId(), user.getId())) {
            throw new IllegalStateException("无权操作此设备");
        }

        // 删除设备绑定记录
        deviceService.removeById(device.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateDevice(@RequestBody @Validated UpdateDeviceRequest request) {
        User user = ContextFactory.getLoginUser();

        // 验证设备是否存在
        Device device = deviceService.getById(request.getDeviceId());
        if (Objects.isNull(device)) {
            throw new IllegalStateException("设备不存在");
        }

        // 验证设备所有权
        if (!Objects.equals(device.getUserId(), user.getId())) {
            throw new IllegalStateException("无权操作此设备");
        }

        // 更新设备信息
        if (request.getRemark() != null) {
            device.setRemark(request.getRemark());
        }
        if (request.getAutoOtaUpgrade() != null) {
            device.setOtaUpdate(request.getAutoOtaUpgrade());
        }
        device.setModifyTime(LocalDateTime.now());
        deviceService.updateById(device);

        return ResponseEntity.ok().build();
    }

    @Data
    public static class UpdateDeviceRequest {
        @NotNull(message = "设备ID不能为空")
        private Long deviceId;

        @Size(max = 255, message = "备注长度不能超过255个字符")
        private String remark;

        private Integer autoOtaUpgrade;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Device>> listDevices(@RequestParam Long userFriendId) {
        User user = ContextFactory.getLoginUser();

        // 验证好友关系
        UserFriend userFriend = userFriendService.getOne(
                new QueryWrapper<UserFriend>().eq(UserFriend.USER_ID, user.getId()).eq(UserFriend.ID, userFriendId)
        );
        if (userFriend == null) {
            throw new IllegalStateException("好友关系不存在");
        }

        // 查询设备列表
        QueryWrapper<Device> queryWrapper = new QueryWrapper<Device>()
                .eq(Device.USER_ID, user.getId())
                .eq(Device.USER_FRIEND_ID, userFriendId)
                .orderByDesc(Device.BIND_TIME);
        List<Device> devices = deviceService.list(queryWrapper);

        return ResponseEntity.ok(devices);
    }

    @Data
    public static class BindDeviceRequest {
        @NotBlank(message = "验证码不能为空")
        @Size(min = 6, max = 6, message = "验证码必须是6位数字")
        private String code;

        @NotNull(message = "好友ID不能为空")
        private Long userFriendId;
    }

    @Data
    public static class UnbindDeviceRequest {
        @NotNull(message = "设备ID不能为空")
        private Long deviceId;
    }
}
