package com.guanmengyuan.backend.model.domain;

import java.util.List;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Comment("重定向路径")
    private String redirect;

    @Comment("图标")
    private String icon;

    @Comment("图标类型 1:iconify 2:local")
    private Integer iconType;

    @Comment("排序")
    @Column(name = "`order`")
    private Integer order;

    @Comment("激活菜单")
    private String activeMenu;

    @Comment("多标签页")
    private Boolean multiTab;

    @Comment("保持 alive")
    private Boolean keepAlive;

    @Comment("菜单类型 1:目录 2:菜单 3:按钮")
    private Integer menuType;

    @Comment("状态")
    private Integer status;

    @com.mybatisflex.annotation.Column(ignore = true)
    @Transient
    private MenuMeta meta;

    @Transient
    @com.mybatisflex.annotation.Column(ignore = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Permission> children;

    public MenuMeta getMeta() {
        MenuMeta menuMeta = new MenuMeta();
        menuMeta.setTitle(title);
        menuMeta.setI18nKey(i18nKey);
        menuMeta.setConstant(constant);
        menuMeta.setHideInMenu(hideInMenu);
        menuMeta.setIcon(icon);
        menuMeta.setOrder(order);
        menuMeta.setActiveMenu(activeMenu);
        menuMeta.setMultiTab(multiTab);
        menuMeta.setKeepAlive(keepAlive);
        return menuMeta;
    }
}
