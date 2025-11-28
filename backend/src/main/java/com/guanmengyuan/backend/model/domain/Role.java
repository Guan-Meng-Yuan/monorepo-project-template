package com.guanmengyuan.backend.model.domain;


import org.hibernate.annotations.Comment;

import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.spring.ex.common.model.domain.TenantDomain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Comment("角色表")
public class Role extends TenantDomain<Role> {
    private String roleName;
    private String roleCode;
    private String roleDesc;
    @Column(columnDefinition = "tinyint(1)")
    private CommonStatus status; 
}
