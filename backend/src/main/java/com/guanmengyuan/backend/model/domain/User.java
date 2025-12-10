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
}
