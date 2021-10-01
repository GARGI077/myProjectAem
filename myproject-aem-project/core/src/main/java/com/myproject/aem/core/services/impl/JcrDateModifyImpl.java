package com.myproject.aem.core.services.impl;


import com.myproject.aem.core.services.JcrDateModify;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component( immediate = true ,service={JcrDateModify.class} ,
        property ={ Constants.SERVICE_VENDOR +"="+"gargi_user"})

public class JcrDateModifyImpl implements JcrDateModify {


    private static final Logger log= LoggerFactory.getLogger(JcrDateModifyImpl.class);

    @Reference
    private transient ResourceResolverFactory resourceResolverFactory;
    private transient ResourceResolver resourceResolver;


    @Activate
    protected void activate() {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ResourceResolverFactory.SUBSERVICE, "XYZABC");

        log.debug("Active method called sucessfully");

        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(paraMap);
        } catch (Exception e) {
            log.debug("resource resolver is throwing exception::{}", e);
        }
    }

    @Override
    public boolean modifyPageProperty(String path) {
        try {
            Date dtf = new Date();
            log.debug("Show date ::{}", dtf);
            Resource res = resourceResolver.getResource(path);
            log.debug("Requested path ::{}", res.getPath());

            ModifiableValueMap resProperty = res.adaptTo(ModifiableValueMap.class);
            resProperty.put("pubdate", dtf);
            log.debug("Date Putup successfully");
            resourceResolver.commit();
            return true;

        } catch (Exception err) {

            log.debug("ERROR in modify page property::{}", err);
        }

        return false;
    }
}
