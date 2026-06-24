package com.azercell.marketplace.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final String traceId;
    private final int status;
    private final boolean success;
    private final String message;
    private final T data;
    private final ApiError error;
    private final Instant timestamp;


    public static <T> ApiResponse<T> ok(T data) {
        return success(data, "OK", HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created(T data) {
        return success(data, "Created", HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }


    public static <T> ApiResponse<T> badRequest(String message) {
        return failure(message, HttpStatus.BAD_REQUEST, null);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return failure(message, HttpStatus.NOT_FOUND, null);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return failure(message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return failure(message, HttpStatus.UNAUTHORIZED, null);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return failure(message, HttpStatus.FORBIDDEN, null);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status, ApiError error) {
        return failure(message, status, error);
    }


    private static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .traceId(UUID.randomUUID().toString())
                .status(status.value())
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    private static <T> ApiResponse<T> failure(String message, HttpStatus status, ApiError error) {
        return ApiResponse.<T>builder()
                .traceId(UUID.randomUUID().toString())
                .status(status.value())
                .success(false)
                .message(message)
                .error(error)
                .timestamp(Instant.now())
                .build();
    }
}