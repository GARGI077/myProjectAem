package com.myproject.aem.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Simple Service Configuration",
        description = "This is a simple service configuration that I am trying to create")
public @interface SimpleServiceConfiguration {

    @AttributeDefinition(name = "path", description = "path for my products", type = AttributeType.STRING)
    public String service_path() default "/content";
}
