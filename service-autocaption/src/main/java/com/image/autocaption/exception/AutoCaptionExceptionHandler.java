package com.image.autocaption.exception;

import com.image.autocaption.constant.ErrorCode;
import com.image.autocaption.model.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;

/**
 * A Global Exception Handler to return a structured error response in case of various runtime exceptions.
 */
@RestControllerAdvice
public class AutoCaptionExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AutoCaptionExceptionHandler.class);

    /**
     * Application/business exceptions.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationException ex, HttpServletRequest request) {

        HttpStatus status = mapStatus(ex.getErrorCode());
        log.warn("Application error: errorCode={}, path={}, message={}",
                ex.getErrorCode(),
                request.getRequestURI(),
                ex.getMessage());

        return buildResponse(status, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    /**
     * @Valid validation failures.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request) {

        var details = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> new ErrorResponseDto.ErrorDetail(error.getField(), error.getDefaultMessage()))
                        .toList();

        return buildResponse(HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                "Request validation failed", request,
                details);
    }

    /**
     * Validation failures for path/query parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex,
                                                                      HttpServletRequest request) {

        var details = ex.getConstraintViolations()
                        .stream()
                        .map(violation -> new ErrorResponseDto.ErrorDetail(
                                violation.getPropertyPath().toString(), violation.getMessage()))
                        .toList();

        return buildResponse(HttpStatus.BAD_REQUEST,
                            ErrorCode.VALIDATION_ERROR,
                            "Request validation failed",
                            request,
                            details);
    }

    /**
     * Invalid path/query parameter type.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST,
                            ErrorCode.INVALID_REQUEST,
                            "Invalid value for parameter: " + ex.getName(),
                            request,
                            null);
    }

    /**
     * Catch-all handler.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error while processing request: path={}", request.getRequestURI(), ex);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request,
                null);
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status,
                                                           ErrorCode errorCode,
                                                           String message,
                                                           HttpServletRequest request,
                                                           List<ErrorResponseDto.ErrorDetail> details) {

        var response = new ErrorResponseDto(Instant.now(),
                status.value(),
                errorCode.name(),
                message,
                request.getRequestURI(),
                getTraceId(request),
                details);
        return ResponseEntity.status(status).body(response);
    }

    private String getTraceId(HttpServletRequest request) {

        String traceId = request.getHeader("X-Trace-Id");
        return traceId != null ? traceId : "unknown";
    }

    private HttpStatus mapStatus(ErrorCode errorCode) {

        return switch (errorCode) {

            case VALIDATION_ERROR, INVALID_REQUEST, IMAGE_REQUIRED,
                    INVALID_IMAGE, IMAGE_TOO_LARGE, UNSUPPORTED_IMAGE_FORMAT -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED, INVALID_TOKEN, TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case RESOURCE_NOT_FOUND, FILE_NOT_FOUND, JOB_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case RATE_LIMIT_EXCEEDED, LLM_RATE_LIMITED -> HttpStatus.TOO_MANY_REQUESTS;
            case UNSUPPORTED_MEDIA_TYPE -> HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyFile(EmptyFileException ex, HttpServletRequest e) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.IMAGE_REQUIRED, ex.getMessage(), e, null);
    }

}
