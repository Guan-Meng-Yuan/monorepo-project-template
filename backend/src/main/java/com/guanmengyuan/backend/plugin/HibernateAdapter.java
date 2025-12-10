package com.guanmengyuan.backend.plugin;

import java.util.Properties;

import javax.security.auth.login.Configuration;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.noear.solon.Solon;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;
import org.noear.solon.core.VarHolder;
import org.noear.solon.core.util.ResourceUtil;

import jakarta.persistence.EntityManagerFactory;

/**
 * @author lingkang
 * @since 2.5
 */
public class HibernateAdapter {
    protected BeanWrap dsWrap;
    protected Props dsProps;

    protected HibernateConfiguration configuration;

    public HibernateAdapter(BeanWrap dsWrap) {
        this(dsWrap, Solon.cfg().getProp("jpa"));
    }

    public HibernateAdapter(BeanWrap dsWrap, Props dsProps) {
        this.dsWrap = dsWrap;
        // 如果传入的 dsProps 为 null，尝试从配置中获取
        if (dsProps == null) {
            String dsName = dsWrap.name();
            if (dsName != null && !dsName.isEmpty()) {
                this.dsProps = Solon.cfg().getProp("jpa." + dsName);
            } else {
                this.dsProps = Solon.cfg().getProp("jpa.default");
            }
            // 如果还是 null，使用空的 Props
            if (this.dsProps == null) {
                this.dsProps = Solon.cfg().getProp("jpa");
            }
        } else {
            this.dsProps = dsProps;
        }

        DataSource dataSource = getDataSource();

        configuration = new HibernateConfiguration();
        configuration.setDataSource(dataSource);

        initConfiguration();

        initDo();
    }

    protected DataSource getDataSource() {
        return dsWrap.raw();
    }

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (this) {
                if (sessionFactory == null) {
                    // 构建 SessionFactory 时会触发建表
                    sessionFactory = getConfiguration().buildSessionFactory();
                }
            }
        }

        return sessionFactory;
    }

    public HibernateConfiguration getConfiguration() {
        return configuration;
    }


    /**
     * @author bai
     * */
    protected void initConfiguration() {
        // 默认兼容 hibernate.cfg.xml
        if (ResourceUtil.hasResource(null, StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME)){
            configuration.configure(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME );
        }
        // 加载hibernate常规设置
        Props propertiesProps = this.dsProps.getProp("properties");
        if (propertiesProps != null) {
            Properties hibernateProperties = convertPropsToProperties(propertiesProps);
            getConfiguration().setProperties(hibernateProperties);
        }
    }

    /**
     * 将 Solon Props 转换为 Java Properties，正确处理嵌套属性名
     */
    private Properties convertPropsToProperties(Props props) {
        Properties properties = new Properties();
        if (props != null) {
            convertPropsToPropertiesRecursive(props, "", properties);
        }
        return properties;
    }

    /**
     * 递归转换 Props 为 Properties，处理嵌套结构
     */
    private void convertPropsToPropertiesRecursive(Props props, String prefix, Properties properties) {
        if (props == null) {
            return;
        }
        
        props.forEach((key, value) -> {
            if (key instanceof String) {
                String propKey = (String) key;
                String fullKey = prefix.isEmpty() ? propKey : prefix + "." + propKey;
                
                if (value instanceof Props) {
                    // 如果是嵌套的 Props，递归处理
                    convertPropsToPropertiesRecursive((Props) value, fullKey, properties);
                } else if (value != null) {
                    // 普通值，直接设置
                    properties.setProperty(fullKey, value.toString());
                }
            }
        });
    }

    protected void initDo() {
        //for mappers section
        if (dsProps != null) {
            // 处理 mappings 数组配置
            Object mappingsObj = dsProps.get("mappings");
            if (mappingsObj != null) {
                if (mappingsObj instanceof String) {
                    // 单个字符串或多个用逗号分隔
                    String valStr = (String) mappingsObj;
                    for (String val : valStr.split(",")) {
                        val = val.trim();
                        if (val.length() > 0) {
                            getConfiguration().addMapping(val);
                        }
                    }
                } else if (mappingsObj instanceof Iterable) {
                    // 数组格式
                    Iterable<?> mappings = (Iterable<?>) mappingsObj;
                    for (Object mapping : mappings) {
                        if (mapping != null) {
                            String val = mapping.toString().trim();
                            if (val.length() > 0) {
                                getConfiguration().addMapping(val);
                            }
                        }
                    }
                }
            }
            
            // 兼容旧的配置格式 mappings[0], mappings[1] 等
            dsProps.forEach((k, v) -> {
                if (k instanceof String && v instanceof String) {
                    String key = (String) k;
                    String valStr = (String) v;

                    if (key.startsWith("mappings[") && key.endsWith("]")) {
                        valStr = valStr.trim();
                        if (valStr.length() > 0) {
                            getConfiguration().addMapping(valStr);
                        }
                    }
                }
            });
        }
    }

    protected void injectTo(VarHolder vh) {
        Class<?> type = vh.getType();
        
        if (SessionFactory.class.isAssignableFrom(type)) {
            vh.setValue(getSessionFactory());
            return;
        }

        if (Configuration.class.isAssignableFrom(type)) {
            vh.setValue(getConfiguration());
            return;
        }

        if (EntityManagerFactory.class.isAssignableFrom(type)) {
            vh.setValue(getSessionFactory());
            return;
        }
    }
}