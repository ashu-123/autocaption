package com.image.autocaption.rateLimiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import io.github.bucket4j.*;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bucket4jRateLimitingFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientIp = req.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
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

