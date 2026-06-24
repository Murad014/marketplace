package com.azercell.marketplace.catalog.domain.vo;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;

import java.util.regex.Pattern;

public record HexCode(String value) {
    private static final Pattern P = Pattern.compile("^#[0-9A-F]{6}$");
    public HexCode {
        if (value == null || !P.matcher(value.trim().toUpperCase()).matches())
            throw new DomainException(ErrorCode.COLOR_HEX_INVALID);
        value = value.trim().toUpperCase();
    }
}