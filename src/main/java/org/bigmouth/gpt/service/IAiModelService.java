package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.AiModel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.response.ModelResponse;

import java.util.List;

/**
 * <p>
 * 模型列表 服务类
 * </p>
 *
 * @author allen
 * @since 2023-10-13
 */
public interface IAiModelService extends IService<AiModel> {

    /**
     * 获取指定模型配置
     * @param modelName
     * @return 可能返回 null
     */
    AiModel get(String modelName);

    /**
     * 返回有效期内的模型列表
     * @return 模型列表
     */
    List<AiModel> getValidityList();

    /**
     * 获取用户的自定义模型
     *
     * @param userId 用户ID
     * @return 模型
     */
    List<ModelResponse> getMyModelResponse(Long userId);
}
