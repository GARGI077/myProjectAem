package com.myproject.aem.core.servlets;


import com.myproject.aem.core.services.JcrDateModify;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/JcrDate" })

public class JcrDateModifyServlet extends SlingSafeMethodsServlet {


    private transient ResourceResolverFactory resourceResolverFactory;
    private transient ResourceResolver resourceResolver;

    private static final long serialVersionUID = 2610051404257637265L;

    private static final Logger log = LoggerFactory.getLogger(JcrDateModifyServlet.class);

    @Reference
    JcrDateModify jcrDateModify;
//
//    log.debug("Service called sucessfully");


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        //super.doGet(request, response);

        log.debug("the servlet is active");

        response.getWriter().write("Servlet is Active");

        try{

            String page_path = request.getParameter("path");
            log.debug("Path::{}",page_path);
            response.getWriter().write(page_path);

            boolean date_set=jcrDateModify.modifyPageProperty(page_path);
            log.debug("Date is set::{}",date_set);


        }catch (Exception e){

            log.debug("Exception ::{}",e);

        }
    }
}
