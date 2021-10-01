package com.myproject.aem.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;


@Model(adaptables = Resource.class)
public class CountriesModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @Named("sling:resourceType")
    @Default(values = "No resourceType")
    protected String resourceType;

    @Inject
    @Optional
    private List<Resource> countries;

    private List<Country> countriesList = new ArrayList<>();

    public List<Country> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(List<Country> countriesList) {
        this.countriesList = countriesList;
    }
    @PostConstruct
    protected void init() {
        logger.debug("In init of CountriesModel");
        if (!countries.isEmpty()) {
            for (Resource resource : countries) {
                Country student = resource.adaptTo(Country.class);
                countriesList.add(student);
            }
        }
    }

}