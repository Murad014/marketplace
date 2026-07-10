package com.azercell.marketplace.catalog.web.controller.api;

import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Categories", description = "Public, localized category listing for the storefront.")
public interface CategoryPublicApi {

    @Operation(summary = "List categories (localized)",
            description = "Returns active categories, each with a single name/description chosen from the "
                    + "`Accept-Language` header (`az` → Azerbaijani, anything else → English). Use `parentId` to "
                    + "build the tree. No token required.")
    ResponseEntity<ApiResponse<List<CategoryResponse>>> list();
}
