package com.guanmengyuan.backend.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Configuration;

import com.guanmengyuan.backend.model.domain.BaseDomain;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.core.tenant.TenantFactory;
import com.mybatisflex.solon.ConfigurationCustomizer;
import com.mybatisflex.solon.MyBatisFlexCustomizer;

import cn.dev33.satoken.stp.StpUtil;

@Component
@Configuration
public class MyBatisFlexCustomizerImpl implements MyBatisFlexCustomizer, ConfigurationCustomizer {

    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new MyLogicDeleteProcessor();
    }

    @Bean
    public TenantFactory tenantFactory() {
        return new TenantFactory() {

            @Override
            public Object[] getTenantIds() {
                return new Object[] { StpUtil.getExtra("tenantId") };
            }
        };
    }

    @Override
    public void customize(FlexConfiguration configuration) {
    }

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new MyInsertListener(), BaseDomain.class);
        config.registerUpdateListener(new MyUpdateListener(), BaseDomain.class);
    }

}
