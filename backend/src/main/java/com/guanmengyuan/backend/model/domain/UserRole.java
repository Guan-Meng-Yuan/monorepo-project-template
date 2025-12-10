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
@Table("user_role")
@Accessors(chain = true)
public class UserRole extends TenantDomain<UserRole> {

    public UserRole() {
    }

    @Comment("用户ID")
    private Long userId;

    @Comment("角色ID")
    private Long roleId;
}

