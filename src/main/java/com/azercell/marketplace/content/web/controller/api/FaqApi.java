package com.azercell.marketplace.content.web.controller.api;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.content.web.dto.response.FaqResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "FAQ", description = "Public, localized FAQ listing for the storefront.")
public interface FaqApi {

    @Operation(summary = "List published FAQ entries",
            description = "Returns active FAQ entries ordered by displayOrder, localized via the "
                    + "`Accept-Language` header (`az` → Azerbaijani, anything else → English). No token required.")
    ResponseEntity<ApiResponse<List<FaqResponse>>> list();
}
