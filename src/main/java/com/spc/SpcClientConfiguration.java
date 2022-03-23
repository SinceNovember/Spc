package com.spc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.spc.core.*;
import com.spc.executor.DefaultSpcExecutor;
import com.spc.executor.SpcExecutor;
import com.spc.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
public class SpcClientConfiguration {

    @Autowired(required = false)
    private List<AnnotationParameterProcessor> paramParameterProcessors;

    @Autowired(required = false)
    private List<ResultHandler> resultHandlers;

    @Autowired(required = false)
    private List<ParameterHandler> parameterHandlers;

    @Autowired(required = false)
    private List<StatementHandler> statementHandlers;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpcParser spcParser() {
        return new DefaultSpcParser(paramParameterProcessors);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpcExecutor spcExecutor(ParameterHandler.ParameterHandlerFactory parameterHandlerFactory,
                                   ResultHandler.ResultHandlerFactory resultHandlerFactory,
                                   StatementHandler.StatementHandlerFactory statementHandlerFactory) {
        return new DefaultSpcExecutor(resultHandlerFactory, parameterHandlerFactory, statementHandlerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public Spc.Builder spcBuilder(SpcParser spcParser, SpcExecutor spcExecutor) {
        return Spc.builder()
                .parser(spcParser)
                .executor(spcExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public ParameterHandler.ParameterHandlerFactory parameterHandlerFactory() {
        return new DefaultParameterHandlerFactory(parameterHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResultHandler.ResultHandlerFactory resultHandlerFactory() {
        return new DefaultResultHandlerFactory(resultHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public StatementHandler.StatementHandlerFactory statementHandlerFactory() {
        return new DefaultStatementHandlerFactory(statementHandlers);
    }


}
