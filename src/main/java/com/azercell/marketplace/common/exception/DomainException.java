package com.azercell.marketplace.common.exception;


import com.azercell.marketplace.common.domain.ErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

}