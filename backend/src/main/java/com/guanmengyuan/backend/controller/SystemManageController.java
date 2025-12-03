package com.guanmengyuan.backend.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.model.domain.RolePermission;
import com.guanmengyuan.backend.model.domain.Tenant;
import com.guanmengyuan.backend.model.domain.User;
import com.guanmengyuan.backend.model.domain.UserRole;
import com.guanmengyuan.backend.service.RolePermissionService;
import com.guanmengyuan.backend.service.TenantService;
import com.guanmengyuan.backend.service.UserRoleService;
import com.guanmengyuan.backend.service.UserService;
import com.guanmengyuan.spring.ex.common.model.dto.req.PageReq;
import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.text.StrUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("systemManage")
@RequiredArgsConstructor
public class SystemManageController {
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final RolePermissionService rolePermissionService;
    private final TenantService tenantService;

    @GetMapping("getUserList")
    public R<Page<User>> getUserList(PageReq<User> pageReq, User user) {
        return R.ok(User.of()
                .where(User::getUserName).like(user.getUserName(), StrUtil.isNotBlank(user.getUserName()))
                .and(User::getUserGender).eq(user.getUserGender())
                .and(User::getNickName).like(user.getNickName(), StrUtil.isNotBlank(user.getNickName()))
                .and(User::getUserPhone).like(user.getUserPhone(), StrUtil.isNotBlank(user.getUserPhone()))
                .and(User::getUserEmail).like(user.getUserEmail(), StrUtil.isNotBlank(user.getUserEmail()))
                .and(User::getStatus).eq(user.getStatus())
                .withRelations()
                .page(pageReq.of()));
    }

    @GetMapping("getTenantList")
    public R<Page<Tenant>> getTenantList(PageReq<Tenant> pageReq, Tenant tenant) {
        return R.ok(Tenant.of().where(Tenant::getName).like(tenant.getName(), StrUtil.isNotBlank(tenant.getName()))
                .and(Tenant::getCode).like(tenant.getCode(), StrUtil.isNotBlank(tenant.getCode()))
                .and(Tenant::getStatus).eq(tenant.getStatus())
                .withRelations()
                .page(pageReq.of()));
    }

    @GetMapping("getAllRoles")
    public R<List<Role>> getAllRoles() {
        return R.ok(Role.of().list());
    }

    @PostMapping("addOrUpdateUser")
    @Transactional
    public R<Boolean> addOrUpdateUser(@RequestBody User user) {
        List<Long> userRoles = user.getUserRoles();
        if (CollUtil.isEmpty(userRoles)) {
            throw new ServiceException("请选择用户角色");
        }
        if (User.of().where(User::getUserName).eq(user.getUserName())
                .and(User::getId).ne(user.getId())
                .exists()) {
            throw new ServiceException("用户名已存在");
        }
        Boolean result = user.saveOrUpdate();
        userRoleService.remove(UserRole.of().where(UserRole::getUserId).eq(user.getId()).toQueryWrapper());
        List<UserRole> userRoleList = ListUtil.of();
        for (Long roleId : userRoles) {
            userRoleList.add(UserRole.of().setUserId(user.getId()).setRoleId(roleId));
        }
        userRoleService.saveBatch(userRoleList);
        return R.ok(result);
    }

    @PostMapping("addOrUpdateTenant")
    @Transactional
    public R<Boolean> addOrUpdateTenant(@RequestBody @Validated Tenant tenant) {
        return TenantManager.withoutTenantCondition(() -> {
            if (Tenant.of().where(Tenant::getCode).eq(tenant.getCode())
                    .and(Tenant::getId).ne(tenant.getId())
                    .exists()) {
                throw new ServiceException("租户编码已存在");
            }
            Boolean isNew = null == tenant.getId();
            Boolean result = tenant.saveOrUpdate();
            if (isNew) {
                tenantService.initTenantManager(tenant, false);
            }
            return R.ok(result);

        });
    }

    @PostMapping("addOrUpdateRole")
    @Transactional
    public R<Boolean> addOrUpdateRole(@RequestBody Role role) {
        if (Role.of().where(Role::getRoleCode).eq(role.getRoleCode())
                .and(Role::getId).ne(role.getId())
                .exists()) {
            throw new ServiceException("角色编码已存在");
        }
        return R.ok(role.saveOrUpdate());
    }

