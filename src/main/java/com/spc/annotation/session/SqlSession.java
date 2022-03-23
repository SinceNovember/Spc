package com.spc.annotation.session;

import java.sql.Statement;
import java.util.List;

public interface SqlSession {

    <E> List<E> selectOne(String statement);

    <E> List<E> selectOne(String statement, Object parameter);

}
