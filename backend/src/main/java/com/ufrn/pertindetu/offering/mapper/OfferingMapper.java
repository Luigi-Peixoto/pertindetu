package com.ufrn.pertindetu.offering.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.offering.dto.OfferingDTO;
import com.ufrn.pertindetu.offering.model.Offering;

@Component
public class OfferingMapper implements DtoMapper<Offering, OfferingDTO> {

    @Override
    public OfferingDTO toDto(Offering entity) {
        return new OfferingDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getProviderId());
    }

    @Override
    public List<OfferingDTO> toDto(List<Offering> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public Offering toEntity(OfferingDTO dto) {
        Offering entity = new Offering();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setCategory(dto.category());
        entity.setNeighborhood(dto.neighborhood());
        entity.setCity(dto.city());
        entity.setProviderId(dto.providerId());
        return entity;
    }
}