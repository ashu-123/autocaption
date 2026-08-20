package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class MaxUploadSizeExceededException extends ApplicationException{

    public MaxUploadSizeExceededException(String message) {
        super(ErrorCode.IMAGE_TOO_LARGE, message);
    }
}
