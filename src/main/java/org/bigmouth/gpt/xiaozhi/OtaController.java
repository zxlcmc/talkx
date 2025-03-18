package org.bigmouth.gpt.xiaozhi;

import com.bxm.warcar.utils.Constants;
import com.bxm.warcar.utils.JsonHelper;
import org.bigmouth.gpt.entity.Device;
import org.bigmouth.gpt.service.IDeviceService;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2025/2/20
 */
@RestController
@RequestMapping("/xiaozhi")
public class OtaController {

    private final IDeviceService deviceService;
    private final MqttService mqttService;

    public OtaController(IDeviceService deviceService, MqttService mqttService) {
        this.deviceService = deviceService;
        this.mqttService = mqttService;
    }

    @PostMapping("/ota")
    public ResponseEntity<String> ota(@RequestBody @Validated OtaRequest request) {
        String macAddress = request.getMacAddress();
        OtaResponse response = new OtaResponse();

        // 设置MQTT配置
        OtaResponse.Mqtt mqtt = mqttService.create(macAddress);
        if (Objects.isNull(mqtt)) {
            throw new IllegalStateException("MQTT Server create fail!");
        }
        response.setMqtt(mqtt);

        // 设置服务器时间
        OtaResponse.ServerTime serverTime = new OtaResponse.ServerTime();
        serverTime.setTimestamp(Instant.now().toEpochMilli());
        serverTime.setTimezone("Asia/Shanghai");
        serverTime.setTimezoneOffset(ZoneId.of("Asia/Shanghai").getRules().getOffset(Instant.now()).getTotalSeconds() / 60);
        response.setServerTime(serverTime);

        // 设置固件信息
        OtaResponse.Firmware firmware = new OtaResponse.Firmware();
        firmware.setVersion(request.getApplication().getVersion());
        firmware.setUrl("");
        response.setFirmware(firmware);

        Device device = deviceService.find(macAddress);
        if (device == null) {
            OtaResponse.Activation activation = new OtaResponse.Activation();
            String bindCode = deviceService.createBindCode(new OtaBindInf().setRequest(request).setResponse(response));
            activation.setMessage(bindCode);
            activation.setCode(bindCode);

            response.setActivation(activation);
        } else {
            // 只有当激活了才检查固件信息
            boolean isOtaUpdate = Objects.equals(Constants.YES, device.getOtaUpdate());
            if (isOtaUpdate) {
                // TODO
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(JsonHelper.convert(response));
    }
}
