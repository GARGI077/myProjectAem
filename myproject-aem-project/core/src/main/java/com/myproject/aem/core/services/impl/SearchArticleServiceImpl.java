package com.myproject.aem.core.services.impl;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.myproject.aem.core.constants.Constants;
import com.myproject.aem.core.models.SearchResultItem;
import com.myproject.aem.core.services.SearchArticleService;
import com.myproject.aem.core.util.CCUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Exporter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




@Component( immediate = true ,service={SearchArticleService.class})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchArticleServiceImpl implements SearchArticleService {
    private static final Logger log = LoggerFactory.getLogger(SearchArticleServiceImpl.class);


    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    Gson gson = new Gson();

    ResourceResolver resourceResolver;
    Session session;

    @Activate
    protected void activate() {
        log.debug("Query builder service  Activated ");
        try {
            resourceResolver = CCUtil.getResourceResolver(resourceResolverFactory, "gargi_user");
            session = resourceResolver.adaptTo(Session.class);
        }catch (Exception e)
        {
            log.info("Not activated" ,e.getMessage());
        }
    }

    @Override
    public List<String> getJSONData(String path, String limit) throws RepositoryException {

        log.info("get Json method callled sucessfully");
        List<String> data= new LinkedList<>();
        try {
            Map<String, String> predicate = new HashMap<>();


            predicate.put("path", path);
            predicate.put("type", "cq:Page");
            predicate.put("orderby", "@jcr:content/cq:lastModified");
            predicate.put("orderby.sort", "asc");
            predicate.put("p.limit", limit);


            Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);


            SearchResult searchResult = query.getResult();
            for (Hit hit : searchResult.getHits()) {
                Resource resource = hit.getResource();
                data.add(resource.getPath());
                data.add(resource.getName());
                //data.add(resource.getValueMap().get("name","path"));


            }



        }catch (Exception e)
        {
            log.info(e.getMessage());
        }
        return data;
    }

}

