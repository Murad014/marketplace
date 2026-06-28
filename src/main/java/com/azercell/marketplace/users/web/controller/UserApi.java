package com.azercell.marketplace.users.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.users.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users (Identity)", description = "Local mirror of the Keycloak (RHSSO) identity. JIT-provisioned on first authenticated request.")
public interface UserApi {

    @Operation(summary = "Get the current user",
            description = "Returns the authenticated user's local profile (provisioned/synced from the Keycloak token).")
    ResponseEntity<ApiResponse<UserResponse>> me();
}
