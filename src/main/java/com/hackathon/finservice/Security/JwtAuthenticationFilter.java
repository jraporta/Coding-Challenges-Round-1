package com.hackathon.finservice.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isProtectedEndpoint(request)) {
            log.debug("Validating JWT");
            if (!executeFilter(request, response)) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        log.debug("Checking if path {} is public", path);
        return !(path.contains("api/users/register") ||
                path.contains("api/users/login") ||
                path.contains("health"));
    }

    protected boolean executeFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(header);

        if (authorizationHeader == null) {
            log.debug("Error, missing authorization header");
            sendErrorResponse(response, "Access Denied");
            return false;
        }

        String jwtToken = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(prefix + " ")) {
            jwtToken = authorizationHeader.substring(prefix.length() + 1);
            log.debug("Read token from header: {}", jwtToken);
        }
        try {
            username = jwtTokenUtil.extractUsername(jwtToken);
            log.debug("Extracted username from token: {}", username);
        }catch (ExpiredJwtException ex) {
            log.debug("Expired Jwt");
            sendErrorResponse(response, "Invalid JWT: token is expired");
            return false;
        }catch (JwtException ex) {
            log.debug("Invalid JWT token");
            sendErrorResponse(response, "Access Denied");
            return false;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        log.debug("Creating custom error response: {}", message);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(message);
    }
}