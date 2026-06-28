package com.ufrn.pertindetu.offering.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.offering.model.Offering;

@Repository
public interface OfferingRepository extends GenericRepository<Offering> {

    @Query("""
        SELECT o FROM Offering o
        WHERE o.active = true
          AND (
            :category IS NULL
            OR LOWER(o.category) LIKE LOWER(CONCAT('%', :category, '%'))
          )
          AND (
            :neighborhood IS NULL
            OR LOWER(o.neighborhood)
               LIKE LOWER(CONCAT('%', :neighborhood, '%'))
          )
        """)
    Page<Offering> search(@Param("category") String category,
                          @Param("neighborhood") String neighborhood,
                          Pageable pageable);

    List<Offering> findByProviderId(Long providerId);
}