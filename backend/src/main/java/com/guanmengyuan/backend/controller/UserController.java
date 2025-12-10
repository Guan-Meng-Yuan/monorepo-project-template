package com.guanmengyuan.backend.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import com.guanmengyuan.backend.model.domain.User;

@Controller
@Mapping("user")
public class UserController {

    @Mapping("test")
    public long test() {
        User.of().save();
        return User.of().count();
    }
}