    @DeleteMapping("deleteRole/{ids}")
    @Transactional
    public R<Boolean> deleteRole(@PathVariable Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            throw new ServiceException("请选择角色");
        }
        return R.ok(Role.of().where(Role::getId).in(ListUtil.of(ids)).remove());
    }

    @DeleteMapping("deleteMenu/{ids}")
    @Transactional
    public R<Boolean> deleteMenu(@PathVariable Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            throw new ServiceException("请选择菜单");
        }
        return R.ok(Permission.of().where(Permission::getId).in(ListUtil.of(ids)).remove());
    }

    @DeleteMapping("deleteUser/{ids}")
    public R<Boolean> deleteUser(@PathVariable Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            throw new ServiceException("请选择用户");
        }
        return R.ok(userService.removeByIds(ListUtil.of(ids)));
    }

    @GetMapping("getRoleList")
    public R<Page<Role>> getRoleList(PageReq<Role> pageReq, Role role) {
        return R.ok(Role.of()
                .where(Role::getRoleName).like(role.getRoleName(), StrUtil.isNotBlank(role.getRoleName()))
                .and(Role::getRoleCode).like(role.getRoleCode(), StrUtil.isNotBlank(role.getRoleCode()))
                .and(Role::getStatus).eq(role.getStatus())
                .withRelations()
                .page(pageReq.of()));
    }

    @GetMapping("getMenuList/v2")
    public R<Page<Permission>> getMenuList(PageReq<Permission> pageReq, Permission permission) {
        return R.ok(Permission.of()
                .where(Permission::getParentId).eq(0)
                .orderBy(Permission::getHideInMenu).asc()
                .orderBy(Permission::getOrder).asc()
                .withRelations()
                .page(pageReq.of()));
    }

    @GetMapping("getAllPages")
    public R<List<String>> getAllPages() {
        return R.ok(Permission.of()
                .select(Permission::getRouteName)
                .where(Permission::getMenuType).eq("2")
                .objListAs(String.class));
    }

    @GetMapping("getMenuTree")
    public R<List<Permission>> getMenuTree() {
        return R.ok(Permission.of()
                .where(Permission::getParentId).eq(0)
                .orderBy(Permission::getHideInMenu).asc()
                .orderBy(Permission::getOrder).asc()
                .withRelations()
                .list());
    }

    @GetMapping("getRolePermissionId/{roleId}")
    public R<List<Long>> getRolePermissionIds(@PathVariable String roleId) {
        return R.ok(
                RolePermission.of()
                        .select(QueryMethods.distinct(RolePermission::getPermissionId))
                        .where(RolePermission::getRoleId).eq(roleId)
                        .objListAs(Long.class)

        );
    }

    @PostMapping("saveRolePermission/{roleId}")
    @Transactional
    public R<Boolean> saveRolePermission(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        RolePermission.of()
                .where(RolePermission::getRoleId).eq(roleId)
                .remove();
        if (CollUtil.isEmpty(permissionIds)) {
            return R.ok(true);
        }
        List<RolePermission> rolePermissionList = ListUtil.of();
        for (Long permissionId : permissionIds) {
            rolePermissionList.add(RolePermission.of().setRoleId(roleId).setPermissionId(permissionId));
        }

        return R.ok(rolePermissionService.saveBatch(rolePermissionList));
    }

    @PostMapping("saveOrUpdateMenu")
    @Transactional
    public R<Boolean> saveOrUpdateMenu(@RequestBody Permission permission) {
        Boolean result = permission.saveOrUpdate();
        if (CollUtil.isNotEmpty(permission.getChildren())) {
            Permission.of().where(Permission::getParentId).eq(permission.getId())
                    .remove();
            for (Permission child : permission.getChildren()) {
                child.setParentId(permission.getId());
                child.saveOrUpdate();
            }
        }
        return R.ok(result);
    }

    @GetMapping("getAllButtons")
    public R<List<Permission>> getAllButtons() {
        return R.ok(Permission.of()
                .where(Permission::getMenuType).eq("3")
                .list());
    }

    @GetMapping("getRoleButtonPermissions/{roleId}")
    public R<List<Long>> getRoleButtonPermissions(@PathVariable String roleId) {
        return R.ok(
                RolePermission.of()
                        .select(QueryMethods.distinct(RolePermission::getPermissionId))
                        .innerJoin(Permission.class)
                        .on(QueryMethods.column(RolePermission::getPermissionId)
                                .eq(QueryMethods.column(Permission::getId)))
                        .where(Permission::getMenuType).eq("3")
                        .and(RolePermission::getRoleId).eq(roleId)
                        .objListAs(Long.class));
    }

    @DeleteMapping("deleteTenant/{ids}")
    @Transactional
    public R<Boolean> deleteTenant(@PathVariable Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            throw new ServiceException("请选择租户");
        }
        return R.ok(Tenant.of().where(Tenant::getId).in(ListUtil.of(ids)).remove());
    }
}
