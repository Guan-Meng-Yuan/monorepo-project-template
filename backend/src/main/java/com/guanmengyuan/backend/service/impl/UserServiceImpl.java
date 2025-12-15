package com.guanmengyuan.backend.service.impl;

import java.util.List;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.solon.data.annotation.Transaction;

import com.guanmengyuan.backend.mapper.UserMapper;
import com.guanmengyuan.backend.model.constant.TenantConstant;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.PermissionService;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.guanmengyuan.backend.service.RoleService;
import com.guanmengyuan.backend.service.UserService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.solon.service.impl.ServiceImpl;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, LifecycleBean {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    @Override
    @Transaction
    public void postStart() throws Throwable {
        TenantManager.withoutTenantCondition(() -> {
            if (!User.of().exists()) {
                User user = User.of();
                user.setUsername("admin");
                user.setPassword(BCrypt.hashpw("123456"));
                user.setStatus(1);
                user.setTenantId(TenantConstant.DEFAULT_TENANT_ID);
                user.save();
                List<Long> permissionIds = permissionService.initPermissions(TenantConstant.DEFAULT_TENANT_ID, true);
                Role role = roleService.initRole(TenantConstant.DEFAULT_TENANT_ID);
                UserRole.of().setUserId(user.getId()).setRoleId(role.getId())
                        .setTenantId(TenantConstant.DEFAULT_TENANT_ID).save();
                List<RolePermission> rolePermissions = ListUtil.of();
                for (Long permissionId : permissionIds) {
                    rolePermissions.add(RolePermission.of().setRoleId(role.getId()).setPermissionId(permissionId)
                            .setTenantId(TenantConstant.DEFAULT_TENANT_ID));
                }
                rolePermissionService.saveBatch(rolePermissions);
            }
        });
    }

}
