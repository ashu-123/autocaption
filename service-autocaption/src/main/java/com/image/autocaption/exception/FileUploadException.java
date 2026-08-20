package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;

public class FileUploadException extends ApplicationException {

    public FileUploadException(String message, Throwable cause) {
        super(ErrorCode.FILE_UPLOAD_FAILED, message, cause);
    }
}