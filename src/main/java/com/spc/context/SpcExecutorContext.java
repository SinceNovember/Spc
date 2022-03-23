package com.spc.context;

import com.spc.core.SpcMethodMetadata;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

public class SpcExecutorContext {

    private SpcMethodMetadata methodMetadata;

    private DataSource dataSource;

    private AnnotationConfigApplicationContext applicationContext;

    private Connection connection;

    public SpcMethodMetadata getMethodMetadata() {
        return methodMetadata;
    }

    public void setMethodMetadata(SpcMethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AnnotationConfigApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(AnnotationConfigApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
