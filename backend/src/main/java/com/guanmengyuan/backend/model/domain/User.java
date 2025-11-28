package com.guanmengyuan.backend.model.domain;

import java.util.List;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.backend.model.enums.UserGender;
import com.guanmengyuan.spring.ex.common.model.domain.TenantDomain;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationManyToMany;
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
@Comment("用户表")
public class User extends TenantDomain<User> {
    @Comment("用户名")
    private String userName;

    @Comment("密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Comment("用户性别")
    @jakarta.persistence.Column(columnDefinition = "tinyint(1)")
    private UserGender userGender;

    @Comment("用户状态")
    @jakarta.persistence.Column(columnDefinition = "tinyint(1)")
    private CommonStatus status;

    @Comment("用户昵称")
    private String nickName;
    @Comment("用户手机号")
    private String userPhone;

    @Comment("用户邮箱")
    private String userEmail;

    @RelationManyToMany(targetField = "id", selfField = "id", joinTable = "user_role", valueField = "roleCode", joinSelfColumn = "user_id", joinTargetColumn = "role_id", targetTable = "role")
    @Transient
    @Column(ignore = true)
    private List<String> roles;

    @RelationOneToMany(targetField = "userId",targetTable = "user_role",selfField = "id",valueField = "roleId")
    @Transient
    @Column(ignore = true)
    private List<Long> userRoles;

    @Transient
    @Column(ignore = true)
    private List<String> buttons;
}
