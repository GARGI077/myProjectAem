package com.myproject.aem.core.servlets;

import com.myproject.aem.core.services.JcrContentModify;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.sling.api.servlets.ServletResolverConstants;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Component(service = { Servlet.class },
        immediate = true,
        property ={Constants.SERVICE_DESCRIPTION + "=" +"DEMO SERVLET" ,
                ServletResolverConstants.SLING_SERVLET_PATHS +"="+"/bin/demo/jcrContent",
                ServletResolverConstants.SLING_SERVLET_METHODS+"="+ HttpConstants.METHOD_GET
        })

public class JcrContentModifyServlet extends SlingAllMethodsServlet {

    private static final Logger log= LoggerFactory.getLogger(JcrContentModifyServlet.class);

    private static final long serialVersionUID=1L;

    @Reference
    JcrContentModify jcrContentModify;

    @Activate

    protected final void activate()
    {
        log.info("Servlet activated");
    }



    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws ServletException, IOException {
        String path=request.getParameter("path"); // getting path in request
        String content=request.getParameter("content");// content to change in parameter as get req
        boolean resp= jcrContentModify.modifyPageProperty(path,content);
        PrintWriter out= response.getWriter();
        final Resource resource = request.getResource();
        if(resp)
        {
            out.println("Properties Saved Sucessfully");
        }



// response.setContentType("text/plain");
        response.getWriter().write("hello");

    }
}