package com.myproject.aem.core.models;

import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;


@ConsumerType
public interface TitleModel {

    String getHeading();

    List<Resource> getListOfItems();

}
