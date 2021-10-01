package com.myproject.aem.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.myproject.aem.core.constants.CommonConstants;
import com.myproject.aem.core.models.SearchResultItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Class SearchServlet processes the full text search
 */
@Component(service = Servlet.class, immediate = true)
@SlingServletResourceTypes(resourceTypes = "training/components/search-results",
        methods = HttpConstants.METHOD_GET,
        selectors = "searchresults", extensions = "json")
@ServiceDescription("Search results full text search Servlet")
public class SearchServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    //Logger
    private static final Logger log = LoggerFactory.getLogger(SearchServlet.class);
    //Constants
    private static final String SEARCH_TERM = "searchTerm";
    private static final String SEARCH_PATH = "searchPath";

    transient ResourceResolver resourceResolver = null;
    @Reference
    transient QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        resourceResolver = req.getResourceResolver();

        String searchKeyword = req.getParameter(SEARCH_TERM);
        List<String> searchResults = new LinkedList<>();
        String searchPath = req.getParameter(SEARCH_PATH);


        Map<String, String> params = new HashMap<>();
        params.put(CommonConstants.PATH, searchPath);
        params.put(CommonConstants.TYPE, CommonConstants.CQ_PAGE);
        params.put(CommonConstants.FULLTEXT, searchKeyword);
        params.put(CommonConstants.P_LIMIT, CommonConstants.MINUS_ONE);

        log.debug("Parameter Map {}", params);
        Query query = queryBuilder.createQuery(PredicateGroup.create(params), resourceResolver.adaptTo(Session.class));
        SearchResult queryResults = query.getResult();

        for (Hit hit : queryResults.getHits()) {
            try {
                Resource resource = hit.getResource();
                searchResults.add(resource.getPath());
            } catch (RepositoryException e) {
                log.error("Exception while fetching search results {}", e);
            }
        }
        resp.setContentType(CommonConstants.APPLICATION_JSON);
        List<SearchResultItem> finalSearchResult = getObjListFromPaths(searchResults);
        resp.getWriter().write(new Gson().toJson(finalSearchResult));
    }

    private List<SearchResultItem> getObjListFromPaths(List<String> resultPathList){
        List<SearchResultItem> searchItemsList = new LinkedList<>();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        resultPathList.forEach(resultPath -> {
            Page page = pageManager.getContainingPage(resultPath);
            String title = StringUtils.isNotBlank(page.getPageTitle()) ? page.getPageTitle() : page.getTitle();
            String description = page.getDescription();
            String pageUrl = page.getPath() + CommonConstants.HTML_EXTENSION;
            SearchResultItem searchItem = new SearchResultItem();
            searchItem.setTitle(title);
            searchItem.setDescription(description);
            searchItem.setPageUrl(pageUrl);
            searchItemsList.add(searchItem);
        });

        return searchItemsList;
    }
}
