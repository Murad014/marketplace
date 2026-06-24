package com.azercell.marketplace.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiError {

    private final String code;
    private final String detail;
    private final List<FieldError> fields;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
        private final Object rejectedValue;
    }
}
