package com.guanmengyuan.backend.model.domain;

import org.hibernate.annotations.Comment;

import com.mybatisflex.annotation.Table;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("role")
@Accessors(chain = true)
public class Role extends TenantDomain<Role> {
    public Role() {
    }

    @Comment("角色名称")
    private String roleName;

    @Comment("角色代码")
    private String roleCode;

    @Comment("角色描述")
    private String roleDesc;
}
