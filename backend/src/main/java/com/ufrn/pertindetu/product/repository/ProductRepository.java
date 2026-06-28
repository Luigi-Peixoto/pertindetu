package com.ufrn.pertindetu.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.product.model.Product;

@Repository
public interface ProductRepository extends GenericRepository<Product> {

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByNeighborhoodIgnoreCase(String neighborhood);

    List<Product> findByProviderId(Long providerId);
}