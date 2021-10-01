package com.myproject.aem.core.servlets;

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/JcrPropertiesFetch" })

public class JcrPropertiesFetch extends SlingSafeMethodsServlet {


    private static final long serialVersionUID = 2610051404257637265L;

    private static final Logger log = LoggerFactory.getLogger(JcrPropertiesFetch.class);
    private Session session;

    private Object String;


}
