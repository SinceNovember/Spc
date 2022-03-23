package com.spc.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class SpcMethodMetadata {

    private String methodKey;

    private String sql;

    private Map<String, Integer> paramNameIndex = new HashMap<>();

    private AnnotationConfigApplicationContext context;

    private Object[] args;

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public void setContext(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public void setMethodKey(String methodKey) {
        this.methodKey = methodKey;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Integer> getParamNameIndex() {
        return paramNameIndex;
    }

    public void setParamNameIndex(Map<String, Integer> paramNameIndex) {
        this.paramNameIndex = paramNameIndex;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


}
