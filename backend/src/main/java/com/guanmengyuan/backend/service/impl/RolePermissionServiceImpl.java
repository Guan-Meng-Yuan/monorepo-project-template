package com.guanmengyuan.backend.service.impl;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.RolePermissionMapper;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements RolePermissionService {

}

