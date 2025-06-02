package com.image.autocaption.rateLimiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rateLimiter.FixedWindowRateLimiter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomRateLimitingFilter implements Filter {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    private final ObjectMapper objectMapper;

    public CustomRateLimitingFilter(FixedWindowRateLimiter fixedWindowRateLimiter) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientIp = req.getRemoteAddr();
        String clientId = "rate-limit: " + clientIp;
        if (fixedWindowRateLimiter.isAllowed(clientId)) {
            chain.doFilter(request, response);
        } else {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.setContentType("application/json");
            res.setHeader("X-Rate-Limit-Limit", "10");
            res.setHeader("X-Rate-Limit-Remaining", "0");
            res.setHeader("Retry-After", "60"); // In seconds

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("timestamp", System.currentTimeMillis());
            responseBody.put("status", 429);
            responseBody.put("error", "Too Many Requests");
            responseBody.put("message", "Rate limit exceeded. Try again later.");
            responseBody.put("path", req.getRequestURI());


            String json = objectMapper.writeValueAsString(responseBody);
            res.getWriter().write(json);
        }
    }
}

