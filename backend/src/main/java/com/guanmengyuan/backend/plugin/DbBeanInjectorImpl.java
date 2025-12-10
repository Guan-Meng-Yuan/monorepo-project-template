package com.guanmengyuan.backend.plugin;

import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.VarHolder;
import org.noear.solon.data.datasource.DsInjector;

import com.guanmengyuan.backend.plugin.annotation.Db;

/**
 * @author lingkang
 * @since 2.5
 */
public class DbBeanInjectorImpl extends DsInjector<Db> {
    public DbBeanInjectorImpl() {
        super(Db::value);

        addHandler(this::injectHandle);
    }

    public void injectHandle(VarHolder vh, BeanWrap dsBw) {
        HibernateAdapter adapter = HibernateAdapterManager.get(dsBw);

        if (adapter != null) {
            adapter.injectTo(vh);
        }
    }
}