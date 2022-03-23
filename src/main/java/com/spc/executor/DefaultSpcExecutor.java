package com.spc.executor;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;
import com.spc.handler.ParameterHandler;
import com.spc.handler.ResultHandler;
import com.spc.handler.StatementHandler;
import com.spc.parser.DefaultPlaceholderParser;
import com.spc.parser.VariableTokenHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DefaultSpcExecutor extends BaseSpcExecutor{

    public DefaultSpcExecutor(ResultHandler.ResultHandlerFactory resultHandlerFactory, ParameterHandler.ParameterHandlerFactory parameterHandlerFactory, StatementHandler.StatementHandlerFactory statementHandlerFactory) {
        super(resultHandlerFactory, parameterHandlerFactory, statementHandlerFactory);
    }

    @Override
    public <E> List<E> doQuery(Object[] argv) throws SQLException  {
        StatementHandler handler = statementHandlerFactory.create(mappedHolder, argv);
        Statement statement = prepareStatement(handler, argv);
        return handler.query(statement, resultHandlerFactory.create(methodMetadata));
    }

    private Statement prepareStatement(StatementHandler statementHandler, Object[] argv) throws SQLException {
        Statement statement = statementHandler.prepare(connection(), 100);
        statementHandler.parameterize(parameterHandlerFactory.create(mappedHolder, argv), statement);
        return statement;
    }


}
