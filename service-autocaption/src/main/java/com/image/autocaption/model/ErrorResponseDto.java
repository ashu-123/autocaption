package com.image.autocaption.model;

/**
 * The API representation of a structured error response in case of runtime exceptions occurring
 */
public class ErrorResponseDto {

    private String error;
    private String message;
    private int status;

    public ErrorResponseDto(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public ErrorResponseDto setError(String error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ErrorResponseDto setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorResponseDto{");
        sb.append("error='").append(error).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
