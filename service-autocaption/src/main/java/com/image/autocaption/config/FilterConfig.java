package com.image.autocaption.config;

import com.image.autocaption.rateLimiting.*;
import com.rateLimiter.FixedWindowRateLimiter;
import com.rateLimiter.SlidingWindowRateLimiter;
import com.rateLimiter.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * The class which defines bean definition of various API rate limiters
 */
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
    @ConditionalOnProperty(name = "api.rateLimiting.filter", havingValue = "fixedWindow", matchIfMissing = true)
    public FixedWindowRateLimiter fixedWindowRateLimiter() {
        return new FixedWindowRateLimiter(jedis(), 60, 10);
    }

    @Bean
    @ConditionalOnProperty(name = "api.rateLimiting.filter", havingValue = "slidingWindow")
    public SlidingWindowRateLimiter slidingWindowRateLimiter() {
        return new SlidingWindowRateLimiter(jedis(), 60, 20, 10);
    }

    @Bean
    @ConditionalOnProperty(name = "api.rateLimiting.filter", havingValue = "tokenBucket")
    public TokenBucketRateLimiter tokenBucketRateLimiter() {
        return new TokenBucketRateLimiter(jedis(), 10, 5);
    }

    @Bean
    @ConditionalOnBean(FixedWindowRateLimiter.class)
    public FixedWindowApiRateLimiter fixedWindowApiRateLimiter(FixedWindowRateLimiter fixedWindowRateLimiter) {
        return new FixedWindowApiRateLimiter(fixedWindowRateLimiter);
    }

    @Bean
    @ConditionalOnBean(SlidingWindowRateLimiter.class)
    public SlidingWindowApiRateLimiter slidingWindowApiRateLimiter(SlidingWindowRateLimiter slidingWindowRateLimiter) {
        return new SlidingWindowApiRateLimiter(slidingWindowRateLimiter);
    }

    @Bean
    @ConditionalOnBean(TokenBucketRateLimiter.class)
    public TokenBucketApiRateLimiter tokenBucketApiRateLimiter(TokenBucketRateLimiter tokenBucketRateLimiter) {
        return new TokenBucketApiRateLimiter(tokenBucketRateLimiter);
    }

    @Bean
    public CustomRateLimitingFilter customRateLimitingFilter(ApiRateLimiter apiRateLimiter) {
        return new CustomRateLimitingFilter(apiRateLimiter);
    }

//    @Bean
//    public FilterRegistrationBean<RateLimitingFilter> registerRateLimitingFilter(){
//        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(rateLimitingFilter);
//        registrationBean.addUrlPatterns("/api/*");
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean<CustomRateLimitingFilter> registerRateLimitingFilter(ApiRateLimiter apiRateLimiter) {
        FilterRegistrationBean<CustomRateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(customRateLimitingFilter(apiRateLimiter));
        registrationBean.addUrlPatterns("/api/*", "/auth/*");
        return registrationBean;
    }
}

