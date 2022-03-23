package com.spc.handler;

import com.spc.context.MappedHolder;
import com.spc.core.SpcMethodMetadata;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DefaultParameterHandler implements ParameterHandler{

    private MappedHolder mappedHolder;

    private Object[] argv;

    public DefaultParameterHandler(MappedHolder mappedHolder, Object[] argv) {
        this.mappedHolder = mappedHolder;
        this.argv = argv;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        for (int idx = 0; idx < mappedHolder.getParsedParam().size(); idx++) {
            String name = mappedHolder.getParsedParam().get(idx);
            int paramIdx = mappedHolder.getParamNameIndex().get(name);
            ps.setObject(idx + 1, argv[paramIdx]);
        }
    }
}
