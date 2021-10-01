package com.myproject.aem.core.util;

import com.myproject.aem.core.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CCUtil {

    private CCUtil() {
    }

    private static Logger log = LoggerFactory.getLogger(CCUtil.class);
    private static final String SALT = "8e3d9870-e2c7-4894-b4f0-98452f95a012";
//
//    /**
//     * Returns {@link ResourceResolver} of the given sub-service. It returns null in case given
//     * {@link ResourceResolverFactory} and subService are null.
//     *
//     * @param resourceResolverFactory {@link ResourceResolverFactory}
//     * @param subService              {@link String} sub-service defined in Sling Apache User Mapping configuration
//     * @return {@link ResourceResolver}
//     */
    public static ResourceResolver getResourceResolver(final ResourceResolverFactory resourceResolverFactory,
                                                       final String subService) {
        ResourceResolver resourceResolver = null;
        if (null != resourceResolverFactory && null != subService) {
            try {
                final Map<String, Object> authInfo = new HashMap<>();
                authInfo.put(ResourceResolverFactory.SUBSERVICE, subService);
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
            } catch (final LoginException loginException) {
                log.error(
                        "CCUtil getResourceResolver() : Exception while getting resourceResolver for subService {} : {} ",
                        subService, loginException);
            }
        }
        return resourceResolver;
    }

    /**
     * Method to Generate Hash (SHA-256)
     */

    public static String generatingHashAndGuid(String email, String uid) {
        String generatedGUID = null;
        try {
            log.debug("Coming Email :: {}", uid);
            String guidInLowerCase = uid.toLowerCase();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((guidInLowerCase + SALT).getBytes());
            log.info("Converted Email :: {}", guidInLowerCase);
            byte[] bytes = md.digest(guidInLowerCase.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedGUID = email.toLowerCase() + "-" + sb.toString();
        } catch (Exception e) {
            log.error("Error in generatingHash(...) :: {}", e);
        }
        return generatedGUID;
    }
//
//    /**
//     * Method to extract the GUID from Cookies[]
//     *
//     * @param cookies List of Cookies
//     * @return GUID
//     * @throws JSONException
//     * @throws UnsupportedEncodingException
//     */
    public static String getGUID(Cookie[] cookies) throws JSONException, UnsupportedEncodingException {
        if (cookies == null) {
            return StringUtils.EMPTY;
        }
        String guid = StringUtils.EMPTY;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.OKTA_USER_COOKIE)) {
                String decodedCookie = URLDecoder.decode(cookie.getValue() , "UTF-8");
                JSONObject jsobj = new JSONObject(decodedCookie);
                log.debug("Cookie Value: {}", jsobj);
                guid = jsobj.getString(Constants.GUID);
                break;
            }
        }
        return guid;
    }

    /**
     * Method to extract the Authorizable User
     *
     * @param userManager      User Manager Object
     * @param resourceResolver Resource Resolver Object
     * @param guid             Unique GUID
     * @return Authorizable or Null if User not found
     * @throws RepositoryException
     */
    public static Authorizable getAuthorizableUser(UserManager userManager, ResourceResolver resourceResolver, String guid)
            throws RepositoryException {
        if (userManager == null || StringUtils.isBlank(guid))
            return null;
        Authorizable userFromCookie = null;
        userManager = resourceResolver.adaptTo(UserManager.class);
        Iterator<Authorizable> authorizableIterator = null;
        if (userManager != null) {
            authorizableIterator = userManager.findAuthorizables(
                    Constants.GUID, guid);
            while (authorizableIterator.hasNext()) {
                Authorizable authenticatedUser = authorizableIterator.next();
                if (!authenticatedUser.isGroup()) {
                    log.debug("Authenticated User ID: {}", authenticatedUser.getID());
                    String id = authenticatedUser.getID();
                    userFromCookie = userManager.getAuthorizable(id);
                    break;
                }
            }
        }
        return userFromCookie;
    }

    /**
     * Method to create the cookie
     *
     * @param cookieName  Cookie Name
     * @param domain      Domain
     * @param cookieValue Cookie Valus
     * @return Cookie Object
     * @throws UnsupportedEncodingException
     */
    public static Cookie createCookie(String cookieName, String domain, String cookieValue) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
        setCookieAge(cookie,cookieName);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setDomain(domain);
        return cookie;
    }

    private static Cookie setCookieAge(Cookie cookie,String cookieName) {
        if(cookieName.equals(Constants.COOKIE_WHITELIST)) {
            cookie.setMaxAge(7 * 24 * 3600);
        }else if(cookieName.equals(Constants.BOOKMARKS_COOKIE)) {
            cookie.setMaxAge(60 * 24 * 3600);
        }else {
            cookie.setMaxAge(365 * 24 * 3600);
        }
        return cookie;
    }

}
