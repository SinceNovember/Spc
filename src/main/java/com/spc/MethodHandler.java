package com.spc;

import com.spc.executor.SpcExecutor;
import com.spc.core.SpcMethodMetadata;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public interface MethodHandler {

    Object invoke(Object[] argv) throws Throwable;

    interface MethodHandlerFactory{

        MethodHandler create(SpcExecutor spcExecutor, SpcMethodMetadata methodMetadata, AnnotationConfigApplicationContext context);

        class BaseMethodHandlerFactory implements MethodHandlerFactory{

            @Override
            public MethodHandler create(SpcExecutor spcExecutor, SpcMethodMetadata methodMetadata, AnnotationConfigApplicationContext context) {
                return new DefaultMethodHandler(spcExecutor, methodMetadata, context);
            }
        }
    }

}
