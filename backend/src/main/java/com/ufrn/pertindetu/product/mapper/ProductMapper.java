package com.ufrn.pertindetu.product.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.product.dto.ProductDTO;
import com.ufrn.pertindetu.product.model.Product;

@Component
public class ProductMapper implements DtoMapper<Product, ProductDTO> {

    @Override
    public ProductDTO toDto(Product entity) {
        return new ProductDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCategory(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getProviderId());
    }

    @Override
    public List<ProductDTO> toDto(List<Product> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setStock(dto.stock());
        entity.setCategory(dto.category());
        entity.setNeighborhood(dto.neighborhood());
        entity.setCity(dto.city());
        entity.setProviderId(dto.providerId());
        return entity;
    }
}