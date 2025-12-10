package com.guanmengyuan.backend.service.impl;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.RoleMapper;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.service.RoleService;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}

