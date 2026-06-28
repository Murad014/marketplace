package com.azercell.marketplace.users.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.security.CurrentUserProvider;
import com.azercell.marketplace.users.application.service.UserService;
import com.azercell.marketplace.users.domain.aggregate.User;
import com.azercell.marketplace.users.web.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final CurrentUserProvider currentUserProvider;
    private final UserService userService;

    /** The current authenticated user (JIT-provisioned from the token on first call). */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        var user = userService.getById(currentUserProvider.requireCurrentUserId());
        return ResponseEntity.ok(ApiResponse.ok(toResponse(user)));
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getAdObjectId(), u.getEmployeeId(), u.getEmail(),
                u.getFullName(), u.getDepartment(), u.getRole().name(), u.isActive());
    }
}