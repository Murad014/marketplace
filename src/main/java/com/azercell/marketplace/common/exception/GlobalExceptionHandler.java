package com.azercell.marketplace.common.exception;


import com.azercell.marketplace.common.dto.ApiError;
import com.azercell.marketplace.common.dto.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<?>> handleDomain(
            DomainException ex, Locale locale) {

        String message = messageSource.getMessage(
                ex.getErrorCode().name(),
                null,
                ex.getMessage(),
                locale);


        var errorResponse = ApiResponse.error(message,HttpStatus.BAD_REQUEST, null);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}