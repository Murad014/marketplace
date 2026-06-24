package com.azercell.marketplace.catalog.domain.vo;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


public record Specs(String json) {
    private static final ObjectMapper M = new ObjectMapper();

    public Specs {
        if (json == null || json.isBlank()) {
            json = null;
        } else {
            try {
                JsonNode n = M.readTree(json.trim());
                if (!n.isObject()) throw new RuntimeException("Specs must be a JSON object");
                json = json.trim();
            } catch (Exception e) {
                throw new RuntimeException("Specs must be valid JSON");
            }
        }
    }
}