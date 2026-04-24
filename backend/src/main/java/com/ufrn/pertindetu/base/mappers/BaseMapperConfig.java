package com.ufrn.pertindetu.base.mappers;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Centralized MapStruct configuration shared across all mappers in the project.
 * <p>
 * Apply via: {@code @Mapper(config = BaseMapperConfig.class)}
 * <ul>
 *   <li>{@code componentModel = "spring"} — mappers are injected as Spring beans.</li>
 *   <li>{@code unmappedTargetPolicy = ERROR} — compile-time error when a target field
 *       has no source mapping, preventing silent data loss.</li>
 * </ul>
 */
@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BaseMapperConfig {
}
