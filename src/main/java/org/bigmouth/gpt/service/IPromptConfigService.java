package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.PromptConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author allen
 * @since 2024-01-03
 */
public interface IPromptConfigService extends IService<PromptConfig> {

    /**
     * 获取指定 roleType 的最新一个有效的提示词配置
     * @param roleType {@link org.bigmouth.gpt.entity.RoleType 角色类型}
     * @return 提示词配置，有可能为 {@code null}
     */
    PromptConfig getOne(String roleType);

    /**
     * 创建一个新的 roleType
     * @return roleType
     */
    String createRoleType();
}
