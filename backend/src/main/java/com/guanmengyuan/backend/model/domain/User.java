package com.guanmengyuan.backend.model.domain;

import java.util.List;

import org.hibernate.annotations.Comment;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("user")
@Accessors(chain = true)
public class User extends TenantDomain<User> {

    public User() {
    }

    @Comment("用户名")
    private String username;

    @Comment("密码")
    private String password;

    @Comment("昵称")
    private String nickName;

    @Comment("性别：1-男，2-女")
    private String userGender;

    @Comment("手机号")
    private String userPhone;

    @Comment("邮箱")
    private String userEmail;

    @Comment("状态：1-正常，2-禁用")
    private Integer status;

    @Transient
    @Column(ignore = true)
    @RelationOneToMany(targetTable = "user_role",targetField = "userId",valueField = "roleId")
    private List<Long> userRoles;
}
