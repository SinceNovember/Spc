package com.spc.core;

import com.spc.annotation.Sql;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class DefaultSpcParser extends SpcParser.BaseParser {

    private Map<Class<? extends Annotation>, AnnotationParameterProcessor> annotatedArgumentProcessors;

    private final Map<String, Method> processedMethods = new HashMap<>();

    public DefaultSpcParser(){
        this(Collections.emptyList());
    }

    public DefaultSpcParser(List<AnnotationParameterProcessor> annotationParameterProcessors) {
        List<AnnotationParameterProcessor> processors;
        if (!CollectionUtils.isEmpty(annotationParameterProcessors)) {
            processors = new ArrayList<>(annotationParameterProcessors);
        } else {
            processors = getDefaultParameterProcessors();
        }
        this.annotatedArgumentProcessors = toAnnotatedArgumentProcessorMap(processors);
    }

    private Map<Class<? extends Annotation>, AnnotationParameterProcessor> toAnnotatedArgumentProcessorMap(List<AnnotationParameterProcessor> annotationParameterProcessors) {
        Map<Class<? extends Annotation>, AnnotationParameterProcessor> annotatedArgumentProcessorMap = new HashMap<>();
        for (AnnotationParameterProcessor annotationParameterProcessor : annotationParameterProcessors) {
            annotatedArgumentProcessorMap.put(annotationParameterProcessor.getAnnotationType(), annotationParameterProcessor);
        }
        return annotatedArgumentProcessorMap;
    }

    private List<AnnotationParameterProcessor> getDefaultParameterProcessors() {
        List<AnnotationParameterProcessor> processors = new ArrayList<>();
        processors.add(new ParamParameterProcessor());
        return processors;
    }

    @Override
    void processAnnotationOnMethod(SpcMethodMetadata methodMetadata, Annotation annotation, Method method) {
        if (!Sql.class.isInstance(annotation) && annotation.annotationType().isAnnotationPresent(Sql.class)) {
            return;
        }
        //获取方法上的注解
        Sql sqlAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, Sql.class);
        String sql = sqlAnnotation.value();
        processAndValidateSql(sql);
        methodMetadata.setSql(sql);
    }

    /**
     * 处理参数的注解
     * @param methodMetadata
     * @param method
     * @param annotations
     * @param index
     */
    @Override
    void processAnnotationOnParam(SpcMethodMetadata methodMetadata, Method method, Annotation[] annotations, int index) {
        AnnotationParameterProcessor.AnnotatedParameterContext context = new DefaultAnnotatedParameterContext(methodMetadata, index);
        for (Annotation annotation : annotations) {
            //根据注解类型查找对应的注解处理器
            AnnotationParameterProcessor annotationParameterProcessor = annotatedArgumentProcessors.get(annotation.annotationType());
            if (annotationParameterProcessor != null) {
                annotationParameterProcessor.processArgument(context, annotation, method);
            }
        }
    }

    private void processAndValidateSql(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return;
        }

    }

    private class DefaultAnnotatedParameterContext implements AnnotationParameterProcessor.AnnotatedParameterContext {

        private final SpcMethodMetadata spcMethodMetadata;

        private final int parameterIndex;


        public DefaultAnnotatedParameterContext(SpcMethodMetadata spcMethodMetadata, int parameterIndex) {
            this.spcMethodMetadata = spcMethodMetadata;
            this.parameterIndex = parameterIndex;
        }

        @Override
        public SpcMethodMetadata getSpcMethodMetadata() {
            return null;
        }

        @Override
        public int getParameterIndex() {
            return 0;
        }

        /**
         * 将参数与索引对应关系存放到元数据中
         * @param name
         */
        @Override
        public void setParameterName(String name) {
            nameParam(spcMethodMetadata, name, parameterIndex);
        }
    }
}
