package com.myproject.aem.core.servlets;

import java.util.*;

import javax.jcr.Session;
import javax.json.*;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/*
 *
 * This servlet uses the QueryBuilder API to fetch the results from the JCR
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/querybuilder" })
public class QueryBuilderServlet extends SlingSafeMethodsServlet {

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 2610051404257637265L;

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(QueryBuilderServlet.class);

    /**
     * Injecting the QueryBuilder dependency
     */
    @Reference
    private QueryBuilder builder;

    @Reference

    /**
     * Session object
     */
    private Session session;
    private Object String;

    /**
     * Overridden doGet() method which executes on HTTP GET request
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {


        try {

            log.info("----------< Executing Query Builder Servlet >----------");

            /**
             * This parameter is passed in the HTTP call
             */
            String param = request.getParameter("param");

            log.info("Search term is: {}", param);

            /**
             * Get resource resolver instance
             */
            ResourceResolver resourceResolver = request.getResourceResolver();

            /**
             * Adapting the resource resolver to the session object
             */
            session = resourceResolver.adaptTo(Session.class);

            /**
             * Map for the predicates
             */
            Map<String, String> predicate = new HashMap<>();

            /**
             * Configuring the Map for the predicate
             */
            predicate.put("path", "/content/icicihfc-corporate/en");
            predicate.put("type","cq:Page");
            predicate.put("orderby", "@jcr:content/jcr:lastModified");
            predicate.put("p.limit", "7");
            predicate.put("orderby.sort","asc");
            //predicate.put("group.1_fulltext", param);
           // predicate.put("group.1_fulltext.relPath", "jcr:content");


            /**
             * Creating the Query instance
             */
            Query query = builder.createQuery(PredicateGroup.create(predicate), session);

            query.setStart(0);
            query.setHitsPerPage(7);

            /**
             * Getting the search results
             */
            SearchResult searchResult = query.getResult();

            // Hit api use to represent a single search result, ie. a node and resource.
            for(Hit hit : searchResult.getHits()) {

                String path = hit.getPath();

                response.getWriter().println(path);
            }
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        } finally {

            if(session != null) {

                session.logout();
            }

        }




    }

}