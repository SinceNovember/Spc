package com.spc;

import com.spc.executor.DefaultSpcExecutor;
import com.spc.executor.SpcExecutor;
import com.spc.core.SpcMethodMetadata;
import com.spc.core.SpcParser;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveSpc extends Spc{

    private SpcParser spcParser;

    private SpcExecutor spcExecutor;

    private MethodHandler.MethodHandlerFactory methodHandlerFactory;

    private AnnotationConfigApplicationContext context;

    ReflectiveSpc(SpcParser spcParser, SpcExecutor spcExecutor, MethodHandler.MethodHandlerFactory methodHandlerFactory ) {
        this.spcParser = spcParser;
        this.spcExecutor = spcExecutor;
        this.methodHandlerFactory = methodHandlerFactory;
    }

    ReflectiveSpc(Builder builder) {
        this.spcParser = builder.spcParser;
        this.spcExecutor = builder.spcExecutor;
        this.methodHandlerFactory = builder.methodHandlerFactory;
        this.context = builder.context;
    }

    @Override
    public <T> T   proxyInstance(Class<?> clazz) {
        Map<String, SpcMethodMetadata> nameToMetadata = spcParser.parseSpcClient(clazz);
        Map<Method, MethodHandler> methodToHandler = new HashMap<>();
        for (Method method : clazz.getMethods()) {
            SpcMethodMetadata methodMetadata = nameToMetadata.get(generateKey(clazz, method));
            methodMetadata.setContext(context);
            MethodHandler handler = methodHandlerFactory.create(spcExecutor, methodMetadata, context);
            methodToHandler.put(method, handler);

        }
        InvocationHandler invocationHandler = new SpcInvocationHandler(clazz, methodToHandler);
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
    }

    static class SpcInvocationHandler implements InvocationHandler {

        private Class<?> clazz;
        private Map<Method, MethodHandler> methodHandlerMap;

        public SpcInvocationHandler(Class<?> clazz, Map<Method, MethodHandler> methodHandlerMap) {
            this.clazz = clazz;
            this.methodHandlerMap = methodHandlerMap;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodHandler proxyMethod = methodHandlerMap.get(method);
            return  proxyMethod.invoke(args);
        }
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SpcInvocationHandler) {
                SpcInvocationHandler other = (SpcInvocationHandler) obj;
                return clazz.equals(other.clazz);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }

}
