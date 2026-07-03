package com.azercell.marketplace.content.web.controller.api;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.content.web.dto.request.CreateFaqRequest;
import com.azercell.marketplace.content.web.dto.request.UpdateFaqRequest;
import com.azercell.marketplace.content.web.dto.response.FaqAdminResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "FAQ (Admin)", description = "Manage bilingual FAQ entries shown on the public storefront. Requires ADMIN.")
public interface FaqAdminApi {

    @Operation(summary = "Create a FAQ entry",
            description = "Creates a bilingual (AZ + EN) FAQ entry. New entries are active by default.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "FAQ created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error (blank question/answer)")
    })
    ResponseEntity<ApiResponse<FaqAdminResponse>> create(CreateFaqRequest request);

    @Operation(summary = "Update a FAQ entry",
            description = "Full update. `displayOrder` and `active` are optional (null leaves them unchanged).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FAQ updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error or FAQ not found (FAQ_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<FaqAdminResponse>> update(
            @Parameter(description = "FAQ id") UUID id,
            UpdateFaqRequest request);

    @Operation(summary = "Delete a FAQ entry")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FAQ deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "FAQ not found (FAQ_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<Void>> delete(@Parameter(description = "FAQ id") UUID id);

    @Operation(summary = "Get a FAQ entry by id (both languages)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FAQ found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "FAQ not found (FAQ_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<FaqAdminResponse>> getById(@Parameter(description = "FAQ id") UUID id);

    @Operation(summary = "List all FAQ entries (active + inactive), ordered by displayOrder")
    ResponseEntity<ApiResponse<List<FaqAdminResponse>>> list();
}
