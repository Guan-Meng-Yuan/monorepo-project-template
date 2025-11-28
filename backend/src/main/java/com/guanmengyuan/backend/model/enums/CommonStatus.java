package com.guanmengyuan.backend.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.guanmengyuan.spring.ex.common.model.enums.ParamEnum;
import com.mybatisflex.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonStatus implements ParamEnum<Integer> {
    /** 启用 */
    ENABLE(1),
    /** 禁用 */
    DISABLE(2),;

    @JsonValue
    @EnumValue
    private final Integer status;

    public Integer getValue() {
        return status;
    }
}
