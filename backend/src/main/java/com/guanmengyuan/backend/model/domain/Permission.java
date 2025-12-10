package com.guanmengyuan.backend.model.domain;

import org.hibernate.annotations.Comment;

import com.guanmengyuan.backend.model.dto.MenuMeta;
import com.mybatisflex.annotation.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("permission")
@Accessors(chain = true)
public class Permission extends TenantDomain<Permission> {

    public Permission() {
    }

    @Comment("路由名称")
    @Column(name = "name")
    private String name;

    @Comment("路由路径")
    @Column(name = "path")
    private String path;

    @Comment("组件路径")
    @Column(name = "component")
    private String component;

    @Comment("是否传递props")
    @Column(name = "props")
    private Boolean props;

    @Comment("路由标题")
    @Column(name = "title")
    private String title;

    @Comment("国际化键")
    private String i18nKey;

    @Comment("是否常量路由")
    private Boolean constant;

    @Comment("是否在菜单中隐藏")
    private Boolean hideInMenu;

    @Comment("父权限ID")
    private Long parentId;

    @com.mybatisflex.annotation.Column(ignore = true)
    @Transient
    private MenuMeta meta;

    public MenuMeta getMeta() {
        MenuMeta menuMeta = new MenuMeta();
        menuMeta.setTitle(title);
        menuMeta.setI18nKey(i18nKey);
        menuMeta.setConstant(constant);
        menuMeta.setHideInMenu(hideInMenu);
        return menuMeta;
    }
}
