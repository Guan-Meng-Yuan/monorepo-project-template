package com.guanmengyuan.backend.service.impl;

import java.util.List;

import org.noear.solon.annotation.Component;

import com.guanmengyuan.backend.mapper.PermissionMapper;
import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.service.PermissionService;
import com.mybatisflex.solon.service.impl.ServiceImpl;

@Component
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {
    @Override
    public List<Long> initPermissions(String tenantId) {

        // login 路由
        Permission.of()
                .setName("login")
                .setPath("/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?")
                .setComponent("layout.blank$view.login")
                .setProps(true)
                .setTitle("login")
                .setI18nKey("route.login")
                .setConstant(true)
                .setHideInMenu(true)
                .setMenuType(2)
                .setStatus(1)
                .setTenantId(tenantId).save();

        // 403 路由
        Permission.of()
                .setName("403")
                .setPath("/403")
                .setComponent("layout.blank$view.403")
                .setProps(null)
                .setTitle("403")
                .setI18nKey("route.403")
                .setConstant(true)
                .setMenuType(2)
                .setHideInMenu(true)
                .setStatus(1)
                .setTenantId(tenantId).save();

        // 404 路由
        Permission.of()
                .setName("404")
                .setPath("/404")
                .setComponent("layout.blank$view.404")
                .setProps(null)
                .setTitle("404")
                .setI18nKey("route.404")
                .setConstant(true)
                .setHideInMenu(true)
                .setMenuType(2)
                .setStatus(1)
                .setTenantId(tenantId).save();

        // 500 路由
        Permission.of()
                .setName("500")
                .setPath("/500")
                .setComponent("layout.blank$view.500")
                .setProps(null)
                .setTitle("500")
                .setI18nKey("route.500")
                .setConstant(true)
                .setHideInMenu(true)
                .setMenuType(2)
                .setStatus(1)
                .setTenantId(tenantId).save();

        // about 路由
        Permission.of()
                .setName("about")
                .setPath("/about")
                .setComponent("layout.base$view.about")
                .setProps(null)
                .setTitle("about")
                .setI18nKey("route.about")
                .setIcon("fluent:book-information-24-regular")
                .setIconType(1)
                .setOrder(10)
                .setConstant(false)
                .setHideInMenu(false)
                .setMenuType(2)
                .setStatus(1)
                .setTenantId(tenantId).save();
        // home 路由
        Permission.of()
                .setName("home")
                .setPath("/home")
                .setComponent("layout.base$view.home")
                .setProps(null)
                .setTitle("home")
                .setI18nKey("route.home")
                .setIcon("mdi:monitor-dashboard")
                .setIconType(1)
                .setOrder(1)
                .setMenuType(2)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(false)
                .setTenantId(tenantId).save();

        // manage 路由组
        Permission manageRoute = Permission.of()
                .setName("manage")
                .setPath("/manage")
                .setComponent("layout.base")
                .setProps(null)
                .setTitle("manage")
                .setI18nKey("route.manage")
                .setIcon("carbon:cloud-service-management")
                .setIconType(1)
                .setOrder(9)
                .setConstant(false)
                .setHideInMenu(false)
                .setMenuType(1)
                .setStatus(1)
                .setTenantId(tenantId);
        manageRoute.save();
        Long manageParentId = manageRoute.getId();

        // manage_menu
        Permission.of()
                .setName("manage_menu")
                .setPath("/manage/menu")
                .setComponent("view.manage_menu")
                .setProps(null)
                .setTitle("manage_menu")
                .setI18nKey("route.manage_menu")
                .setIcon("material-symbols:route")
                .setIconType(1)
                .setOrder(3)
                .setMenuType(2)
                .setKeepAlive(true)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(false)
                .setParentId(manageParentId)
                .setTenantId(tenantId).save();

        // manage_role
        Permission.of()
                .setName("manage_role")
                .setPath("/manage/role")
                .setComponent("view.manage_role")
                .setProps(null)
                .setTitle("manage_role")
                .setI18nKey("route.manage_role")
                .setIcon("carbon:user-role")
                .setIconType(1)
                .setOrder(2)
                .setMenuType(2)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(false)
                .setParentId(manageParentId)
                .setTenantId(tenantId).save();

        // manage_user
        Permission.of()
                .setName("manage_user")
                .setPath("/manage/user")
                .setComponent("view.manage_user")
                .setProps(null)
                .setTitle("manage_user")
                .setI18nKey("route.manage_user")
                .setIcon("ic:round-manage-accounts")
                .setIconType(1)
                .setOrder(1)
                .setMenuType(2)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(false)
                .setParentId(manageParentId)
                .setTenantId(tenantId).save();

        // manage_user-detail
        Permission.of()
                .setName("manage_user-detail")
                .setPath("/manage/user-detail/:id")
                .setComponent("view.manage_user-detail")
                .setProps(true)
                .setTitle("manage_user-detail")
                .setI18nKey("route.manage_user-detail")
                .setActiveMenu("manage_user")
                .setMenuType(2)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(true)
                .setParentId(manageParentId)
                .setTenantId(tenantId).save();

        // user-center 路由
        Permission.of()
                .setName("user-center")
                .setPath("/user-center")
                .setComponent("layout.base$view.user-center")
                .setProps(null)
                .setTitle("user-center")
                .setI18nKey("route.user-center")
                .setMenuType(2)
                .setStatus(1)
                .setConstant(false)
                .setHideInMenu(true)
                .setTenantId(tenantId).save();

        return Permission.of()
                .select(Permission::getId)
                .where(Permission::getTenantId).eq(tenantId).and(Permission::getConstant).eq(false)
                .objListAs(Long.class);
    }

}
