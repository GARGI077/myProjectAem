package com.myproject.aem.core.services.impl;


import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.myproject.aem.core.models.JcrData;
import com.myproject.aem.core.services.JcrSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component(service = JcrSearchService.class, immediate = true,
        property = {Constants.SERVICE_DESCRIPTION + " = Jcr Search Service"})
public class JcrSearchServiceImpl implements JcrSearchService {

    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private QueryBuilder builder;

    @Override
    public String JcrSearch(String path, String limit) {
        String json = null;
        log.debug("Debugging Started.....");
        log.debug("path::{}", path);
        log.debug("limit::{}", limit);


        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "XYZABC");
        Session session;
        try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(param)) {
            session = resourceResolver.adaptTo(Session.class);

            Map<String, String> predicate = new HashMap<>(); //map variable for predicated

            //Configuring the Map for the predicate
            predicate.put("path", path); // using arguments to get path
            predicate.put("type", "cq:Page");
            predicate.put("orderby", "@jcr:content/jcr:lastModified");
            predicate.put("p.limit", limit);
            predicate.put("orderby.sort", "asc");

            Query query = builder.createQuery(PredicateGroup.create(predicate), session);// queryBuilder instance
            SearchResult result = query.getResult();// getting search  results of query


            log.debug("Working qeury");

            List<JcrData> contentList = new ArrayList<>(); // A list var to store results


            // Hit api use to represent a single search result, ie. a node and resource.
            for (Hit hit : result.getHits()) {
                log.debug("Start");
                String paths = hit.getPath();//Gives The Path of Current Hit
                log.debug("Get Paths::{}", paths);
                String title = hit.getTitle();
                log.debug("Title::{}", title);

                JcrData data = new JcrData();
                data.setPath(paths);
                log.debug("Path Set");
                data.setTitle(title);
                log.debug("Title Set");
                contentList.add(data);
                log.debug("Contet Added sucessffully");
            }
            log.debug("Content List::{}", contentList);
            Gson gson = new Gson();
            json = gson.toJson(contentList);
            log.debug("JSON::{}", json);

        } catch (Exception e) {
            log.error("Exception Method::{}", e);
        }
        log.debug("Json Returning::{}", json);
        return json;
    }


}