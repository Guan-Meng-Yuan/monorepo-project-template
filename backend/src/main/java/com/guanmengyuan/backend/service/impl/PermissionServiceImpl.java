package com.guanmengyuan.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;

import com.guanmengyuan.backend.mapper.PermissionMapper;
import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.service.PermissionService;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService, LifecycleBean {

    @Override
    public void postStart() throws Throwable {
        TenantManager.withoutTenantCondition(() -> {
            if (!Permission.of().exists()) {
                List<Permission> permissions = new ArrayList<>();

                // login 路由
                Permission loginRoute = Permission.of()
                        .setName("login")
                        .setPath("/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?")
                        .setComponent("layout.blank$view.login")
                        .setProps(true)
                        .setTitle("login")
                        .setI18nKey("route.login")
                        .setConstant(true)
                        .setHideInMenu(true)
                        .setTenantId("000000");
                permissions.add(loginRoute);

                // 403 路由
                Permission route403 = Permission.of()
                        .setName("403")
                        .setPath("/403")
                        .setComponent("layout.blank$view.403")
                        .setProps(null)
                        .setTitle("403")
                        .setI18nKey("route.403")
                        .setConstant(true)
                        .setHideInMenu(true)
                        .setTenantId("000000");
                permissions.add(route403);

                // 404 路由
                Permission route404 = Permission.of()
                        .setName("404")
                        .setPath("/404")
                        .setComponent("layout.blank$view.404")
                        .setProps(null)
                        .setTitle("404")
                        .setI18nKey("route.404")
                        .setConstant(true)
                        .setHideInMenu(true)
                        .setTenantId("000000");
                permissions.add(route404);

                // 500 路由
                Permission route500 = Permission.of()
                        .setName("500")
                        .setPath("/500")
                        .setComponent("layout.blank$view.500")
                        .setProps(null)
                        .setTitle("500")
                        .setI18nKey("route.500")
                        .setConstant(true)
                        .setHideInMenu(true)
                        .setTenantId("000000");
                permissions.add(route500);
                saveBatch(permissions);
            }
        });
    }

}
