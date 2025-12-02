package com.guanmengyuan.backend.model.domain;

import org.hibernate.annotations.Comment;

import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Comment("租户表")
public class Tenant extends BaseDomain<Tenant> {

    @Comment("租户名称")
    @NotBlank(message = "请输入租户名称")
    private String name;
    @Comment("租户编码")
    @NotBlank(message = "请输入租户编码")
    private String code;

    @Comment("租户描述")
    @Column(name = "`desc`")
    private String desc;
    @Column(columnDefinition = "tinyint(1)")
    private CommonStatus status;
}
