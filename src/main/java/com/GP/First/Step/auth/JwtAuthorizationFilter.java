package com.GP.First.Step.auth;

import com.GP.First.Step.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//JwtAuthorizationFilter is the first executed class
// when getting any request to check JWT token and send it to be validated.
@Component
// We want this filter to be active every time we get request, So we extend it from OncePerRequestFilter.
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;
    private final CustomUserDetailsService userDetailsService;

    // Constructor to inject JwtUtil, ObjectMapper, and CustomUserDetailsService.
    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Map<String, Object> errorDetails = new HashMap<>();
        try {
            // Resolve the token from the request.
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null) {
                // If no token is present, continue with the filter chain.
                filterChain.doFilter(request, response);
                return;
            }
            // Parse the token claims.
            Claims claims = jwtUtil.resolveClaims(request);

            // Validate the token claims.
            String email = claims.getSubject();
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (jwtUtil.isTokenValid(claims, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // If the token is not valid, throw an exception.
                    throw new RuntimeException("Invalid JWT token");
                }
            }
            else {
                // If the claims are not valid, throw an exception.
                throw new RuntimeException("Invalid JWT token Claims.");
            }

        } catch (Exception e) {
            // Handle exceptions by sending an error response.
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Write the error details to the response.
            mapper.writeValue(response.getWriter(), errorDetails);

        }
        // Continue with the filter chain.
        filterChain.doFilter(request, response);
    }
}