package com.guanmengyuan.backend.service;

import com.guanmengyuan.backend.model.domain.Role;
import com.mybatisflex.core.service.IService;

public interface RoleService extends IService<Role> {

    Role initRole(String tenantId);
}

