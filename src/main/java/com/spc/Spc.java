package com.spc;

import com.spc.context.SpcContext;
import com.spc.executor.SpcExecutor;
import com.spc.core.SpcParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.lang.reflect.Method;

public abstract class Spc {

    public abstract <T> T proxyInstance(Class<?> clazz);

    public static Builder builder(){
        return new Builder();
    }

    public static String generateKey(Class<?> clazz, Method method) {
        String key = clazz.getSimpleName() + "#" + method.getName();
        return key;
    }

    static class Builder{

        protected SpcParser spcParser;

        protected SpcExecutor spcExecutor;

        protected AnnotationConfigApplicationContext context;

        protected MethodHandler.MethodHandlerFactory methodHandlerFactory = new MethodHandler.MethodHandlerFactory.BaseMethodHandlerFactory();;

//        protected DataSource dataSource;

        public Builder parser(SpcParser spcParser){
            this.spcParser = spcParser;
            return this;
        }

        public Builder executor(SpcExecutor spcExecutor) {
            this.spcExecutor = spcExecutor;
            return this;
        }

        public Builder context(AnnotationConfigApplicationContext context) {
            this.context = context;
            return this;
        }

//        public Builder dataSource(DataSource dataSource) {
//            this.dataSource = dataSource;
//            return this;
//        }

        public Builder handlerFactory(MethodHandler.MethodHandlerFactory methodHandlerFactory) {
            this.methodHandlerFactory = methodHandlerFactory;
            return this;
        }

        public <T> T proxy(Class<T> clazz, AnnotationConfigApplicationContext configApplicationContext) {
            return build().proxyInstance(clazz);
        }

        public Spc build(){
            return new ReflectiveSpc(this);
        }

    }
}
