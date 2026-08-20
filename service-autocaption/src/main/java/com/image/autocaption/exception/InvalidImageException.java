package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class InvalidImageException extends ApplicationException {

    public InvalidImageException(String message) {
        super(ErrorCode.INVALID_IMAGE, message);
    }
}