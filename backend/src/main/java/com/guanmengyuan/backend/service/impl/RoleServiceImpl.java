package com.guanmengyuan.backend.service.impl;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.RoleMapper;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.service.RoleService;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Role initRole(String tenantId) {
        Role role = Role.of();
        role.setRoleName("超级管理员");
        role.setRoleCode("R_SUPER");
        role.setTenantId(tenantId);
        role.save();
        return role;
    }

}
