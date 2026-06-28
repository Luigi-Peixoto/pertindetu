package com.ufrn.pertindetu.provider.controller;

import com.ufrn.pertindetu.provider.dto.ProviderProfileDTO;
import com.ufrn.pertindetu.provider.service.ProviderProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ufrn.pertindetu.base.dto.UserDetailsInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/providers/me")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService providerService;

    // Retorna a minha própria vitrine
    @GetMapping
    public ResponseEntity<ProviderProfileDTO> getMyProfile(@AuthenticationPrincipal UserDetailsInfo userDetails) {
        return ResponseEntity.ok(providerService.getMyProfile(userDetails.getEmail()));
    }

    // Cria ou atualiza a minha vitrine
    @PutMapping
    public ResponseEntity<ProviderProfileDTO> createOrUpdate(
            @AuthenticationPrincipal UserDetailsInfo userDetails,
            @Valid @RequestBody ProviderProfileDTO dto) {
        return ResponseEntity.ok(providerService.createOrUpdate(userDetails.getEmail(), dto));
    }
}