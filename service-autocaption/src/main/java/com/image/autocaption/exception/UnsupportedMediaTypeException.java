package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class UnsupportedMediaTypeException extends ApplicationException {

    public UnsupportedMediaTypeException(String message) {
        super(ErrorCode.UNSUPPORTED_MEDIA_TYPE, message);
    }
}
