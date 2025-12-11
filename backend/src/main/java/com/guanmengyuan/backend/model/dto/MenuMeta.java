package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MenuMeta implements Serializable{
    private String title; 
    private String i18nKey;
    private Boolean constant;
    private Boolean hideInMenu;
    private String icon;
    private Integer order;
    private String activeMenu;
    private Boolean multiTab;
    private Boolean keepAlive;
}
