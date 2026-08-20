package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
