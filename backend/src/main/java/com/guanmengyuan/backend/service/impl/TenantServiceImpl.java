package com.guanmengyuan.backend.service.impl;

import java.util.List;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.TenantMapper;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.Tenant;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.PermissionService;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.guanmengyuan.backend.service.RoleService;
import com.guanmengyuan.backend.service.TenantService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.solon.service.impl.ServiceImpl;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    @Override
    public void initTenant(Tenant tenant) {
        TenantManager.withoutTenantCondition(() -> {
            String tenantId = tenant.getId().toString();
            User user = User.of().setUsername(tenant.getCode())
                    .setPassword(BCrypt.hashpw(tenant.getCode()))
                    .setStatus(1)
                    .setTenantId(tenantId);
            user.save();

            List<Long> permissionIds = permissionService.initPermissions(tenantId, false);
            Role role = roleService.initRole(tenantId);
            UserRole.of().setUserId(user.getId()).setRoleId(role.getId())
                    .setTenantId(tenantId).save();
            List<RolePermission> rolePermissions = ListUtil.of();
            for (Long permissionId : permissionIds) {
                rolePermissions.add(RolePermission.of().setRoleId(role.getId()).setPermissionId(permissionId)
                        .setTenantId(tenantId));
            }
            rolePermissionService.saveBatch(rolePermissions);
        });
    }
}
