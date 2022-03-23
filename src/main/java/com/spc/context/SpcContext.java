package com.spc.context;

import com.spc.SpcClientConfiguration;

public class SpcContext extends SpcNameContextFactory<SpcSpecification>{

    public SpcContext() {
        super(SpcClientConfiguration.class, "spc", "spc.client.name");
    }
}
