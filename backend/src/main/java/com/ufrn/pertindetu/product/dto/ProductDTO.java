package com.ufrn.pertindetu.product.dto;

import java.math.BigDecimal;

import com.ufrn.pertindetu.base.dto.EntityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request/response payload for a product.
 */
public record ProductDTO(
        Long id,
        @NotBlank(message = "Name is required.") String name,
        String description,
        @NotNull(message = "Price is required.")
        @PositiveOrZero(message = "Price cannot be negative.") BigDecimal price,
        @NotNull(message = "Stock is required.")
        @PositiveOrZero(message = "Stock cannot be negative.") Integer stock,
        String category,
        String neighborhood,
        String city,
        @NotNull(message = "Provider is required.") Long providerId) implements EntityDTO {

    @Override
    public EntityDTO toResponse() {
        return this;
    }
}