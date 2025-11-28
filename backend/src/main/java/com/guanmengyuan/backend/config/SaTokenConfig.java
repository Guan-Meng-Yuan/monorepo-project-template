package com.guanmengyuan.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    @Bean
    StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStateless();
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(
                handle -> StpUtil.checkLogin())).addPathPatterns("/**")
                .excludePathPatterns("/auth/login");
    }
}
