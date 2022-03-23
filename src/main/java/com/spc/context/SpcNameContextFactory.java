package com.spc.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SpcNameContextFactory<C extends SpcNameContextFactory.Specification> implements DisposableBean, ApplicationContextAware {

    private final String propertyName;

    private final String propertySourceName;

    private ApplicationContext parent;

    private Map<String, AnnotationConfigApplicationContext> contexts = new ConcurrentHashMap<>();

    private Map<String, C> configurations = new ConcurrentHashMap<>();

    private Class<?> defaultConfigType;


    public SpcNameContextFactory(Class<?> defaultConfigType, String propertyName, String propertySourceName) {
        this.defaultConfigType = defaultConfigType;
        this.propertyName = propertyName;
        this.propertySourceName = propertySourceName;
    }

    public AnnotationConfigApplicationContext getContext(String name) {
        if (!this.contexts.containsKey(name)) {
            synchronized (this.contexts) {
                this.contexts.put(name, createContext(name));
            }
        }
        return this.contexts.get(name);
    }

    /**
     * 创建一个子容器
     *
     * @param name 容器名
     * @return
     */
    protected AnnotationConfigApplicationContext createContext(String name) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //注入自定义容器的配置
        if (this.configurations.containsKey(name)) {
            for (Class<?> configuration : this.configurations.get(name).getConfiguration()) {
                context.register(configuration);
            }
        }

        //注入default开头的配置类
        for (Map.Entry<String, C> entry : this.configurations.entrySet()) {
            if (entry.getKey().startsWith("default.")) {
                for (Class<?> configuration : entry.getValue().getConfiguration()) {
                    context.register(configuration);
                }
            }
        }
        //默认的配置类注入
        context.register(PropertyPlaceholderAutoConfiguration.class, ConfigurationPropertiesBindingPostProcessor.class,
                this.defaultConfigType);
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(
                this.propertySourceName,
                Collections.<String, Object>singletonMap(this.propertyName, name)));
        if (parent != null) {
            context.setParent(parent);
            context.setClassLoader(parent.getClassLoader());
        }
        context.setDisplayName(generateDisplayName(name));
        context.refresh();
        return context;
    }

    protected String generateDisplayName(String name) {
        return this.getClass().getSimpleName() + "-" + name;
    }


    /**
     * 获取子容器对应的bean
     *
     * @param name
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getInstance(String name, Class<T> type) {
        AnnotationConfigApplicationContext context = getContext(name);
        if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, type).length > 0) {
            return context.getBean(type);
        }
        return null;
    }

    public <T> Map<String, T> getInstances(String name, Class<T> type) {
        AnnotationConfigApplicationContext context = getContext(name);
        if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, type).length > 0) {
            return BeanFactoryUtils.beansOfTypeIncludingAncestors(context, type);
        }
        return null;
    }

    public DataSource getDatasource(String name) {
        return getInstance(name, DataSource.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext;
    }

    @Override
    public void destroy() {
        this.contexts.values()
                .forEach(AnnotationConfigApplicationContext::close);
        this.contexts.clear();
    }

    public String getPropertyName() {
        return propertyName;
    }



    public String getPropertySourceName() {
        return propertySourceName;
    }



    public ApplicationContext getParent() {
        return parent;
    }

    public void setParent(ApplicationContext parent) {
        this.parent = parent;
    }

    public Map<String, AnnotationConfigApplicationContext> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, AnnotationConfigApplicationContext> contexts) {
        this.contexts = contexts;
    }

    public Map<String, C> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<C> configurations) {
        for (C configuration : configurations) {
            this.configurations.put(configuration.getName(), configuration);
        }
    }

    public Class<?> getDefaultConfigType() {
        return defaultConfigType;
    }

    public void setDefaultConfigType(Class<?> defaultConfigType) {
        this.defaultConfigType = defaultConfigType;
    }

    public interface Specification {

        String getName();

        Class<?>[] getConfiguration();
    }
}
