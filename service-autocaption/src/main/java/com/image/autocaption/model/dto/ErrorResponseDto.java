package com.image.autocaption.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * The API representation of a structured error response in case of runtime exceptions occurring
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(Instant timestamp,
                               int status,
                               String error,
                               String message,
                               String path,
                               String traceId,
                               List<ErrorDetail> details) {

    public record ErrorDetail(String field, String message) { }
}
