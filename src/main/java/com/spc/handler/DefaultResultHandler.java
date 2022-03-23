package com.spc.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultHandler implements ResultHandler{
    @Override
    public List<Object> handlerResult(Statement statement) throws SQLException {
        List<Object> results = new ArrayList<>();
        PreparedStatement ps = (PreparedStatement)statement;
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            results.add(rs.getObject(1));
        }
        return results;
    }
}
