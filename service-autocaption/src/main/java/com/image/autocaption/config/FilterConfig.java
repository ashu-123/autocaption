package com.image.autocaption.config;

import com.image.autocaption.rateLimiting.CustomRateLimitingFilter;
import com.image.autocaption.rateLimiting.Bucket4jRateLimitingFilter;
import com.rateLimiter.FixedWindowRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class FilterConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    private final Bucket4jRateLimitingFilter bucket4jRateLimitingFilter;

    public FilterConfig(Bucket4jRateLimitingFilter bucket4jRateLimitingFilter) {
        this.bucket4jRateLimitingFilter = bucket4jRateLimitingFilter;
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }

    @Bean
    public FixedWindowRateLimiter fixedWindowRateLimiter() {
        return new FixedWindowRateLimiter(jedis(), 60, 10);
    }

    @Bean
    public CustomRateLimitingFilter customRateLimitingFilter() {
        return new CustomRateLimitingFilter(fixedWindowRateLimiter());
    }

//    @Bean
//    public FilterRegistrationBean<RateLimitingFilter> registerRateLimitingFilter(){
//        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(rateLimitingFilter);
//        registrationBean.addUrlPatterns("/api/*");
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean<CustomRateLimitingFilter> registerRateLimitingFilter() {
        FilterRegistrationBean<CustomRateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(customRateLimitingFilter());
        registrationBean.addUrlPatterns("/api/*", "/auth/*");
        return registrationBean;
    }
}

