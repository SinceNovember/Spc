package com.spc;

import com.spc.annotation.EnableSpcClients;
import com.spc.annotation.SpcClient;
import com.spc.context.SpcSpecification;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

public class SpcClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerSpcClients(importingClassMetadata, registry);
    }


    /**
     * 注册Spc客户端到Bean中
     *
     * @param metadata
     * @param registry
     */
    private void registerSpcClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(resourceLoader);

        Set<String> basePackages;

        //找出需要扫描的包路径
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableSpcClients.class.getName());
        AnnotationTypeFilter typeFilter = new AnnotationTypeFilter(SpcClient.class);
        final Class<?>[] clients = attrs == null ? null
                : (Class<?>[]) attrs.get("clients");
        if (clients == null || clients.length == 0) {
            scanner.addIncludeFilter(typeFilter);
            basePackages = getBasePackages(metadata);
        } else {
            final Set<String> clientClasses = new HashSet<>();
            basePackages = new HashSet<>();
            for (Class<?> client : clients) {
                basePackages.add(ClassUtils.getPackageName(client));
                clientClasses.add(client.getCanonicalName());
                AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {
                    @Override
                    protected boolean match(ClassMetadata metadata) {
                        String cleaned = metadata.getClassName().replaceAll("\\$", ".");
                        return clientClasses.contains(cleaned);
                    }
                };
                scanner.addIncludeFilter(new AllTypeFilter(Arrays.asList(typeFilter, filter)));
            }
        }

        for (String pkg : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(pkg);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(SpcClient.class.getCanonicalName());
                    String name = (String) attributes.get("name");
                    registerSpcConfig(registry, name, attributes.get("datasourceConfig"));
                    registerSpcClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerSpcConfig(BeanDefinitionRegistry registry, String name, Object configuration) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpcSpecification.class);
        beanDefinitionBuilder.addConstructorArgValue(name);
        beanDefinitionBuilder.addConstructorArgValue(configuration);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinitionBuilder.getBeanDefinition(),  name + "." + SpcSpecification.class.getSimpleName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void registerSpcClient(BeanDefinitionRegistry registry, AnnotationMetadata metadata, Map<String, Object> attr) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpcClientFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("name", attr.get("name"));
        beanDefinitionBuilder.addPropertyValue("type", metadata.getClassName());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinitionBuilder.getBeanDefinition(), metadata.getClassName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableSpcClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attrs.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        for (String pkg : (String[]) attrs.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        for (Class pkg : (Class<?>[]) attrs.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(pkg));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;


    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 判断类别是否匹配全部过滤条件
     */
    public static class AllTypeFilter implements TypeFilter {

        private List<TypeFilter> delegates;

        public AllTypeFilter(List<TypeFilter> delegates) {
            this.delegates = delegates;
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            for (TypeFilter typeFilter : delegates) {
                if (!typeFilter.match(metadataReader, metadataReaderFactory)) {
                    return false;
                }
            }
            return true;
        }
    }

}
