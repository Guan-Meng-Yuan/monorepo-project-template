package com.guanmengyuan.backend.service.impl;

import org.springframework.stereotype.Service;

import com.guanmengyuan.backend.mapper.UserMapper;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
