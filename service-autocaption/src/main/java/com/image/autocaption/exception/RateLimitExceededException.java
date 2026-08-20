package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class RateLimitExceededException extends ApplicationException {
    public RateLimitExceededException() {
        super(ErrorCode.RATE_LIMIT_EXCEEDED,  "Too many requests. Please try again later.");
    }
}
