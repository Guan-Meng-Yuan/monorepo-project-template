package com.guanmengyuan.backend.service;

import com.guanmengyuan.backend.model.domain.Tenant;
import com.mybatisflex.core.service.IService;

public interface TenantService extends IService<Tenant> {

    void initTenant(Tenant tenant);
    
}
