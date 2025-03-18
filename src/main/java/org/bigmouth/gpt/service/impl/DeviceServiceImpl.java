package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.utils.KeyBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.bigmouth.gpt.entity.Device;
import org.bigmouth.gpt.mapper.talkx.DeviceMapper;
import org.bigmouth.gpt.service.IDeviceService;
import org.bigmouth.gpt.xiaozhi.OtaBindInf;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiMqttConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allen
 * @since 2025-02-27
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final Fetcher fetcher;
    private final Updater updater;
    private final XiaozhiMqttConfig xiaozhiMqttConfig;

    public DeviceServiceImpl(Fetcher fetcher, Updater updater, XiaozhiMqttConfig xiaozhiMqttConfig) {
        this.fetcher = fetcher;
        this.updater = updater;
        this.xiaozhiMqttConfig = xiaozhiMqttConfig;
    }

    @Override
    public Device find(String macAddress) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Device.MAC_ADDRESS, macAddress);
        return getOne(queryWrapper);
    }

    @Override
    public Device findByClientId(String clientId) {
        String clientIdSuffix = parseClientIdSuffix(clientId);
        String macAddress = parse2MacAddress(clientIdSuffix);
        return find(macAddress);
    }

    @Override
    public String generateClientId(String macAddress) {
        String clientIdSuffix = parse2ClientIdSuffix(macAddress);
        return xiaozhiMqttConfig.getClientIdPrefixForDevice() + clientIdSuffix;
    }

    @Override
    public String parseClientIdSuffix(String clientId) {
        return clientId.substring(xiaozhiMqttConfig.getClientIdPrefixForDevice().length());
    }

    /**
     * 将mac地址转换为clientId后缀
     * @param macAddress a0:85:e3:e1:55:34
     * @return a0_85_e3_e1_55_34
     */
    public static String parse2ClientIdSuffix(String macAddress) {
        return macAddress.replace(':', '_');
    }

    /**
     * 将clientId后缀转换为mac地址
     * @param clientIdSuffix a0_85_e3_e1_55_34
     * @return a0:85:e3:e1:55:34
     */
    public static String parse2MacAddress(String clientIdSuffix) {
        return clientIdSuffix.replace('_', ':');
    }

    @Override
    public String createBindCode(OtaBindInf otaBindInf) {
        String code = RandomStringUtils.randomNumeric(6);
        KeyGenerator keyGenerator = stringBindCodeKeyGenerator(code);
        updater.update(keyGenerator, otaBindInf, 10 * 60);
        return code;
    }

    @Override
    public OtaBindInf verifyBindCode(String code) {
        KeyGenerator keyGenerator = stringBindCodeKeyGenerator(code);
        return fetcher.fetch(keyGenerator, OtaBindInf.class);
    }

    private KeyGenerator stringBindCodeKeyGenerator(String code) {
        return () -> KeyBuilder.build("talkx", "device_bind_code", code);
    }
}
