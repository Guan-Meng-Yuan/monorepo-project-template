package com.guanmengyuan.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.model.dto.UserRouteDto;
import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("route")
public class RouteController {

    @GetMapping("getConstantRoutes")
    @SaIgnore
    public R<List<Permission>> getConstantRoutes() {
        // 查询所有常量路由（constant=true），不受租户限制
        List<Permission> permissions = TenantManager.withoutTenantCondition(() -> {
            return Permission.of()
                    .where(Permission::getConstant).eq(true)
                    .list();
        });
        return R.ok(permissions);
    }

    @GetMapping("getUserRoutes")
    public R<UserRouteDto> getUserRoutes() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Permission> permissions = Permission.of()
                .innerJoin(RolePermission.class)
                .on(QueryMethods.column(Permission::getId).eq(QueryMethods.column(RolePermission::getPermissionId)))
                .innerJoin(UserRole.class)
                .on(QueryMethods.column(RolePermission::getRoleId).eq(QueryMethods.column(UserRole::getRoleId)))
                .where(UserRole::getUserId).eq(userId)
                .and(Permission::getParentId).eq(0)
                .and(Permission::getConstant).eq(false)
                .and(Permission::getMenuType).ne("3")
                .withFields()
                .fieldMapping(Permission::getChildren, per -> Permission.of()
                        .innerJoin(RolePermission.class)
                        .on(QueryMethods.column(Permission::getId)
                                .eq(QueryMethods.column(RolePermission::getPermissionId)))
                        .innerJoin(UserRole.class)
                        .on(QueryMethods.column(RolePermission::getRoleId).eq(QueryMethods.column(UserRole::getRoleId)))
                        .where(UserRole::getUserId).eq(userId)
                        .and(Permission::getConstant).eq(false)
                        .and(Permission::getMenuType).ne("3")
                        .and(Permission::getParentId).eq(per.getId()).toQueryWrapper())
                .list();

        UserRouteDto userRouteDto = new UserRouteDto();
        userRouteDto.setRoutes(permissions);
        userRouteDto.setHome("home");
        return R.ok(userRouteDto);
    }
}
