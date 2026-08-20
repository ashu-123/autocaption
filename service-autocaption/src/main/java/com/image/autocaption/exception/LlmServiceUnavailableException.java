package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class LlmServiceUnavailableException extends ApplicationException {

    public LlmServiceUnavailableException(String message) {
        super(ErrorCode.LLM_SERVICE_UNAVAILABLE, message);
    }
}
