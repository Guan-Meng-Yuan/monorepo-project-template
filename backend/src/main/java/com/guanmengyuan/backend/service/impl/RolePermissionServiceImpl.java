package com.guanmengyuan.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.RolePermissionMapper;
import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements RolePermissionService {

    @Override
    public void initRolePermission(String tenantId, Long roleId) {
        List<Long> permissionIds = Permission.of()
                .select(Permission::getId)
                .where(Permission::getTenantId).eq(tenantId)
                .objListAs(Long.class);
        for (Long permissionId : permissionIds) {
            RolePermission.of().setRoleId(roleId).setPermissionId(permissionId).setTenantId(tenantId).save();
        }
    }

}
