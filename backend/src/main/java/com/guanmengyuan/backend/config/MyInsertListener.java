package com.guanmengyuan.backend.config;

import java.util.Date;

import com.guanmengyuan.backend.model.domain.BaseDomain;
import com.mybatisflex.annotation.InsertListener;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义插入监听器
 */
@Slf4j
public class MyInsertListener implements InsertListener {
    /**
     * 默认构造
     */
    public MyInsertListener() {
    }

    @Override
    public void onInsert(Object o) {
        Long userId = 0L;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            log.atDebug().log("saToken get loginId unsuccessful");
        }
        if (o instanceof BaseDomain<?> baseDomain) {
            baseDomain.setCreateUserId(userId);
            baseDomain.setCreateTime(new Date());
        }
    }
}
