package org.bigmouth.gpt.autoconfigure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * @author allen
 * @date 2023/6/13
 * @since 1.0.0
 */
@Configuration
public class MyBatisPlusAutoConfiguration {

    private final DynamicDataSourceProperties properties;

    public MyBatisPlusAutoConfiguration(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//    @Bean
//    @Primary
//    @ConditionalOnMissingBean
//    public DynamicDataSourceProvider dynamicDataSourceProvider() {
//        Map<String, DataSourceProperty> datasource = this.properties.getDatasource();
//        return new YmlDynamicDataSourceProvider(datasource);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public DynamicRoutingDataSource dynamicRoutingDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
//        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
//        dynamicRoutingDataSource.setPrimary(this.properties.getPrimary());
//        dynamicRoutingDataSource.setStrict(this.properties.getStrict());
//        dynamicRoutingDataSource.setStrategy(this.properties.getStrategy());
//        dynamicRoutingDataSource.setProvider(dynamicDataSourceProvider);
//        dynamicRoutingDataSource.setP6spy(this.properties.getP6spy());
//        dynamicRoutingDataSource.setSeata(this.properties.getSeata());
//        return dynamicRoutingDataSource;
//    }
}
