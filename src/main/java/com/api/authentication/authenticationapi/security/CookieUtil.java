package com.api.authentication.authenticationapi.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    private final String cookieName = "sessionId";

    public CookieUtil() {
    }

    public HttpServletResponse addTokenToCookiesResponse(HttpServletResponse response, String token) throws UnsupportedEncodingException {

        Cookie sessionIdCookie = new Cookie(cookieName, URLEncoder.encode("Bearer "+ token, "UTF-8"));
        response.addCookie(sessionIdCookie);
        
        return response;
    }

    public String getCookieName() {
        return cookieName;
    }
   
}
