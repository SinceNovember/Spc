package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultStatementHandlerFactory implements StatementHandler.StatementHandlerFactory{

    private  final List<StatementHandler> statementHandlers;

    public DefaultStatementHandlerFactory(List<StatementHandler> statementHandlers) {
        this.statementHandlers = statementHandlers;
    }

    @Override
    public StatementHandler create(MappedHolder mappedHolder, Object[] argv) {
        return new PrepareStatementHandler(mappedHolder, argv);

    }

}
