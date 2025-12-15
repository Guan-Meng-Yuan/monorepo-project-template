package com.guanmengyuan.backend.model.domain;

import java.util.List;

import org.hibernate.annotations.Comment;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("role_permission")
@Accessors(chain = true)
public class RolePermission extends TenantDomain<RolePermission> {

    public RolePermission() {
    }

    @Comment("角色ID")
    private Long roleId;

    @Comment("权限ID")
    private Long permissionId;

    @Transient
    @Column(ignore = true)
    private List<Long> permissionIds;
}
