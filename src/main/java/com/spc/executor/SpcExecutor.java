package com.spc.executor;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.MethodMetadata;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface SpcExecutor {

    DataSource dataSource();


    Connection connection() throws SQLException;

    Statement statement() throws SQLException;

    void close() throws SQLException;

    Object execute(MappedHolder mappedHolder, Object[] argv);

    void initExecutor(SpcMethodMetadata methodMetadata, AnnotationConfigApplicationContext applicationContext);

    <E> List<E> queryList(Object[] argv) throws SQLException;

}
