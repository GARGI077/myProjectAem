package com.myproject.aem.core.servlets;

import com.myproject.aem.core.services.JcrSearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/JcrSearchServlet" })

public class JcrSearchServlet extends SlingSafeMethodsServlet {


    private static final long serialVersionUID = 2610051404257637265L;

    private static final Logger log = LoggerFactory.getLogger(JcrSearchServlet.class);
    private Session session;

    private Object String;

    @Reference
    JcrSearchService jcrSearchService;





    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
      //  resp.setContentType("text/plain");


try {
    String path = req.getParameter("path");
    String limit = req.getParameter("limit");

    resp.getWriter().write("Servlet is Active ");

    log.info("path",path);
    log.info("limit",limit);
    resp.getWriter().write(path);
    resp.getWriter().write(limit);

    //searchArticleService.getJSONData(path,limit);
    String json=jcrSearchService.JcrSearch(path,limit);

    resp.getWriter().write(json);
}
catch (Exception e){

    log.info("Exception thrown::{}",e.getMessage());
}

    }
}
