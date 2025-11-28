package com.guanmengyuan.backend.service.impl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.UserMapper;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.backend.model.enums.UserGender;
import com.guanmengyuan.backend.service.UserService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import cn.hutool.v7.crypto.digest.BCrypt;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        TenantManager.withoutTenantCondition(() -> {
            if (!User.of().exists()) {
                User user = User.of()
                        .setId(1L)
                        .setTenantId("000000")
                        .setUserName("admin")
                        .setPassword(BCrypt.hashpw("123456"))
                        .setStatus(CommonStatus.ENABLE)
                        .setUserGender(UserGender.MALE);
                user.save();
                if (!Role.of().exists()) {
                    Role role = Role.of()
                            .setId(1L)
                            .setRoleCode("R_SUPER")
                            .setRoleName("超级管理员")
                            .setTenantId("000000")
                            .setStatus(CommonStatus.ENABLE);
                    role.save();
                    UserRole.of().setUserId(user.getId()).setRoleId(role.getId()).setTenantId("000000").save();
                }
            }
        });
    }

}
