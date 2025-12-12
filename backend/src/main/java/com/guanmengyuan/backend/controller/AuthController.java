package com.guanmengyuan.backend.controller;

import java.util.List;

import org.hibernate.solon.annotation.Db;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.model.dto.LoginRequest;
import com.guanmengyuan.backend.model.dto.LoginToken;
import com.guanmengyuan.backend.model.dto.UserInfo;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.v7.crypto.digest.BCrypt;
import jakarta.persistence.EntityManagerFactory;

@Controller
@Mapping("/auth")
public class AuthController {

    @Db("default")
    private EntityManagerFactory entityManagerFactory;
 
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

    @Get
    @Mapping("/getUserInfo")
    public UserInfo getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = User.of().setId(userId).oneById();
        UserInfo userInfo = new UserInfo();

        userInfo.setUserName(user.getNickName());
        userInfo.setUserId(userId);
        List<String> buttons = Permission.of()
                .select(QueryMethods.distinct(Permission::getName))
                .innerJoin(RolePermission.class)
                .on(QueryMethods.column(Permission::getId).eq(QueryMethods.column(RolePermission::getPermissionId)))
                .innerJoin(UserRole.class)
                .on(QueryMethods.column(RolePermission::getRoleId).eq(QueryMethods.column(UserRole::getRoleId)))
                .where(Permission::getMenuType).eq(3)
                .and(UserRole::getUserId).eq(userId)
                .objListAs(String.class);
        userInfo.setButtons(buttons);
        List<String> roles = UserRole.of()
                .select(QueryMethods.distinct(Role::getRoleCode))
                .innerJoin(Role.class)
                .on(QueryMethods.column(UserRole::getRoleId).eq(QueryMethods.column(Role::getId)))
                .where(UserRole::getUserId).eq(userId)
                .objListAs(String.class);
        userInfo.setRoles(roles);
        return userInfo;
    }

    @Delete
    @Mapping("/logout")
    public Boolean logout() {
        StpUtil.logout();
        return true;
    }
}
