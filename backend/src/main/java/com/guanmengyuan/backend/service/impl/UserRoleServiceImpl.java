package com.guanmengyuan.backend.service.impl;

import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.UserRoleMapper;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.UserRoleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper,UserRole> implements UserRoleService{
    
}
