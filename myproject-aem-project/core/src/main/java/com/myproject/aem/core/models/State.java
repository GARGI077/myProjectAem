package com.myproject.aem.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class State {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ValueMapValue
    private String stateDensity;

    @ValueMapValue
    private String statePopulation;

    @ValueMapValue
    private String stateName;

    @ValueMapValue
    private String statePostal;

    public String getStateDensity() {
        return stateDensity;
    }


    public void setStateDensity(String stateDensity) {
        this.stateDensity = stateDensity;
    }


    public String getStatePopulation() {
        return statePopulation;
    }


    public void setStatePopulation(String statePopulation) {
        this.statePopulation = statePopulation;
    }


    public String getStateName() {
        return stateName;
    }


    public void setStateName(String stateName) {
        this.stateName = stateName;
    }


    public String getStatePostal() {
        return statePostal;
    }


    public void setStatePostal(String statePostal) {
        this.statePostal = statePostal;
    }


    public Logger getLogger() {
        return logger;
    }

    @PostConstruct
    protected void init() {
        logger.debug("In init of State Model");
    }

}