package com.guanmengyuan.backend.model.domain;

import org.hibernate.annotations.Comment;

import com.mybatisflex.annotation.Table;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table("tenant")
@Accessors(chain = true)
public class Tenant extends BaseDomain<Tenant> {
    public Tenant() {
    }

    @Comment("租户名称")
    private String name;

    @Comment("租户编码")
    private String code;

    @Comment("租户状态")
    private Integer status;
}
