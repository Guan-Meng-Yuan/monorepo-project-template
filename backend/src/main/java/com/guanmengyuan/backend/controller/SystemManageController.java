package com.guanmengyuan.backend.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;

import com.guanmengyuan.backend.model.domain.Permission;
import com.mybatisflex.core.paginate.Page;

@Controller
@Mapping("/systemManage")
public class SystemManageController {
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
}
