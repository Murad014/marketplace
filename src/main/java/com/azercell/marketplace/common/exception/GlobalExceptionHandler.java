package com.azercell.marketplace.common.exception;


import com.azercell.marketplace.common.dto.ApiError;
import com.azercell.marketplace.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /** Domain rule violations carrying an ErrorCode (message resolved via messages.properties). */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<?>> handleDomain(DomainException ex, Locale locale) {
        String message = messageSource.getMessage(
                ex.getErrorCode().name(),
                null,
                ex.getMessage(),
                locale);

        var error = ApiError.builder()
                .code(ex.getErrorCode().name())
                .detail(message)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST, error));
    }

    /** Bean-validation failures on a @Valid body — reported with field-level detail. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex, Locale locale) {
        List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ApiError.FieldError.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .rejectedValue(fe.getRejectedValue())
                        .build())
                .toList();

        String message = msg("VALIDATION_FAILED", locale);
        var error = ApiError.builder()
                .code("VALIDATION_FAILED")
                .detail(message)
                .fields(fields)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST, error));
    }

    /** Malformed JSON, or an unparseable enum/number in the request body (e.g. a bad availability value). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleUnreadable(HttpMessageNotReadableException ex, Locale locale) {
        String message = msg("MALFORMED_REQUEST", locale);
        var error = ApiError.builder()
                .code("MALFORMED_REQUEST")
                .detail(message)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST, error));
    }

    /** Domain guard failures thrown as plain argument/state violations (price, hex, money, etc.). */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponse<?>> handleBadInput(RuntimeException ex) {
        var error = ApiError.builder()
                .code("INVALID_REQUEST")
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST, error));
    }

    /** Anything unexpected — consistent 500 envelope, still logged with the full stack trace. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpected(Exception ex, Locale locale) {
        log.error("Unhandled exception", ex);

        String message = msg("INTERNAL_ERROR", locale);
        var error = ApiError.builder()
                .code("INTERNAL_ERROR")
                .detail(message)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    /** Resolves an envelope message code against messages_*.properties, falling back to the code itself. */
    private String msg(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }
}