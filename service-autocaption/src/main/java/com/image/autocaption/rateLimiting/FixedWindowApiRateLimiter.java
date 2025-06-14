package com.image.autocaption.rateLimiting;

import com.rateLimiter.FixedWindowRateLimiter;

public final class FixedWindowApiRateLimiter implements ApiRateLimiter{

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    public FixedWindowApiRateLimiter(FixedWindowRateLimiter fixedWindowRateLimiter) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
    }

    public FixedWindowRateLimiter getFixedWindowRateLimiter() {
        return fixedWindowRateLimiter;
    }
}
