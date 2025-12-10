package com.guanmengyuan.backend.controller;

import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;

import com.guanmengyuan.backend.model.domain.Permission;
import com.mybatisflex.core.tenant.TenantManager;

@Controller
@Mapping("/route")
public class RouteController {

    @Get
    @Mapping("/getConstantRoutes")
    public List<Permission> getConstantRoutes() {
        return TenantManager.withoutTenantCondition(() -> Permission.of()
                .where(Permission::getConstant).eq(true)
                .list());
    }

    @Get
    @Mapping("/getUserRoutes")
    public Object getUserRoutes() {
        // TODO: 实现获取用户路由的逻辑
        return new Object();
    }

    @Get
    @Mapping("/isRouteExist")
    public boolean isRouteExist(@Param("routeName") String routeName) {
        // TODO: 实现检查路由是否存在的逻辑
        return false;
    }
}
