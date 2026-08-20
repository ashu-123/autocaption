package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class CaptionGenerationException extends ApplicationException {

    public CaptionGenerationException(String message, Throwable cause) {
        super(ErrorCode.CAPTION_GENERATION_FAILED, message, cause);
    }
}
