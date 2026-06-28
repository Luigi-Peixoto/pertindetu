package com.ufrn.pertindetu.offering.dto;

import java.math.BigDecimal;

import com.ufrn.pertindetu.base.dto.EntityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request/response payload for an offering.
 */
public record OfferingDTO(
        Long id,
        @NotBlank(message = "Title is required.") String title,
        String description,
        @PositiveOrZero(message = "Price cannot be negative.") BigDecimal price,
        String category,
        String neighborhood,
        String city,
        @NotNull(message = "Provider is required.") Long providerId) implements EntityDTO {

    @Override
    public EntityDTO toResponse() {
        return this;
    }
}