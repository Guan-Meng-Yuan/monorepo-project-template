package com.guanmengyuan.backend.service.impl;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;

import com.guanmengyuan.backend.mapper.UserMapper;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.service.UserService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.solon.service.impl.ServiceImpl;

import cn.hutool.v7.crypto.digest.BCrypt;

@Component
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService,LifecycleBean{

    @Override
    public void postStart() throws Throwable {
    TenantManager.withoutTenantCondition(()->{
        if(!User.of().exists()){
            User user = User.of();
            user.setUsername("admin");
            user.setPassword(BCrypt.hashpw("123456"));
            user.setTenantId("000000");
            user.save();
        }
    });
    }
    
}
