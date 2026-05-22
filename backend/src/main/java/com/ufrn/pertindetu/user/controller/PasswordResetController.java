package com.ufrn.pertindetu.user.controller;

import com.ufrn.pertindetu.base.dto.ApiResponseDTO;
import com.ufrn.pertindetu.user.dto.PasswordResetConfirmDTO;
import com.ufrn.pertindetu.user.dto.PasswordResetRequestDTO;
import com.ufrn.pertindetu.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PasswordResetController {

    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<Object>> requestReset(@Valid @RequestBody PasswordResetRequestDTO dto, HttpServletRequest request) {
        // Build a base URL for the frontend from request (could be configured instead)
        String baseUrl = request.getHeader("X-Frontend-Base") != null ? request.getHeader("X-Frontend-Base") : request.getRequestURL().toString();
        userService.initiatePasswordReset(dto.getEmail(), baseUrl);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, null, null));
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ApiResponseDTO<Object>> confirmReset(@Valid @RequestBody PasswordResetConfirmDTO dto) {
        userService.confirmPasswordReset(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, null, null));
    }
}

