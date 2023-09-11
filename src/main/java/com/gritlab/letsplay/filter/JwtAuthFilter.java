package com.gritlab.letsplay.filter;

import com.gritlab.letsplay.config.SecurityConfig;
import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.service.JwtService;
import com.gritlab.letsplay.service.UserInfoDetailsService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // extract the jwt
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserInfoDetailsService userInfoDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWZpc2FoLnJhbnRhc2FsbWlAZ21haWwuY29tIiwiaWF0IjoxNjkzNTAwMjI0LCJleHAiOjE2OTM1MDM4MjR9.BUfgZ2TJB1WdgQoEObe73hdNd-oA9n7QdTOcoUffqYQ
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ") ) {

                token = authHeader.substring(7);
                try {
                    username = jwtService.extractUsername(token);
                } catch(Exception e) {
                    sendErrorResponse(response, "Invalid JWT token.");
                    return;
                }

        }

//        String uri = request.getRequestURI();
//        System.out.println("uri: " + uri);
//
//        // Split the URI by '/' and get the last segment
//        String[] segments = uri.split("/");
//        String lastSegment = segments[segments.length - 1];
//
//        System.out.println("Last segment: " + lastSegment);
//
//        System.out.println("authHeader: " + authHeader);



        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
//        else  if ((!uri.equals("/users/new") &&
//                !uri.equals("/users/authenticate") &&
//                !uri.equals("/products/create") &&
//                !uri.equals("/products/all") &&
//                !uri.equals("/products/single/" + lastSegment)))
//        {  // For URIs with variable parts like {id}, you can use regex. Here \d+ matches one or more digits.
//            sendErrorResponse(response, "Authorization needed!");
//            return;
//        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("error: " + message );
    }
}
