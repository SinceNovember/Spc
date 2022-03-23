package com.spc.executor;

import com.spc.context.MappedHolder;
import com.spc.context.SpcExecutorContext;
import com.spc.core.SpcMethodMetadata;
import com.spc.event.SqlExecuteEvent;
import com.spc.handler.ParameterHandler;
import com.spc.handler.ResultHandler;
import com.spc.handler.StatementHandler;
import com.spc.parser.DefaultPlaceholderParser;
import com.spc.parser.VariableTokenHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.MethodMetadata;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public abstract class BaseSpcExecutor implements SpcExecutor {

    protected AnnotationConfigApplicationContext context;

    protected ResultHandler.ResultHandlerFactory resultHandlerFactory;

    protected ParameterHandler.ParameterHandlerFactory parameterHandlerFactory;

    protected StatementHandler.StatementHandlerFactory statementHandlerFactory;

    protected DataSource dataSource;

    protected SpcMethodMetadata methodMetadata;

    protected String name;

    protected MappedHolder mappedHolder;

    public abstract <E> List<E> doQuery(Object[] argv) throws SQLException;


    public BaseSpcExecutor(ResultHandler.ResultHandlerFactory resultHandlerFactory, ParameterHandler.ParameterHandlerFactory parameterHandlerFactory, StatementHandler.StatementHandlerFactory statementHandlerFactory) {
        this.resultHandlerFactory = resultHandlerFactory;
        this.parameterHandlerFactory = parameterHandlerFactory;
        this.statementHandlerFactory = statementHandlerFactory;
    }

    @Override
    public void initExecutor(SpcMethodMetadata methodMetadata, AnnotationConfigApplicationContext context) {
        this.dataSource = context.getBean(DataSource.class);
        this.context = context;
        name = context.getDisplayName();
        buildMapped(methodMetadata);
    }

    private void buildMapped(SpcMethodMetadata methodMetadata) {
        this.mappedHolder = MappedHolder.builder()
                .methodMetadata(methodMetadata).build();
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }


    @Override
    public Connection connection() throws SQLException {

        return dataSource().getConnection();
    }

    @Override
    public Statement statement() throws SQLException {
        return connection().createStatement();
    }

    @Override
    public void close() throws SQLException {
        connection().close();
    }

    @Override
    public Object execute(MappedHolder mappedHolder , Object[] argv) {
        return null;
    }

    @Override
    public <E> List<E> queryList(Object[] argv) throws SQLException {
        long startTime = System.nanoTime();
        //先解析sql
        parseSql();
        List<E> list = doQuery(argv);
        long endTime = System.nanoTime();
        context.publishEvent(new SqlExecuteEvent(this, new SqlExecuteEvent.SqlExecuteRecord(endTime - startTime, (List<Object>) list)));
        return list;
    }

    private void parseSql() {
        VariableTokenHandler handler = new VariableTokenHandler(mappedHolder.getParsedParam());
        DefaultPlaceholderParser parser = new DefaultPlaceholderParser("#{", "}", handler);
        String parsedSql = parser.parse(mappedHolder.getOriginSql());
        mappedHolder.setSql(parsedSql);
    }


    @Override
    public String toString() {
        return "BaseSpcExecutor{" +
                "context=" + context +
                ", dataSource=" + dataSource +
                ", methodMetadata=" + methodMetadata +
                ", name='" + name + '\'' +
                '}';
    }
}
