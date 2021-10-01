package com.myproject.aem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, adapters = TitleModel.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class TitleModelImpl implements TitleModel {


    @ChildResource
    private List<Resource> listOfItems;

    @ValueMapValue
    private String heading;

    @PostConstruct
    protected void init() {
        if (heading != null && !heading.isEmpty()) {
            heading = heading.toUpperCase();
        }
    }

    @Override
    public String getHeading() {
        return heading;
    }


    @Override
    public List<Resource> getListOfItems() {
        return new ArrayList<>(listOfItems);
    }

}
