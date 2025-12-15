package com.guanmengyuan.backend.service;

import java.util.List;

import com.guanmengyuan.backend.model.domain.Permission;
import com.mybatisflex.core.service.IService;

public interface PermissionService extends IService<Permission> {

    List<Long> initPermissions(String tenantId, Boolean initTenant);
}

