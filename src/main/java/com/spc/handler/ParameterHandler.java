package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {

    void setParameters(PreparedStatement ps)
            throws SQLException;

    interface ParameterHandlerFactory{

        ParameterHandler create(MappedHolder holder, Object[] argv);

    }
}
