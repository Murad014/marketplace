package com.azercell.marketplace.catalog.domain.vo;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


public record Specs(String json) {
    private static final ObjectMapper M = new ObjectMapper();

    public Specs {
        if (json == null || json.isBlank()) {
            json = null;
        } else {
            JsonNode n;
            try {
                n = M.readTree(json.trim());
            } catch (Exception e) {
                throw new DomainException(ErrorCode.SPECS_INVALID_JSON);
            }
            // Outside the catch, so a valid-but-non-object payload reports the right error
            // instead of being masked as "invalid JSON".
            if (!n.isObject())
                throw new DomainException(ErrorCode.SPECS_NOT_OBJECT);
            json = json.trim();
        }
    }
}
