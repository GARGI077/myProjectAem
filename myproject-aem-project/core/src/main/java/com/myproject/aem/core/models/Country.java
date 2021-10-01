package com.myproject.aem.core.models;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Country.
 */
@Model(adaptables = Resource.class)
public class Country {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ValueMapValue
    private String stateName;

    @ValueMapValue
    private List<Resource> states;

    @ValueMapValue
    private List<State> stateList = new ArrayList<>();


    public List<State> getStateList() {
        return stateList;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }


    @PostConstruct
    protected void init() {
        logger.debug("In init method of Country model.");
        if(!states.isEmpty()) {
            for (Resource resource : states) {
                State state = resource.adaptTo(State.class);
                stateList.add(state);
            }
        }
    }

}
