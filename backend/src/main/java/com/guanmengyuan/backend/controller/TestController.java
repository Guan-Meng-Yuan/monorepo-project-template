package com.guanmengyuan.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guanmengyuan.spring.ex.common.model.dto.res.R;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.v7.core.map.MapUtil;

@RestController
@RequestMapping("test")
@SaIgnore
public class TestController {
    
    @GetMapping
    public R<Map<String,String> > test(){
        return R.ok(MapUtil.of("code","123"));
    }
}
