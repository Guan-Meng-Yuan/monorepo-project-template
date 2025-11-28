package com.guanmengyuan.backend.service;

 
import com.guanmengyuan.backend.model.domain.Permission;
import com.mybatisflex.core.service.IService;

public interface PermissionService extends IService<Permission>{
    /**
     * 初始化租户路由权限（供外部调用，新增租户时使用）
     * 
     * @param tenantId 租户ID
     */
    void initTenantRoutes(String tenantId);
}
