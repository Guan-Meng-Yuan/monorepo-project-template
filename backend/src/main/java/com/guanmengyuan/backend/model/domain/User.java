package com.guanmengyuan.backend.model.domain;

import com.mybatisflex.annotation.Table;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("user")
@Accessors(chain = true)
public class User extends TenantDomain<User> {

    public User(){}
    private String username;
    private String password;
}
