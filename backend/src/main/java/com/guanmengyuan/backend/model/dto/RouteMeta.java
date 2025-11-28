package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RouteMeta implements Serializable {
    private String title;
    private String i18nKey;
    private String icon;
    private Integer order;
    private Boolean hideInMenu;
    private String activeMenu;
    private Boolean multiTab;
    private Boolean keepAlive;
    private Boolean constant;
}

