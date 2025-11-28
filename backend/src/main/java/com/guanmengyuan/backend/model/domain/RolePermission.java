package com.guanmengyuan.backend.model.domain;

import org.hibernate.annotations.Comment;

import com.guanmengyuan.spring.ex.common.model.domain.TenantDomain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Comment("角色权限表")
public class RolePermission extends TenantDomain<RolePermission>{
    private Long roleId;
    private Long permissionId;
}
