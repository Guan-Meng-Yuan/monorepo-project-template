package com.guanmengyuan.backend.service.impl;

import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.TenantMapper;
import com.guanmengyuan.backend.model.domain.Tenant;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.backend.service.PermissionService;
import com.guanmengyuan.backend.service.TenantService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import cn.hutool.v7.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
    private final PermissionService permissionService;

    @Override
    public void initTenantManager(Tenant tenant) {
        String tenantId = tenant.getId().toString();
        
        // 创建租户管理员用户
        User user = User.of()
                .setUserName(tenant.getCode())
                .setNickName(tenant.getName())
                .setPassword(BCrypt.hashpw("123456"))
                .setStatus(CommonStatus.ENABLE)
                .setTenantId(tenantId);
        user.save();
        
        // 初始化租户路由权限
        permissionService.initTenantRoutes(tenantId);
    }

}
