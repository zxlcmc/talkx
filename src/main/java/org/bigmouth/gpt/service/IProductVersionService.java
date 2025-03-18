package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.ProductVersion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品的版本管理 服务类
 * </p>
 *
 * @author allen
 * @since 2023-10-23
 */
public interface IProductVersionService extends IService<ProductVersion> {

    /**
     * 按照产品的发布时间，获取最新的可用版本
     * @param productType
     * @return
     */
    ProductVersion getLatestVersionByProduct(Integer productType);
}
