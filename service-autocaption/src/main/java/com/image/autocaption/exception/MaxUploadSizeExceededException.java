package com.image.autocaption.exception;

public class MaxUploadSizeExceededException extends RuntimeException{

    public MaxUploadSizeExceededException(String message) {
        super(message);
    }
}
