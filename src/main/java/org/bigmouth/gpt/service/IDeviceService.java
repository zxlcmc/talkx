package org.bigmouth.gpt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.Device;
import org.bigmouth.gpt.xiaozhi.OtaBindInf;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author allen
 * @since 2025-02-27
 */
public interface IDeviceService extends IService<Device> {

    Device find(String macAddress);

    Device findByClientId(String clientId);

    /**
     * 生成clientId
     * @param macAddress a0:85:e3:e1:55:34
     * @return GID_test@@@a0_85_e3_e1_55_34
     */
    String generateClientId(String macAddress);

    /**
     * 解析clientId
     * @param clientId GID_test@@@a0_85_e3_e1_55_34
     * @return a0_85_e3_e1_55_34
     */
    String parseClientIdSuffix(String clientId);

    String createBindCode(OtaBindInf otaBindInf);

    OtaBindInf verifyBindCode(String code);
}
