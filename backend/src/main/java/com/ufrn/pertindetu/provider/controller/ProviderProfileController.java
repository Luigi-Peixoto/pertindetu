package com.ufrn.pertindetu.provider.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.pertindetu.base.controller.GenericController;
import com.ufrn.pertindetu.base.dto.ApiResponseDTO;
import com.ufrn.pertindetu.base.dto.EntityDTO;
import com.ufrn.pertindetu.provider.dto.ProviderProfileDTO;
import com.ufrn.pertindetu.provider.model.ProviderProfile;
import com.ufrn.pertindetu.provider.service.ProviderProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/providers")
public class ProviderProfileController extends
        GenericController<ProviderProfile, ProviderProfileDTO, ProviderProfileService> {

    public ProviderProfileController(ProviderProfileService service) {
        super(service);
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<EntityDTO>> create(
            @Valid @RequestBody ProviderProfileDTO dto) {
        ProviderProfileDTO saved = service.createOrUpdate(currentEmail(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, saved.toResponse(), null));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> update(
            @PathVariable Long id, @Valid @RequestBody ProviderProfileDTO dto) {
        ProviderProfileDTO saved = service.createOrUpdate(currentEmail(), dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, saved.toResponse(), null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> getMyProfile() {
        ProviderProfileDTO dto = service.getMyProfile(currentEmail());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, dto.toResponse(), null));
    }

    private String currentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}