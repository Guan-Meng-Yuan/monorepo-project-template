package com.guanmengyuan.backend.controller;

import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.model.dto.UserRoute;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;

import cn.dev33.satoken.stp.StpUtil;

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
    public UserRoute getUserRoutes() {
        UserRoute userRoute = new UserRoute();
        userRoute.setHome("home");
        userRoute.setRoutes(Permission.of()
                .innerJoin(RolePermission.class)
                .on(QueryMethods.column(Permission::getId).eq(QueryMethods.column(RolePermission::getPermissionId)))
                .innerJoin(UserRole.class)
                .on(QueryMethods.column(RolePermission::getRoleId).eq(QueryMethods.column(UserRole::getRoleId)))
                .where(Permission::getMenuType).ne(3)
                .and(UserRole::getUserId).eq(StpUtil.getLoginIdAsLong())
                .and(Permission::getParentId).isNull()
                .withFields()
                .fieldMapping(Permission::getChildren, per -> Permission.of()
                        .innerJoin(RolePermission.class)
                        .on(QueryMethods.column(Permission::getId)
                                .eq(QueryMethods.column(RolePermission::getPermissionId)))
                        .innerJoin(UserRole.class)
                        .on(QueryMethods.column(RolePermission::getRoleId)
                                .eq(QueryMethods.column(UserRole::getRoleId)))
                        .where(Permission::getMenuType).ne(3)
                        .and(UserRole::getUserId).eq(StpUtil.getLoginIdAsLong())
                        .and(Permission::getParentId).eq(per.getId())
                        .toQueryWrapper())
                .list());
        return userRoute;
    }

    @Get
    @Mapping("/isRouteExist")
    public boolean isRouteExist(@Param("routeName") String routeName) {
        // TODO: 实现检查路由是否存在的逻辑
        return false;
    }
}
