package com.vvdn.ems_backend.security;

import com.vvdn.ems_backend.services.impl.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        logger.debug("JwtAuthFilter processing request: {} {}", request.getMethod(), requestURI);

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found for request: {} — skipping JWT validation", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        logger.debug("Bearer token found, attempting to extract username");

        if(tokenBlacklistService.isBlacklisted(token)){
            logger.warn("Token is blacklisted, Access denied.");
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            if (!jwtUtil.validateToken(token, "access")) {
                logger.warn("Invalid JWT token");
                filterChain.doFilter(request, response);
                return;
            }
            username = jwtUtil.extractUsername(token);
            logger.debug("Username extracted from token: {}", username);
        } catch (Exception e) {
            logger.warn("Failed to extract username from JWT token: {}", e.getMessage());

            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("Loading UserDetails for username: {}", username);

            UserDetails userDetails;
            try {
                userDetails = customUserDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                logger.warn("User not found in DB for token username '{}': {}", username, e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }
            logger.debug("Setting authentication in SecurityContext for user: {}", username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("Authentication set for user: {} on path: {}", username, requestURI);
        }

        filterChain.doFilter(request, response);
    }
}


