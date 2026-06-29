package com.ufrn.pertindetu.provider.mapper;

import com.ufrn.pertindetu.base.mappers.BaseMapperConfig;
import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.provider.dto.ProviderProfileDTO;
import com.ufrn.pertindetu.provider.model.ProviderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface ProviderProfileMapper extends DtoMapper<ProviderProfile, ProviderProfileDTO> {

    @Override
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "active", source = "active")
    ProviderProfileDTO toDto(ProviderProfile entity);

    @Override
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    ProviderProfile toEntity(ProviderProfileDTO dto);
}