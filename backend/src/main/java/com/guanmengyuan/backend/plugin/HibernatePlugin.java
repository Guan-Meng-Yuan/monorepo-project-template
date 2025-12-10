package com.guanmengyuan.backend.plugin;

import javax.sql.DataSource;

import org.noear.solon.Utils;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.VarHolder;
import org.noear.solon.data.datasource.DsInjector;
import org.noear.solon.data.datasource.DsUtils;

import com.guanmengyuan.backend.plugin.annotation.Db;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernatePlugin implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        //增加 jpa 的 solon yml 配置支持
        PersistenceProviderResolverHolder
                .getPersistenceProviderResolver()
                .getPersistenceProviders()
                .add(new JpaPersistenceProvider());

        context.subWrapsOfType(DataSource.class, HibernateAdapterManager::register);

        //添加 db 注入处理
        DbBeanInjectorImpl dbBeanInjector = new DbBeanInjectorImpl();
        context.beanInjectorAdd(Db.class, dbBeanInjector);
        DsInjector.getDefault().addHandler(dbBeanInjector::injectHandle);

        // 标准 jpa PersistenceContext 注入支持
        //PersistenceUnit
        context.beanInjectorAdd(PersistenceContext.class, this::persistenceContextInject);
        context.beanInjectorAdd(PersistenceUnit.class, this::persistenceUnitInject);
        
        // 预初始化所有 SessionFactory，触发建表
        context.subWrapsOfType(DataSource.class, dsBw -> {
            HibernateAdapter adapter = HibernateAdapterManager.get(dsBw);
            if (adapter != null) {
                try {
                    // 创建 SessionFactory 会触发建表
                    adapter.getSessionFactory();
                    log.debug("Hibernate SessionFactory initialized for datasource: {}", dsBw.name());
                } catch (Exception e) {
                    log.error("Failed to initialize Hibernate SessionFactory for datasource: {}", dsBw.name(), e);
                }
            }
        });
    }

    private void persistenceContextInject(VarHolder vh, PersistenceContext anno) {
        String unitName = Utils.annoAlias(anno.unitName(), anno.name());

        DsUtils.observeDs(vh.context(), unitName, dsBw -> {
            HibernateAdapter adapter = HibernateAdapterManager.get(dsBw);

            if (adapter != null) {
                adapter.injectTo(vh);
            }
        });
    }

    private void persistenceUnitInject(VarHolder vh, PersistenceUnit anno) {
        String unitName = Utils.annoAlias(anno.unitName(), anno.name());

        DsUtils.observeDs(vh.context(), unitName, dsBw -> {
            HibernateAdapter adapter = HibernateAdapterManager.get(dsBw);

            if (adapter != null) {
                adapter.injectTo(vh);
            }
        });
    }

}
