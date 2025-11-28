package com.guanmengyuan.backend.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MenuTree implements Serializable {
    private Long id;
    private String label;
    private Long pId;
    private List<MenuTree> children;
}
