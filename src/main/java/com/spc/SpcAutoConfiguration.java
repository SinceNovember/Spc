package com.spc;

import com.spc.context.SpcContext;
import com.spc.context.SpcSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpcAutoConfiguration {

    @Autowired(required = false)
    private List<SpcSpecification> configuration = new ArrayList<>();

    @Bean
    public SpcContext spcContext() {
        SpcContext context = new SpcContext();
        context.setConfigurations(configuration);
        return context;
    }
}
