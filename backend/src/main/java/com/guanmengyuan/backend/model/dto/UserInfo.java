package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class UserInfo implements Serializable{
    private Long userId;
    private String userName;
    private List<String> roles;
    private List<String> buttons;
    private String nickName;
}
