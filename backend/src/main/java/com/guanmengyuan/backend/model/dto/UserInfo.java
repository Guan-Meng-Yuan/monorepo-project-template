package com.guanmengyuan.backend.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
    private Long userId;
    private String userName;
    private List<String> roles;
    private List<String> buttons;
}

