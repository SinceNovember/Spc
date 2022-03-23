package com.spc.core;

import com.spc.annotation.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ParamParameterProcessor implements AnnotationParameterProcessor {

    private static final Class<Param> ANNOTATION = Param.class;

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ANNOTATION;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        int index = context.getParameterIndex();
        SpcMethodMetadata methodMetadata = context.getSpcMethodMetadata();
        Class<?> parameterType = method.getParameterTypes()[index];
        Param param = ANNOTATION.cast(annotation);
        String name = param.value();
        context.setParameterName(name);
        return true;
    }
}
