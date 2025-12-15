package com.guanmengyuan.backend.controller;

import java.util.Arrays;
import java.util.List;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Post;

import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.domain.Role;
import com.guanmengyuan.backend.service.PermissionService;
import com.mybatisflex.core.paginate.Page;

import lombok.RequiredArgsConstructor;

@Controller
@Mapping("/systemManage")
@RequiredArgsConstructor
public class SystemManageController {

    private final PermissionService permissionService;

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
}
