package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxm.warcar.cache.DataExtractor;
import com.bxm.warcar.cache.Fetcher;
import org.apache.commons.collections.CollectionUtils;
import org.bigmouth.gpt.entity.ProductVersion;
import org.bigmouth.gpt.mapper.talkx.ProductVersionMapper;
import org.bigmouth.gpt.service.IProductVersionService;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.utils.RedisKeys;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 产品的版本管理 服务实现类
 * </p>
 *
 * @author allen
 * @since 2023-10-23
 */
@Service
public class ProductVersionServiceImpl extends ServiceImpl<ProductVersionMapper, ProductVersion> implements IProductVersionService {

    private final Fetcher fetcher;

    public ProductVersionServiceImpl(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public ProductVersion getLatestVersionByProduct(Integer productType) {
        if (Objects.isNull(productType)) {
            return null;
        }
        return fetcher.fetch(RedisKeys.Cache.stringProductVersion(productType), new DataExtractor<ProductVersion>() {
            @Override
            public ProductVersion extract() {
                QueryWrapper<ProductVersion> query = new QueryWrapper<ProductVersion>()
                        .eq(ProductVersion.PRODUCT_TYPE, productType)
                        .eq(ProductVersion.STATUS, Constants.ProductVersion.STATUS_AVAILABLE)
                        .orderByDesc(ProductVersion.RELEASE_TIME);
                IPage<ProductVersion> page = new Page<ProductVersion>().setSize(1);
                IPage<ProductVersion> result = page(page, query);
                List<ProductVersion> records = result.getRecords();
                return CollectionUtils.isNotEmpty(records) ? records.iterator().next() : null;
            }
        }, ProductVersion.class, 5 * 60);
    }
}
