package com.guanmengyuan.backend.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.guanmengyuan.spring.ex.common.model.enums.ParamEnum;
import com.mybatisflex.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserGender implements ParamEnum<Integer> {
    /** 男性 */
    MALE(1),
    /** 女性 */
    FEMALE(2),;

    @JsonValue
    @EnumValue
    private final Integer gender;

    public Integer getValue() {
        return gender;
    }

}
