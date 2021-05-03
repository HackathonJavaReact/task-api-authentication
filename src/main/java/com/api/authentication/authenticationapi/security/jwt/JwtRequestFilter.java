package com.api.authentication.authenticationapi.security.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.authentication.authenticationapi.security.CookieUtil;
import com.api.authentication.authenticationapi.service.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestToken = getTokenFromCookies(request);

        String username = null;
        String jwtToken = null;

        // If the token is not specified in headers, check for token in session cookies
        if (requestToken == null) {
            requestToken = request.getHeader("Authorization");
        }

        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestToken != null && requestToken.startsWith("Bearer ")) {

            jwtToken = requestToken.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            } catch (Exception e) {
                System.out.println("Problem with JWT token");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails;

            try {
                userDetails = this.applicationUserService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                userDetails = null;
            }

            // if token is valid configure Spring Security to manually set
            // authentication
            if (userDetails != null && jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromCookies(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = null;
        Cookie[] cookies = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();

        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieUtil.getCookieName())) {
                    token = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }

        return token;
    }

}
