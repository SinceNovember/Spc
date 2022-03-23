package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;
import com.spc.parser.DefaultPlaceholderParser;
import com.spc.parser.VariableTokenHandler;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class PrepareStatementHandler implements StatementHandler {

    private MappedHolder mappedHolder;

    private Object[] argv;

    public PrepareStatementHandler(MappedHolder mappedHolder, Object[] argv) {
        this.mappedHolder = mappedHolder;
        this.argv = argv;
    }

    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(mappedHolder.getSql());
        return statement;
    }



    @Override
    public void parameterize(ParameterHandler parameterHandler, Statement statement) throws SQLException {
        parameterHandler.setParameters((PreparedStatement)statement);
    }

    @Override
    public void parameterHandler(ParameterHandler parameterHandler) {
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.executeQuery();
        return resultHandler.handlerResult(statement);

    }

}
