package com.guanmengyuan.backend.controller;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;

import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.dto.LoginRequest;
import com.guanmengyuan.backend.model.dto.LoginToken;
import com.mybatisflex.core.tenant.TenantManager;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.v7.crypto.digest.BCrypt;

@Controller
@Mapping("/auth")
public class AuthController {

    @Post
    @Mapping("/login")
    public LoginToken login(@Body LoginRequest request) {
        String userName = request.getUserName();
        User user = TenantManager.withoutTenantCondition(() -> {
            return User.of().where(User::getUsername).eq(userName).one();
        });
        if (null == user) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        StpUtil.login(user.getId(), SaLoginParameter.create().setExtra("tenantId", user.getTenantId()));
        LoginToken loginToken = new LoginToken();
        loginToken.setToken(StpUtil.getTokenValue());
        loginToken.setRefreshToken(StpUtil.getTokenValue());
        return loginToken;
    }
}
