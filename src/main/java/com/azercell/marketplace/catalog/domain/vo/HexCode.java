package com.azercell.marketplace.catalog.domain.vo;

import java.util.regex.Pattern;

public record HexCode(String value) {
    private static final Pattern P = Pattern.compile("^#[0-9A-F]{6}$");
    public HexCode {
        if (value == null || !P.matcher(value.trim().toUpperCase()).matches())
            throw new RuntimeException("Hex code must be #RRGGBB");
        value = value.trim().toUpperCase();
    }
}