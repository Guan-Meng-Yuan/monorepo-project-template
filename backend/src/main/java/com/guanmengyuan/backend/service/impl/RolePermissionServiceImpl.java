package com.guanmengyuan.backend.service.impl;

import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.RolePermissionMapper;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    
}
