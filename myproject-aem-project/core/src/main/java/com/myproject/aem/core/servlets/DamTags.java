package com.myproject.aem.core.servlets;

import java.io.PrintWriter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.tagging.TagManager;
import com.day.cq.tagging.Tag;


import javax.servlet.Servlet;

/**
 *
 * @author praveen
 *
 */


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/getDamTags" })
public class DamTags extends org.apache.sling.api.servlets.SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DamTags.class);

    protected void doGet(SlingHttpServletRequest slingRequest,
                         SlingHttpServletResponse slingResponse) {
        try {
            LOGGER.info("Getting request to .....");


            // Get print writer object to output
            PrintWriter out = slingResponse.getWriter();
            String path = slingRequest.getParameter("path");
            LOGGER.info("Path received.. {}", path);

            Resource resource = slingRequest.getResourceResolver().getResource(path);
            Asset asset = resource.adaptTo(Asset.class);

            Object[] titleArray = null;
            Object titleObj = asset.getMetadata("cq:tags");
            if (titleObj instanceof Object[]) {
                titleArray = (Object[]) titleObj;
            }


            JSONObject damTagsJson = new JSONObject();
            JSONArray tags = new JSONArray();

            for (Object ob : titleArray) {
                String a = ob.toString();
                TagManager tagManager = null;
                tagManager = slingRequest.getResourceResolver().adaptTo(
                        TagManager.class);
                Tag custTag = tagManager.resolve(a);
                tags.put(custTag.getTitle());

            }

            // Output tags in json format
            out.print(damTagsJson.put("damTags", tags));

        } catch (Exception e) {
            LOGGER.info("Exception while fetching DAM assets tags "
                    + e.getMessage());
        }
    }
}