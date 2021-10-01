package com.myproject.aem.core.services.impl;

import com.myproject.aem.core.services.JcrContentModify;
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

import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = {JcrContentModify.class},
        property = {Constants.SERVICE_VENDOR + "=" + "XYZABC"})

public class JcrContentModifyImpl implements JcrContentModify {

    private static final Logger LOGGER = LoggerFactory.getLogger(JcrContentModifyImpl.class);

    @Reference
    private transient ResourceResolverFactory resourceResolverFactory;
    private transient ResourceResolver resourceResolver;

    @Activate
    protected void activate() {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ResourceResolverFactory.SUBSERVICE, "gargi_user");

        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(paraMap);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }


    }

    @Override
    public boolean modifyPageProperty(String path, String content) {

        try {
            Resource res = resourceResolver.getResource(path);
            LOGGER.info(res.getPath());
            if ((res != null && res instanceof Resource)) {
                ModifiableValueMap resProperty = res.adaptTo(ModifiableValueMap.class);
                resProperty.put("des", content);
                resourceResolver.commit();
                return true;
            }


        } catch (Exception err) {

            LOGGER.info("ERROR", err);
        }
        return false;
    }
}