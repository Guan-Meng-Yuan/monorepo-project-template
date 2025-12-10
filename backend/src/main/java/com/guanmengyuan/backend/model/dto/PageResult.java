package com.guanmengyuan.backend.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class PageResult<T> {
    private Long current;
    private Long size;
    private Long total;
    private List<T> records;
}

