package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest implements Serializable{
    
    @NotBlank(message = "请输入用户名")
    private String userName;
    @NotBlank(message = "请输入密码")
    private String password;
}
