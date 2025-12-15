package com.guanmengyuan.backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Post;
import org.noear.solon.data.annotation.Transaction;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.Tenant;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.PermissionService;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.guanmengyuan.backend.service.TenantService;
import com.guanmengyuan.backend.service.UserRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;

import lombok.RequiredArgsConstructor;

@Controller
@Mapping("/systemManage")
@RequiredArgsConstructor
public class SystemManageController {

    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;
    private final UserRoleService userRoleService;
    private final TenantService tenantService;

    @Get
    @Mapping("/getMenuList/v2")
    public Page<Permission> getMenuList(@Param(defaultValue = "1", required = false) Long current,
            @Param(defaultValue = "10", required = false) Long size) {
        return Permission.of()
                .where(Permission::getParentId).isNull()
                .and(Permission::getConstant).eq(false)
                .withFields()
                .fieldMapping(Permission::getChildren, per -> Permission.of()
                        .where(Permission::getParentId).eq(per.getId())
                        .and(Permission::getConstant).eq(false)
                        .toQueryWrapper())
                .page(Page.of(current, size));
    }

    @Get
    @Mapping("/getAllPages")
    public List<String> getAllPages() {
        return Permission.of().select(Permission::getName).where(Permission::getMenuType).eq(2)
                .objListAs(String.class);
    }

    @Get
    @Mapping("/getAllRoles")
    public List<Role> getAllRoles() {
        return Role.of().list();
    }

    @Post
    @Mapping("/saveOrUpdateMenu")
    public Boolean saveOrUpdateMenu(@Body Permission permission) {
        if (Permission.of().where(Permission::getName).eq(permission.getName())
                .and(Permission::getId).ne(permission.getId())
                .exists()) {
            throw new RuntimeException("路由名称已存在");
        }
        return permission.saveOrUpdate();
    }

    @Delete
    @Mapping("/deleteMenu/{ids}")
    public Boolean deleteMenu(String[] ids) {
        return permissionService.removeByIds(Arrays.asList(ids));
    }

    @Get
    @Mapping("getRoleList")
    public Page<Role> getRoleList(@Param(defaultValue = "1", required = false) Long current,
            @Param(defaultValue = "10", required = false) Long size) {
        return Role.of().page(Page.of(current, size));
    }

    @Post
    @Mapping("/saveOrUpdateRole")
    public Boolean saveOrUpdateRole(@Body Role role) {
        if (Role.of().where(Role::getRoleCode).eq(role.getRoleCode()).and(Role::getId).ne(role.getId()).exists()) {
            throw new RuntimeException("角色编码已存在");
        }
        return role.saveOrUpdate();
    }

    @Get
    @Mapping("/getMenuTree")
    public List<Permission> getMenuTree() {
        return Permission.of().where(Permission::getParentId).isNull()
                .and(Permission::getConstant).eq(false)
                .withFields()
                .fieldMapping(Permission::getChildren, per -> Permission.of()
                        .where(Permission::getParentId).eq(per.getId())
                        .and(Permission::getConstant).eq(false)
                        .toQueryWrapper())
                .list();
    }

    @Get
    @Mapping("/getRolePermissionIds/{roleId}")
    public List<Long> getRolePermissionIds(Long roleId) {
        return RolePermission.of().select(QueryMethods.distinct(RolePermission::getPermissionId))
                .where(RolePermission::getRoleId).eq(roleId).objListAs(Long.class);
    }

    @Post
    @Mapping("/saveRolePermission")
    @Transaction
    public Boolean saveRolePermission(@Body RolePermission rolePermission) {
        Boolean result = RolePermission.of().where(RolePermission::getRoleId).eq(rolePermission.getRoleId()).remove();
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : rolePermission.getPermissionIds()) {
            rolePermissions.add(RolePermission.of().setRoleId(rolePermission.getRoleId())
                    .setPermissionId(permissionId));
        }
        if (rolePermissions.size() > 0) {
            return rolePermissionService.saveBatch(rolePermissions);
        }
        return result;
    }

    @Delete
    @Mapping("/deleteRole/{ids}")
    public Boolean deleteRole(String[] ids) {
        return Role.of().where(Role::getId).in(Arrays.asList(ids)).remove();
    }

    @Get
    @Mapping("/getUserList")
    public Page<User> getUserList(@Param(defaultValue = "1", required = false) Long current,
            @Param(defaultValue = "10", required = false) Long size) {
        return User.of().withRelations().page(Page.of(current, size));
    }

    @Post
    @Mapping("/saveOrUpdateUser")
    @Transaction
    public Boolean saveOrUpdateUser(@Body User user) {
        TenantManager.withoutTenantCondition(() -> {
            if (User.of().where(User::getUsername).eq(user.getUsername()).and(User::getId).ne(user.getId()).exists()) {
                throw new RuntimeException("用户名已存在");
            }
        });
        Boolean result = user.saveOrUpdate();
        UserRole.of().where(UserRole::getUserId).eq(user.getId()).remove();
        List<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : user.getUserRoles()) {
            userRoles.add(UserRole.of().setUserId(user.getId()).setRoleId(roleId));
        }
        if (userRoles.size() > 0) {
            return userRoleService.saveBatch(userRoles);
        }
        return result;
    }

    @Delete
    @Mapping("/deleteUser/{ids}")
    public Boolean deleteUser(String[] ids) {
        return User.of().where(User::getId).in(Arrays.asList(ids)).remove();
    }

    @Get
    @Mapping("/getTenantList")
    public Page<Tenant> getTenantList(@Param(defaultValue = "1", required = false) Long current,
            @Param(defaultValue = "10", required = false) Long size) {
        return Tenant.of().page(Page.of(current, size));
    }

    @Post
    @Mapping("/saveOrUpdateTenant")
    @Transaction
    public Boolean saveOrUpdateTenant(@Body Tenant tenant) {
        Boolean isNew = null == tenant.getId();
        if (Tenant.of().where(Tenant::getCode).eq(tenant.getCode()).and(Tenant::getId).ne(tenant.getId()).exists()) {
            throw new RuntimeException("租户编码已存在");
        }
        TenantManager.withoutTenantCondition(() -> {
            if (User.of().where(User::getUsername).eq(tenant.getCode()).exists()) {
                throw new RuntimeException("用户名已存在");
            }
        });
        Boolean result = tenant.saveOrUpdate();
        if (isNew) {
            tenantService.initTenant(tenant);
        }
        return result;
    }

    @Delete
    @Mapping("/deleteTenant/{ids}")
    public Boolean deleteTenant(String[] ids) {
        return Tenant.of().where(Tenant::getId).in(Arrays.asList(ids)).remove();
    }
}
