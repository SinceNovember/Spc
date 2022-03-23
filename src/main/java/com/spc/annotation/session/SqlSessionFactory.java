package com.spc.annotation.session;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

public interface SqlSessionFactory {

    SqlSession openSession(AnnotationConfigApplicationContext applicationContext);
}
