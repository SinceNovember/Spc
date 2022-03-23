package com.spc.annotation.session;

import com.spc.executor.SpcExecutor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    @Override
    public SqlSession openSession(AnnotationConfigApplicationContext applicationContext) {
        SpcExecutor executor = applicationContext.getBean(SpcExecutor.class);
        return new DefaultSqlSession(executor);
    }

}
