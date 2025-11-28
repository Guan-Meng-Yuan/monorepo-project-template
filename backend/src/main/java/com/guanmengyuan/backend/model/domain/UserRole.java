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
@Comment("用户角色表")
public class UserRole extends TenantDomain<UserRole> {
    private Long userId;
    private Long roleId;
}
