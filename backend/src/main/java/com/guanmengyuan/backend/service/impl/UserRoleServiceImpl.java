package com.guanmengyuan.backend.service.impl;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.UserRoleMapper;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.UserRoleService;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

