package com.ufrn.pertindetu.auth.controller;

import com.ufrn.pertindetu.auth.dto.LoginDTO;
import com.ufrn.pertindetu.auth.dto.TokenDTO;
import com.ufrn.pertindetu.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }
}