package com.myproject.aem.core.models;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

public class BreakLineTextImpl implements BreakLineText {

    @ValueMapValue
    String text;

    @Override
    public String getHeading() {
        return text;
    }
}
