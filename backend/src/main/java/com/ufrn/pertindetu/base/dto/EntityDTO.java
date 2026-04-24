package com.ufrn.pertindetu.base.dto;

/**
 * Interface for Data Transfer Objects (DTOs) that can convert themselves to a response format.
 * Implementing classes should provide a method to transform the DTO into a suitable response object.
 */
public interface EntityDTO {
    EntityDTO toResponse();
}
