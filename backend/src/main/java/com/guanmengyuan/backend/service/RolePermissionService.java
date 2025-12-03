package com.guanmengyuan.backend.service;

import com.guanmengyuan.backend.model.domain.RolePermission;
import com.mybatisflex.core.service.IService;

public interface RolePermissionService extends IService<RolePermission>{

    void initRolePermission(String tenantId,Long roleId);
    
}
