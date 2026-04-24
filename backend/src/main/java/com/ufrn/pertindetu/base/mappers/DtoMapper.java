package com.ufrn.pertindetu.base.mappers;

import java.util.List;

/**
 * Base mapper interface to be implemented by MapStruct mappers.
 * <p>
 * Usage example for a concrete mapper:
 * <pre>{@code
 * @Mapper(componentModel = "spring", config = BaseMapperConfig.class)
 * public interface ProductMapper extends DtoMapper<Product, ProductDTO> {
 *     // MapStruct generates toDto(Product), toEntity(ProductDTO) and toDto(List<Product>)
 * }
 * }</pre>
 *
 * @param <E>   The entity type.
 * @param <DTO> The DTO type.
 */
public interface DtoMapper<E, DTO> {

    /**
     * Converts an entity to its corresponding DTO.
     *
     * @param entity The entity to be converted.
     * @return The DTO representing the entity.
     */
    DTO toDto(E entity);

    /**
     * Converts a list of entities to a list of DTOs.
     * Default implementation delegates to {@link #toDto(Object)} for each element.
     *
     * @param entities The list of entities to be converted.
     * @return The list of DTOs representing the entities.
     */
    default List<DTO> toDto(List<E> entities) {
        return entities == null ? List.of() : entities.stream().map(this::toDto).toList();
    }

    /**
     * Converts a DTO to its corresponding entity.
     *
     * @param dto The DTO to be converted.
     * @return The entity representing the DTO.
     */
    E toEntity(DTO dto);

}

