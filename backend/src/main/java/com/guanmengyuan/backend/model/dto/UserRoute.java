package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;
import java.util.List;

import com.guanmengyuan.backend.model.domain.Permission;

import lombok.Data;

@Data
public class UserRoute implements Serializable{
    private List<Permission> routes; 
    private String home;
}
