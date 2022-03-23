package com.spc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationParameterProcessor {

    Class<? extends Annotation> getAnnotationType();

    boolean processArgument(AnnotatedParameterContext context, Annotation annotation,
                            Method method);

    interface AnnotatedParameterContext {

        SpcMethodMetadata getSpcMethodMetadata();

        int getParameterIndex();

        void setParameterName(String name);
    }

}
