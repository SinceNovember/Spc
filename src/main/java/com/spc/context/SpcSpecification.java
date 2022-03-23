package com.spc.context;

import java.util.Arrays;
import java.util.Objects;

public class SpcSpecification implements SpcNameContextFactory.Specification {

    private String name;

    private Class<?>[] configuration;

    public SpcSpecification(String name, Class<?>[] configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConfiguration(Class<?>[] configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return configuration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configuration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpcSpecification that = (SpcSpecification) o;
        return Objects.equals(this.name, that.name)
                && Arrays.equals(this.configuration, that.configuration);
    }
}
