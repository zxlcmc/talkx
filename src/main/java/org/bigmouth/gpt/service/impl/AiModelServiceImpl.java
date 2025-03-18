package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxm.warcar.cache.DataExtractor;
import com.bxm.warcar.cache.Fetcher;
import org.apache.commons.collections.CollectionUtils;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.AiModel;
import org.bigmouth.gpt.entity.response.ModelResponse;
import org.bigmouth.gpt.mapper.talkx.AiModelMapper;
import org.bigmouth.gpt.service.IAiModelService;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.utils.RedisKeys;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 模型列表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2023-10-13
 */
@Service
public class AiModelServiceImpl extends ServiceImpl<AiModelMapper, AiModel> implements IAiModelService {

    private final Fetcher fetcher;

    public AiModelServiceImpl(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public AiModel get(String modelName) {
        AiModel aiModel = fetcher.fetch(RedisKeys.Cache.stringAiModel(modelName), new DataExtractor<AiModel>() {
            @Override
            public AiModel extract() {
                return getOne(Wrappers.query(new AiModel().setModelName(modelName)));
            }
        }, AiModel.class, 60);
        if (Objects.nonNull(aiModel) && isActiveNow(aiModel)) {
            return aiModel;
        }
        return null;
    }

    @Override
    public List<AiModel> getValidityList() {
        List<AiModel> aiModels = fetcher.fetchList(RedisKeys.Cache.stringAiModels(), new DataExtractor<List<AiModel>>() {
            @Override
            public List<AiModel> extract() {
                QueryWrapper<AiModel> queryWrapper = new QueryWrapper<>();
                LocalDateTime now = LocalDateTime.now();
                queryWrapper.eq(AiModel.IS_HIDDEN, Constants.NO);
                queryWrapper.lt(AiModel.BEGIN_TIME, now);
                queryWrapper.gt(AiModel.EXPIRE_TIME, now);
                queryWrapper.orderByAsc(AiModel.ORDERED);
                return getBaseMapper().selectList(queryWrapper);
            }
        }, AiModel.class, 60);

        if (CollectionUtils.isNotEmpty(aiModels)) {
            return aiModels.stream().filter(new Predicate<AiModel>() {
                @Override
                public boolean test(AiModel aiModel) {
                    return isActiveNow(aiModel);
                }
            }).collect(Collectors.toList());
        }
        return aiModels;
    }

    @Override
    public List<ModelResponse> getMyModelResponse(Long userId) {
        List<ApplicationConfig.CustomModel> customModels = fetcher.fetchList(RedisKeys.Cache.stringCustomModels(),
                ApplicationConfig.CustomModel.class);
        if (Objects.isNull(customModels)) {
            return null;
        }
        for (ApplicationConfig.CustomModel customModel : customModels) {
            if (customModel.getUserIds().contains(userId)) {
                return customModel.getModelResponses();
            }
        }
        return null;
    }

    private static boolean isActiveNow(AiModel e) {
        LocalDateTime now = LocalDateTime.now();
        boolean between = now.isAfter(e.getBeginTime()) && now.isBefore(e.getExpireTime());
        return between || now.equals(e.getBeginTime()) || now.equals(e.getExpireTime());
    }
}
