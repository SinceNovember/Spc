package com.spc.context;

import com.spc.core.SpcMethodMetadata;
import com.spc.core.SqlCommand;
import com.spc.parser.DefaultPlaceholderParser;
import com.spc.parser.PlaceholderParser;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedHolder {

    private SpcMethodMetadata methodMetadata;

    private String sql;

    private String originSql;

    private SqlCommand sqlCommand;

    private Map<String, Integer> paramNameIndex;

    private Map<String, Integer> nameToIndex;

    private List<String> parsedParam = new ArrayList<>();

    private PlaceholderParser placeholderParser;

    public MappedHolder(SpcMethodMetadata methodMetadata, String sql, String originSql, SqlCommand sqlCommand, Map<String, Integer> indexToName) {
        this.methodMetadata = methodMetadata;
        this.sql = sql;
        this.originSql = originSql;
        this.sqlCommand = sqlCommand;
        this.paramNameIndex = indexToName;
    }

    public MappedHolder(Builder builder) {
        this.methodMetadata = builder.methodMetadata;
        this.originSql = methodMetadata.getSql();
        this.paramNameIndex = methodMetadata.getParamNameIndex();
        this.placeholderParser = builder.placeholderParser;
        this.nameToIndex = builder.nameToIndex;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private SpcMethodMetadata methodMetadata;

        private PlaceholderParser placeholderParser;

        private String sql;

        private String originSql;

        private SqlCommand sqlCommand;

        private Map<String, Integer> nameToIndex = new HashMap<>();

        public Builder methodMetadata(SpcMethodMetadata methodMetadata){
            this.methodMetadata = methodMetadata;
            return this;
        }

        public Builder placeholderParser(PlaceholderParser placeholderParser){
            this.placeholderParser = placeholderParser;
            return this;
        }

        public MappedHolder build(){
            return new MappedHolder(this);
        }

    }

    public void addNameToIndex(String name, Integer idx) {
        this.nameToIndex.put(name, idx);
    }

    public SpcMethodMetadata getMethodMetadata() {
        return methodMetadata;
    }

    public void setMethodMetadata(SpcMethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getOriginSql() {
        return originSql;
    }

    public void setOriginSql(String originSql) {
        this.originSql = originSql;
    }

    public SqlCommand getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(SqlCommand sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public Map<String, Integer> getParamNameIndex() {
        return paramNameIndex;
    }

    public PlaceholderParser getPlaceholderParser() {
        return placeholderParser;
    }

    public void setPlaceholderParser(PlaceholderParser placeholderParser) {
        this.placeholderParser = placeholderParser;
    }


    public Map<String, Integer> getNameToIndex() {
        return nameToIndex;
    }

    public void setNameToIndex(Map<String, Integer> nameToIndex) {
        this.nameToIndex = nameToIndex;
    }

    public void setParamNameIndex(Map<String, Integer> paramNameIndex) {
        this.paramNameIndex = paramNameIndex;
    }

    public List<String> getParsedParam() {
        return parsedParam;
    }

    public void setParsedParam(List<String> parsedParam) {
        this.parsedParam = parsedParam;
    }
}
