package com.spc.handler;

import com.spc.core.SpcMethodMetadata;

import java.util.List;

public class DefaultResultHandlerFactory implements ResultHandler.ResultHandlerFactory {

    private List<ResultHandler> resultHandlers;

    public DefaultResultHandlerFactory(List<ResultHandler> resultHandlers) {
        this.resultHandlers = resultHandlers;
    }

    @Override
    public ResultHandler create(SpcMethodMetadata methodMetadata) {
        return new DefaultResultHandler();
    }
}
