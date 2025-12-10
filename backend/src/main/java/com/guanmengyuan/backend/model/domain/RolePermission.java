package com.guanmengyuan.backend.model.domain;

import com.mybatisflex.annotation.Table;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

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
}

