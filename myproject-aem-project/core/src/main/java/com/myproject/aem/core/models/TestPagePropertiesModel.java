package com.myproject.aem.core.models;

import com.day.cq.wcm.api.Page;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Getter
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class TestPagePropertiesModel {

    private static final Logger log = LoggerFactory.getLogger(TestPagePropertiesModel.class);

    @ValueMapValue
    ResourceResolver resourceResolver;

    @ValueMapValue
    ResourceResolverFactory resolverFactory;
    @ValueMapValue
    private String pagePath;

    private String heading;
    private String subheading;
    private String image;

    @PostConstruct
    protected void init() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "XYZABC");
        Session session;

        try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(param)) {
            session = resourceResolver.adaptTo(Session.class);
            log.debug("TestPagePropertiesModel:: init()");
            Resource path = resourceResolver.getResource(pagePath);
            log.debug("TestPagePropertiesModel:: init():: PagePath::{}", path.getPath());
            if (path != null) {
                Page page = path.adaptTo(Page.class);
                log.debug("TestPagePropertiesModel:: init():: PagePagePath::{}", page);
                TestPagePropertiesDto dto = new TestPagePropertiesDto();
                ValueMap valueMap = page.getProperties();
                heading = valueMap.get("heading", String.class);
                log.debug("TestPagePropertiesModel:: init():: Heading::{}", heading);
                subheading = valueMap.get("description", String.class);
                image = valueMap.get("image", String.class);
            }
        } catch (Exception e) {
            log.error("TestPagePropertiesModel:: init():: Exception::{}", e);
        }
    }
}
