package com.ufrn.pertindetu.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.product.model.Product;

@Repository
public interface ProductRepository extends GenericRepository<Product> {

    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
          AND (
            :category IS NULL
            OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%'))
          )
          AND (
            :neighborhood IS NULL
            OR LOWER(p.neighborhood)
               LIKE LOWER(CONCAT('%', :neighborhood, '%'))
          )
        """)
    Page<Product> search(@Param("category") String category,
                         @Param("neighborhood") String neighborhood,
                         Pageable pageable);

    List<Product> findByProviderId(Long providerId);
}