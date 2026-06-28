package com.ufrn.pertindetu.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.base.service.GenericService;
import com.ufrn.pertindetu.product.dto.ProductDTO;
import com.ufrn.pertindetu.product.mapper.ProductMapper;
import com.ufrn.pertindetu.product.model.Product;
import com.ufrn.pertindetu.product.repository.ProductRepository;

@Service
public class ProductService implements GenericService<Product, ProductDTO> {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Product> getRepository() {
        return repository;
    }

    @Override
    public DtoMapper<Product, ProductDTO> getDtoMapper() {
        return mapper;
    }

    /**
     * Search active products by optional category and/or neighborhood.
     */
    public Page<ProductDTO> search(String category, String neighborhood,
                                   Pageable pageable) {
        Page<Product> page =
                repository.search(category, neighborhood, pageable);
        return new PageImpl<>(mapper.toDto(page.getContent()), pageable,
                page.getTotalElements());
    }
}