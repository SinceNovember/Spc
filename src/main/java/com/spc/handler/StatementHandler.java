package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    Statement prepare(Connection connection, Integer transactionTimeout)
            throws SQLException;

    void parameterize(ParameterHandler parameterHandler, Statement statement)
            throws SQLException;

    void parameterHandler(ParameterHandler parameterHandler);

    <E> List<E> query(Statement statement, ResultHandler resultHandler)
            throws SQLException;

    interface StatementHandlerFactory{
        StatementHandler create(MappedHolder mappedHolder, Object[] argv);

    }

}
