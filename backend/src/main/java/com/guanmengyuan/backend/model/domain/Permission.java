package com.guanmengyuan.backend.model.domain;

import java.util.List;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guanmengyuan.backend.model.dto.RouteMeta;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.spring.ex.common.model.domain.TenantDomain;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToMany;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Comment("权限表")
public class Permission extends TenantDomain<Permission> {
    private String component;
    private String i18nKey;
    private String icon;
    private String iconType;
    private String menuName;

    @jakarta.persistence.Column(name = "`desc`")
    private String desc;
    @Comment("权限按钮编码")
    private String code;

    /**
     * 菜单类型
     * 1-目录
     * 2-菜单
     * 3-按钮
     */
    private String menuType;
    /** 排序 */
    @Comment("排序")
    @jakarta.persistence.Column(name = "order_num")
    @Column("order_num")
    private Integer order;
    private Long parentId;
    private String routeName;
    private String routePath;

    @jakarta.persistence.Column(columnDefinition = "tinyint(1)")
    private CommonStatus status;

    @RelationOneToMany(targetField = "parentId")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Permission> children;

    private String activeMenu;
    private Boolean hideInMenu;
    private Boolean multiTab;
    private Boolean constant;
    private Boolean props;
    private Boolean keepAlive;

    @Transient
    @Column(ignore = true)
    private String path;

    @Transient
    @Column(ignore = true)
    private String name;

    public String getName() {
        return routeName;
    }

    public String getPath() {
        return routePath;
    }

    @Transient
    @Column(ignore = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RouteMeta meta;

    /**
     * 获取 meta 对象，自动从其他字段映射生成
     */
    public RouteMeta getMeta() {
        if (meta == null) {
            meta = new RouteMeta();
            meta.setTitle(menuName);
            meta.setI18nKey(i18nKey);
            meta.setIcon(icon);
            meta.setOrder(order);
            meta.setHideInMenu(hideInMenu);
            meta.setActiveMenu(activeMenu);
            meta.setMultiTab(multiTab);
            meta.setKeepAlive(keepAlive);
            meta.setConstant(constant);
        }
        return meta;
    }

}
