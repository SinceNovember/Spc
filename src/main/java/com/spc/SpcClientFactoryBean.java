package com.spc;

import com.spc.context.SpcContext;
import com.spc.core.SpcParser;
import com.spc.executor.SpcExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

public class SpcClientFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private Class<?> type;

    private String name;

    private String datasourceName;

    private DataSource dataSource;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        SpcContext spcContext = applicationContext.getBean(SpcContext.class);
        AnnotationConfigApplicationContext context = spcContext.getContext(name);
        Spc.Builder builder = get(spcContext, Spc.Builder.class);
        builder.context(context);
        //                .dataSource(get(spcContext, DataSource.class))
//        Spc spc = Spc.builder().executor(spcExecutor).build();
        return builder.proxy(type, context);
    }

    private <T> T get(SpcContext context, Class<T> clazz) {
        return context.getInstance(name, clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
