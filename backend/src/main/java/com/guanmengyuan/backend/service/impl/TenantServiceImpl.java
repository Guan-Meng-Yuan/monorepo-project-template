package com.guanmengyuan.backend.service.impl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guanmengyuan.backend.mapper.TenantMapper;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.Tenant;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.backend.service.PermissionService;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.guanmengyuan.backend.service.TenantService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import cn.hutool.v7.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService, CommandLineRunner {
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;

    @Override
    @Transactional
    public void initTenantManager(Tenant tenant,Boolean isSys) {
        String tenantId = tenant.getId().toString();

        // 1. 创建租户管理员用户
        User user = User.of()
                .setUserName(tenant.getCode())
                .setNickName(tenant.getName())
                .setPassword(BCrypt.hashpw("123456"))
                .setStatus(CommonStatus.ENABLE)
                .setTenantId(tenantId);
        user.save();

        // 2. 创建角色（超级管理员）
        Role role = Role.of()
                .setRoleCode("R_SUPER")
                .setRoleName("超级管理员")
                .setTenantId(tenantId)
                .setStatus(CommonStatus.ENABLE);
        role.save();

        // 3. 创建用户角色关系
        UserRole.of()
                .setUserId(user.getId())
                .setRoleId(role.getId())
                .setTenantId(tenantId)
                .save();

        // 4. 初始化租户路由权限（会创建权限和角色权限关系）
        permissionService.initPermission(tenantId,isSys);

        rolePermissionService.initRolePermission(tenantId, role.getId());
    }

    @Override
    public void run(String... args) throws Exception {
        TenantManager.withoutTenantCondition(() -> {
            if (!Tenant.of().exists()) {
                Tenant tenant = Tenant.of().setCode("admin").setName("系统管理员").setStatus(CommonStatus.ENABLE);
                tenant.save();
                initTenantManager(tenant,true);
            }
        });
    }

}
