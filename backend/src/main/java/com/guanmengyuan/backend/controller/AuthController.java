package com.guanmengyuan.backend.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.dto.LoginRequest;
import com.guanmengyuan.backend.model.dto.LoginResult;
import com.guanmengyuan.backend.model.dto.UserInfo;
import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import com.mybatisflex.core.tenant.TenantManager;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.crypto.digest.BCrypt;

@RestController
@RequestMapping("auth")
public class AuthController {

    @PostMapping("login")
    public R<LoginResult> login(@RequestBody @Validated LoginRequest loginRequest) {
        return TenantManager.withoutTenantCondition(() -> {
            User user = User.of().where(User::getUserName).eq(loginRequest.getUserName()).one();
            if (null == user) {
                throw new ServiceException("用户名或密码错误");
            }
            if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                throw new ServiceException("用户名或密码错误");
            }
            StpUtil.login(user.getId(), SaLoginParameter.create().setExtra("tenantId", user.getTenantId()));
            LoginResult loginResult = new LoginResult();
            loginResult.setToken(StpUtil.getTokenValue());
            return R.ok(loginResult);
        });
    }

    @DeleteMapping("logout")
    public R<Boolean> logout() {
        StpUtil.logout();
        return R.ok(true);
    }

    @GetMapping("getUserInfo")
    public R<UserInfo> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user= User.of()
        .setId(userId)
        .withRelations().oneById();
        UserInfo userInfo= new UserInfo();
        userInfo.setButtons(ListUtil.of());
        userInfo.setUserName(user.getUserName());
        userInfo.setNickName(user.getNickName());
        userInfo.setRoles(user.getRoles());
        userInfo.setUserId(userId);
        return R.ok(userInfo);
    }
}
