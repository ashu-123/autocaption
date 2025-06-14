package com.image.autocaption.rateLimiting;

public sealed interface ApiRateLimiter permits FixedWindowApiRateLimiter, SlidingWindowApiRateLimiter, TokenBucketApiRateLimiter {
}
