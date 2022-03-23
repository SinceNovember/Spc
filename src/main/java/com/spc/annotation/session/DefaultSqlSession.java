package com.spc.annotation.session;

import com.spc.executor.SpcExecutor;

import java.sql.Statement;
import java.util.List;

public class DefaultSqlSession implements SqlSession{

    private SpcExecutor executor;

    public DefaultSqlSession(SpcExecutor executor) {
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectOne(String statement) {
//        return executor.query;
        return null;
    }

    @Override
    public <E> List<E> selectOne(String statement, Object parameter) {
        return null;
    }
}
