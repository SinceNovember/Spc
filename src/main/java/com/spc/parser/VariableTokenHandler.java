package com.spc.parser;

import com.spc.context.MappedHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableTokenHandler implements TokenHandler{

    private final List<String> parsedParam;

    public VariableTokenHandler(List<String> parsedParam) {
        this.parsedParam = parsedParam;
    }

    @Override
    public String handleToken(String content) {
        parsedParam.add(content);
        return "?";
    }
}
