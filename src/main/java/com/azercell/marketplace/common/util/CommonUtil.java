package com.azercell.marketplace.common.util;

import com.azercell.marketplace.catalog.web.dto.SpecificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommonUtil {
    private final ObjectMapper objectMapper;

    public String toJson(List<SpecificationDto> specifications) {
        if (specifications == null || specifications.isEmpty()) return null;
        try {
            // Convert List -> Map -> JSON object {"key": "value", ...}
            Map<String, String> specsMap = specifications.stream()
                    .collect(Collectors.toMap(
                            SpecificationDto::specificationKey,
                            SpecificationDto::specificationValue,
                            (existing, duplicate) -> existing  // handle duplicate keys
                    ));
            return objectMapper.writeValueAsString(specsMap);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize specifications", e);
        }
    }
}
