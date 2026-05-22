package com.ufrn.pertindetu.base.controller;

import com.ufrn.pertindetu.base.dto.ApiResponseDTO;
import com.ufrn.pertindetu.base.dto.EntityDTO;
import com.ufrn.pertindetu.base.model.BaseEntity;
import com.ufrn.pertindetu.base.service.GenericService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * A generic controller providing common CRUD operations for entities in the
 * application.
 * This abstract class defines methods for handling HTTP requests related to
 * entity management.
 *
 * @param <E>   The type of the entity extending BaseEntity.
 * @param <D> The type of the DTO (Data Transfer Object) associated with the
 *              entity.
 * @param <S>   The type of the service extending GenericService for the entity.
 */
@Validated
public abstract class GenericController<E extends BaseEntity, D extends EntityDTO, S extends GenericService<E, D>> {

    protected S service;

    /**
     * Constructs a GenericController instance with the provided service.
     *
     * @param service The service associated with the controller.
     */
    protected GenericController(S service) {
        this.service = service;
    }

    /**
     * Get all the entities.
     *
     * @return ResponseEntity containing the list of DTOs and status 200 (OK).
     * @param pageable The pagination information for the request.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageImpl<EntityDTO>>> getAll(@ParameterObject Pageable pageable) {
        var page = service.findAll(pageable);
        var res = new PageImpl<>(page.getContent().stream().map(D::toResponse).toList(), pageable,
                page.getTotalElements());

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                res,
                null));
    }

    /**
     * Get an entity by its ID.
     *
     * @param id The ID of the entity to get.
     * @return ResponseEntity containing the DTO and status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                service.findById(id).toResponse(),
                null));
    }

    /**
     * Save a new entity.
     *
     * @param d The DTO representing the entity to save.
     * @return ResponseEntity containing the DTO and status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<EntityDTO>> create(@Valid @RequestBody D d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(
                true,
                service.create(d).toResponse(),
                null));
    }

    /**
     * Update an existing entity.
     *
     * @param id  The ID of the entity to update.
     * @param d The DTO representing the updated entity.
     * @return ResponseEntity containing the DTO and status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> update(@PathVariable Long id, @Valid @RequestBody D d) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                service.update(id, d).toResponse(),
                null));
    }

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return ResponseEntity with status 200 (OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<D>> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                null,
                null));
    }
}

