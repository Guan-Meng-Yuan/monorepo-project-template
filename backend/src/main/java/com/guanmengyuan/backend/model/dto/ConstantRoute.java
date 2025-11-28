package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConstantRoute implements Serializable {
    private String name;
    private String path;
    private String component;
    private Boolean props;
    private RouteMeta meta;
}

