package com.image.autocaption.exception;

import com.image.autocaption.model.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A Global Exception Handler to return a structured error response in case of various runtime exceptions.
 */
@RestControllerAdvice
public class AutoCaptionExceptionHandler {

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyFile(EmptyFileException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponseDto("EmptyFile", ex.getMessage(), HttpStatus.BAD_REQUEST.value())
        );
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedMediaType(UnsupportedMediaTypeException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                new ErrorResponseDto("UnsupportedMediaType", ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                new ErrorResponseDto("FileTooLarge", "Uploaded file is too large", HttpStatus.PAYLOAD_TOO_LARGE.value())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto("InternalError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}
