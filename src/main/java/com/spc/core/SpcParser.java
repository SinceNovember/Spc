package com.spc.core;


import com.spc.MethodHandler;
import com.spc.Spc;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface SpcParser {

    Map<String, SpcMethodMetadata> parseSpcClient(Class<?> clazz);

    abstract class BaseParser  implements SpcParser{

        private ApplicationContext applicationContext;

        private List<SpcMethodMetadata> methods = new ArrayList<>();

        abstract void processAnnotationOnMethod(SpcMethodMetadata methodMetadata,  Annotation annotation, Method method);

        abstract void processAnnotationOnParam(SpcMethodMetadata methodMetadata, Method method, Annotation[] annotations, int index);


        @Override
        public Map<String, SpcMethodMetadata> parseSpcClient(Class<?> clazz) {
            List<SpcMethodMetadata> metadata = parseMethodMetadata(clazz);
            return metadata.stream()
                    .collect(Collectors.toMap(v -> v.getMethodKey(), Function.identity()));
        }

        private List<SpcMethodMetadata> parseMethodMetadata(Class<?> clazz) {
            List<SpcMethodMetadata> methodMetadataList = new ArrayList<>();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() == Object.class ||
                        (method.getModifiers() & Modifier.STATIC) != 0 ||
                        isDefault(method)) {
                    continue;
                }
                methodMetadataList.add(parseMethodMetadata(clazz, method));
            }
            return methodMetadataList;
        }

        /**
         * 解析方法
         * @param clazz
         * @param method
         * @return
         */
        private SpcMethodMetadata parseMethodMetadata(Class<?> clazz, Method method) {
            SpcMethodMetadata methodMetadata = new SpcMethodMetadata();
            methodMetadata.setMethodKey(Spc.generateKey(clazz, method));
            for (Annotation annotation : method.getAnnotations()) {
                //处理方法上的注解
                processAnnotationOnMethod(methodMetadata, annotation, method);
            }
            Class<?>[] params = method.getParameterTypes();
            Type[] types = method.getGenericParameterTypes();

            Annotation[][] paramAnnotations = method.getParameterAnnotations();
            int count = paramAnnotations.length;
            for (int i = 0; i < count; i++) {
                if (paramAnnotations[i] != null) {
                    //处理方法上的参数注解
                    processAnnotationOnParam(methodMetadata, method, paramAnnotations[i], i);
                }
            }

            return methodMetadata;

        }

        private boolean isDefault(Method method) {
            // Default methods are public non-abstract, non-synthetic, and non-static instance methods
            // declared in an interface.
            // method.isDefault() is not sufficient for our usage as it does not check
            // for synthetic methods. As a result, it picks up overridden methods as well as actual default
            // methods.
            final int SYNTHETIC = 0x00001000;
            return ((method.getModifiers()
                    & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) == Modifier.PUBLIC)
                    && method.getDeclaringClass().isInterface();
        }

        public void nameParam(SpcMethodMetadata methodMetadata, String name, int index) {
            methodMetadata.getParamNameIndex().put(name, index);
        }

    }

    class Default extends BaseParser {

        @Override
        void processAnnotationOnMethod(SpcMethodMetadata methodMetadata, Annotation annotation, Method method) {

        }

        @Override
        void processAnnotationOnParam(SpcMethodMetadata methodMetadata, Method method, Annotation[] annotations, int index) {

        }

    }

}
