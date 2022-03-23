package com.spc;

import com.spc.annotation.session.SqlSession;
import com.spc.executor.SpcExecutor;
import com.spc.core.SpcMethodMetadata;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class DefaultMethodHandler implements MethodHandler{

    private  AnnotationConfigApplicationContext context;

    private final SpcExecutor spcExecutor;

    private final SpcMethodMetadata methodMetadata;

    public DefaultMethodHandler(SpcExecutor spcExecutor, SpcMethodMetadata methodMetadata, AnnotationConfigApplicationContext context) {
        this.spcExecutor = spcExecutor;
        this.context = context;
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object invoke(Object[] argv) throws Throwable {
        prepareExecutor();
        return spcExecutor.queryList(argv);
    }

    private void prepareExecutor() {
        this.spcExecutor.initExecutor(methodMetadata, context);
    }
}
