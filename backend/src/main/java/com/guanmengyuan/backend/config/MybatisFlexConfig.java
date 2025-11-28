package com.guanmengyuan.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mybatisflex.core.tenant.TenantFactory;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;

@Configuration
public class MybatisFlexConfig {
    @Bean
    TenantFactory tenantFactory(){
        return new TenantFactory() {
            @Override
            public Object[] getTenantIds() {
                try {
                    // 检查 SaToken 上下文是否已初始化
                    if (SaHolder.getStorage() == null) {
                        // 上下文未初始化时返回 null，表示不应用租户条件
                        return null;
                    }
                    Object tenantId = StpUtil.getExtra("tenantId");
                    return tenantId != null ? new Object[]{tenantId} : null;
                } catch (Exception e) {
                    // 如果获取失败，返回 null，表示不应用租户条件
                    return null;
                }
            }
        };
    }
}
