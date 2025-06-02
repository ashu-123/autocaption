package com.image.autocaption.config;

import com.image.autocaption.rateLimiting.CustomRateLimitingFilter;
import com.image.autocaption.rateLimiting.RateLimitingFilter;
import com.rateLimiter.FixedWindowRateLimiter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class FilterConfig {

    private final RateLimitingFilter rateLimitingFilter;

    public FilterConfig(RateLimitingFilter rateLimitingFilter) {
        this.rateLimitingFilter = rateLimitingFilter;
    }

    @Bean
    public Jedis jedis() {
        return new Jedis("localhost", 6379);
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
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}

