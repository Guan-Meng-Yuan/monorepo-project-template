package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResult implements Serializable {
    private String token;
    private String refreshToken;
}
