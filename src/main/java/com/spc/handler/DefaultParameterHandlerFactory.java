package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;

import java.util.List;

public class DefaultParameterHandlerFactory implements ParameterHandler.ParameterHandlerFactory{

    private List<ParameterHandler> resultHandlers;

    public DefaultParameterHandlerFactory(List<ParameterHandler> resultHandlers) {
        this.resultHandlers = resultHandlers;
    }

    @Override
    public ParameterHandler create(MappedHolder holder, Object[] argv) {
        return new DefaultParameterHandler(holder, argv);
    }

}
